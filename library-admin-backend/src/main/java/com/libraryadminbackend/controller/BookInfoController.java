package com.libraryadminbackend.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.libraryadminbackend.annotation.AuthCheck;
import com.libraryadminbackend.common.BaseResponse;
import com.libraryadminbackend.common.DeleteRequest;
import com.libraryadminbackend.common.ErrorCode;
import com.libraryadminbackend.common.ResultUtils;
import com.libraryadminbackend.constant.UserConstant;
import com.libraryadminbackend.exception.BusinessException;
import com.libraryadminbackend.exception.ThrowUtils;
import com.libraryadminbackend.model.dto.bookinfo.BookInfoAddRequest;
import com.libraryadminbackend.model.dto.bookinfo.BookInfoQueryRequest;
import com.libraryadminbackend.model.dto.bookinfo.BookInfoUpdateRequest;
import com.libraryadminbackend.model.entity.BookInfo;
import com.libraryadminbackend.model.vo.bookinfo.BookInfoVO;
import com.libraryadminbackend.service.BookInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 图书信息接口
 */
@RestController
@RequestMapping("/bookinfo")
@Slf4j
public class BookInfoController {

    @Resource
    private BookInfoService bookInfoService;

    // region 增删改查

    /**
     * 添加图书
     *
     * @param bookInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addBookInfo(@RequestBody BookInfoAddRequest bookInfoAddRequest, HttpServletRequest request) {
        if (bookInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        BookInfo bookInfo = new BookInfo();
        BeanUtils.copyProperties(bookInfoAddRequest, bookInfo);
        // 校验书名
        if (StrUtil.isBlank(bookInfo.getName())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "书名不能为空");
        }
        // 校验藏书总数和可借数量
        if (bookInfo.getTotalQty() != null && bookInfo.getTotalQty() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "藏书总数不能为负数");
        }
        if (bookInfo.getAvailableQty() != null && bookInfo.getAvailableQty() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "可借数量不能为负数");
        }
        if (bookInfo.getTotalQty() != null && bookInfo.getAvailableQty() != null
                && bookInfo.getAvailableQty() > bookInfo.getTotalQty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "可借数量不能大于藏书总数");
        }
        boolean result = bookInfoService.save(bookInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(bookInfo.getBookId());
    }

    /**
     * 删除图书
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteBookInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = bookInfoService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新图书
     *
     * @param bookInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateBookInfo(@RequestBody BookInfoUpdateRequest bookInfoUpdateRequest,
                                                 HttpServletRequest request) {
        if (bookInfoUpdateRequest == null || bookInfoUpdateRequest.getBookId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        BookInfo bookInfo = new BookInfo();
        BeanUtils.copyProperties(bookInfoUpdateRequest, bookInfo);
        // 校验藏书总数和可借数量
        if (bookInfo.getTotalQty() != null && bookInfo.getTotalQty() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "藏书总数不能为负数");
        }
        if (bookInfo.getAvailableQty() != null && bookInfo.getAvailableQty() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "可借数量不能为负数");
        }
        if (bookInfo.getTotalQty() != null && bookInfo.getAvailableQty() != null
                && bookInfo.getAvailableQty() > bookInfo.getTotalQty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "可借数量不能大于藏书总数");
        }
        boolean result = bookInfoService.updateById(bookInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取图书（仅管理员）
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<BookInfo> getBookInfoById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        BookInfo bookInfo = bookInfoService.getById(id);
        ThrowUtils.throwIf(bookInfo == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(bookInfo);
    }

    /**
     * 根据 id 获取图书视图
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<BookInfoVO> getBookInfoVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        BookInfo bookInfo = bookInfoService.getById(id);
        ThrowUtils.throwIf(bookInfo == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(bookInfoService.getBookInfoVO(bookInfo));
    }

    /**
     * 分页获取图书列表（仅管理员）
     *
     * @param bookInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<BookInfo>> listBookInfoByPage(@RequestBody BookInfoQueryRequest bookInfoQueryRequest,
                                                            HttpServletRequest request) {
        long current = bookInfoQueryRequest.getCurrent();
        long size = bookInfoQueryRequest.getPageSize();
        Page<BookInfo> bookInfoPage = bookInfoService.page(new Page<>(current, size),
                bookInfoService.getQueryWrapper(bookInfoQueryRequest));
        return ResultUtils.success(bookInfoPage);
    }

    /**
     * 分页获取图书视图列表
     *
     * @param bookInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<BookInfoVO>> listBookInfoVOByPage(@RequestBody BookInfoQueryRequest bookInfoQueryRequest,
                                                                HttpServletRequest request) {
        if (bookInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = bookInfoQueryRequest.getCurrent();
        long size = bookInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<BookInfo> bookInfoPage = bookInfoService.page(new Page<>(current, size),
                bookInfoService.getQueryWrapper(bookInfoQueryRequest));
        Page<BookInfoVO> bookInfoVOPage = new Page<>(current, size, bookInfoPage.getTotal());
        List<BookInfoVO> bookInfoVO = bookInfoService.getBookInfoVO(bookInfoPage.getRecords());
        bookInfoVOPage.setRecords(bookInfoVO);
        return ResultUtils.success(bookInfoVOPage);
    }

    // endregion
}
