package com.libraryadminbackend.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.libraryadminbackend.common.ErrorCode;
import com.libraryadminbackend.constant.CommonConstant;
import com.libraryadminbackend.exception.BusinessException;
import com.libraryadminbackend.mapper.BookInfoMapper;
import com.libraryadminbackend.model.dto.bookinfo.BookInfoQueryRequest;
import com.libraryadminbackend.model.entity.BookInfo;
import com.libraryadminbackend.model.vo.bookinfo.BookInfoVO;
import com.libraryadminbackend.service.BookInfoService;
import com.libraryadminbackend.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 图书信息服务实现
 */
@Service
@Slf4j
public class BookInfoServiceImpl extends ServiceImpl<BookInfoMapper, BookInfo> implements BookInfoService {

    @Override
    public BookInfoVO getBookInfoVO(BookInfo bookInfo) {
        if (bookInfo == null) {
            return null;
        }
        BookInfoVO bookInfoVO = new BookInfoVO();
        BeanUtils.copyProperties(bookInfo, bookInfoVO);
        return bookInfoVO;
    }

    @Override
    public List<BookInfoVO> getBookInfoVO(List<BookInfo> bookInfoList) {
        if (CollUtil.isEmpty(bookInfoList)) {
            return new ArrayList<>();
        }
        return bookInfoList.stream().map(this::getBookInfoVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<BookInfo> getQueryWrapper(BookInfoQueryRequest bookInfoQueryRequest) {
        if (bookInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long bookId = bookInfoQueryRequest.getBookId();
        String name = bookInfoQueryRequest.getName();
        String author = bookInfoQueryRequest.getAuthor();
        String isbn = bookInfoQueryRequest.getIsbn();
        String publisher = bookInfoQueryRequest.getPublisher();
        String language = bookInfoQueryRequest.getLanguage();
        Long classId = bookInfoQueryRequest.getClassId();
        Integer status = bookInfoQueryRequest.getStatus();
        String sortField = bookInfoQueryRequest.getSortField();
        String sortOrder = bookInfoQueryRequest.getSortOrder();
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(bookId != null, "book_id", bookId);
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        queryWrapper.like(StrUtil.isNotBlank(author), "author", author);
        queryWrapper.eq(StrUtil.isNotBlank(isbn), "isbn", isbn);
        queryWrapper.like(StrUtil.isNotBlank(publisher), "publisher", publisher);
        queryWrapper.eq(StrUtil.isNotBlank(language), "language", language);
        queryWrapper.eq(classId != null, "class_id", classId);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}
