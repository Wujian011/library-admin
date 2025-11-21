package com.libraryadminbackend.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.libraryadminbackend.common.ErrorCode;
import com.libraryadminbackend.exception.BusinessException;
import com.libraryadminbackend.mapper.LendListMapper;
import com.libraryadminbackend.model.dto.lendlist.LendListQueryRequest;
import com.libraryadminbackend.model.entity.BookInfo;
import com.libraryadminbackend.model.entity.LendList;
import com.libraryadminbackend.model.entity.User;
import com.libraryadminbackend.model.vo.lendlist.LendListVO;
import com.libraryadminbackend.service.BookInfoService;
import com.libraryadminbackend.service.LendListService;
import com.libraryadminbackend.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 借阅记录服务实现
 */
@Service
public class LendListServiceImpl extends ServiceImpl<LendListMapper, LendList> implements LendListService {

    @Resource
    private UserService userService;

    @Resource
    private BookInfoService bookInfoService;

    @Override
    public QueryWrapper<LendList> getQueryWrapper(LendListQueryRequest lendListQueryRequest) {
        QueryWrapper<LendList> queryWrapper = new QueryWrapper<>();
        if (lendListQueryRequest == null) {
            return queryWrapper;
        }
        Long lendId = lendListQueryRequest.getLendId();
        Long userId = lendListQueryRequest.getUserId();
        Long bookId = lendListQueryRequest.getBookId();
        Integer status = lendListQueryRequest.getStatus();
        Date lendDateStart = lendListQueryRequest.getLendDateStart();
        Date lendDateEnd = lendListQueryRequest.getLendDateEnd();
        String sortField = lendListQueryRequest.getSortField();
        String sortOrder = lendListQueryRequest.getSortOrder();

        queryWrapper.eq(lendId != null, "lend_id", lendId);
        queryWrapper.eq(userId != null, "user_id", userId);
        queryWrapper.eq(bookId != null, "book_id", bookId);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.ge(lendDateStart != null, "lend_date", lendDateStart);
        queryWrapper.le(lendDateEnd != null, "lend_date", lendDateEnd);
        queryWrapper.orderBy(sortField != null, "ascend".equals(sortOrder), sortField);
        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }

    @Override
    public LendListVO getLendListVO(LendList lendList) {
        if (lendList == null) {
            return null;
        }
        LendListVO lendListVO = new LendListVO();
        BeanUtils.copyProperties(lendList, lendListVO);

        // 查询用户信息
        if (lendList.getUserId() != null) {
            User user = userService.getById(lendList.getUserId());
            if (user != null) {
                lendListVO.setUserName(user.getName());
                lendListVO.setCardNo(user.getCardNo());
            }
        }

        // 查询图书信息
        if (lendList.getBookId() != null) {
            BookInfo bookInfo = bookInfoService.getById(lendList.getBookId());
            if (bookInfo != null) {
                lendListVO.setBookName(bookInfo.getName());
                lendListVO.setIsbn(bookInfo.getIsbn());
            }
        }

        return lendListVO;
    }

    @Override
    public List<LendListVO> getLendListVO(List<LendList> lendListList) {
        if (CollUtil.isEmpty(lendListList)) {
            return new ArrayList<>();
        }

        // 批量查询用户信息
        Set<Long> userIds = lendListList.stream()
                .map(LendList::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, User> userMap = new HashMap<>();
        if (!userIds.isEmpty()) {
            List<User> users = userService.listByIds(userIds);
            userMap = users.stream().collect(Collectors.toMap(User::getUserId, u -> u));
        }

        // 批量查询图书信息
        Set<Long> bookIds = lendListList.stream()
                .map(LendList::getBookId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, BookInfo> bookMap = new HashMap<>();
        if (!bookIds.isEmpty()) {
            List<BookInfo> books = bookInfoService.listByIds(bookIds);
            bookMap = books.stream().collect(Collectors.toMap(BookInfo::getBookId, b -> b));
        }

        // 转换为 VO
        Map<Long, User> finalUserMap = userMap;
        Map<Long, BookInfo> finalBookMap = bookMap;
        return lendListList.stream().map(lendList -> {
            LendListVO vo = new LendListVO();
            BeanUtils.copyProperties(lendList, vo);

            User user = finalUserMap.get(lendList.getUserId());
            if (user != null) {
                vo.setUserName(user.getName());
                vo.setCardNo(user.getCardNo());
            }

            BookInfo book = finalBookMap.get(lendList.getBookId());
            if (book != null) {
                vo.setBookName(book.getName());
                vo.setIsbn(book.getIsbn());
            }

            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long borrowBook(Long userId, Long bookId, Date dueDate, String note) {
        // 检查用户是否存在
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }

        // 检查图书是否存在
        BookInfo bookInfo = bookInfoService.getById(bookId);
        if (bookInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "图书不存在");
        }

        // 检查图书是否上架
        if (bookInfo.getStatus() != 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "图书已下架");
        }

        // 检查可借数量
        if (bookInfo.getAvailableQty() <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "图书库存不足");
        }

        // 创建借阅记录
        LendList lendList = new LendList();
        lendList.setUserId(userId);
        lendList.setBookId(bookId);
        lendList.setLendDate(new Date());
        lendList.setDueDate(dueDate);
        lendList.setStatus(1); // 借出状态
        lendList.setNote(note);
        boolean saved = this.save(lendList);
        if (!saved) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "借阅失败");
        }

        // 更新图书可借数量
        bookInfo.setAvailableQty(bookInfo.getAvailableQty() - 1);
        bookInfoService.updateById(bookInfo);

        return lendList.getLendId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean returnBook(Long lendId) {
        // 查询借阅记录
        LendList lendList = this.getById(lendId);
        if (lendList == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "借阅记录不存在");
        }

        // 检查是否已还
        if (lendList.getStatus() == 2) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该图书已归还");
        }

        // 更新借阅状态
        lendList.setStatus(2); // 已还状态
        lendList.setBackDate(new Date());
        boolean updated = this.updateById(lendList);
        if (!updated) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "还书失败");
        }

        // 更新图书可借数量
        BookInfo bookInfo = bookInfoService.getById(lendList.getBookId());
        if (bookInfo != null) {
            bookInfo.setAvailableQty(bookInfo.getAvailableQty() + 1);
            bookInfoService.updateById(bookInfo);
        }

        return true;
    }
}
