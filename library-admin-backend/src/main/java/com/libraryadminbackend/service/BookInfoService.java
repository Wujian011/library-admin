package com.libraryadminbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.libraryadminbackend.model.dto.bookinfo.BookInfoQueryRequest;
import com.libraryadminbackend.model.entity.BookInfo;
import com.libraryadminbackend.model.vo.bookinfo.BookInfoVO;

import java.util.List;

/**
 * 图书信息服务
 */
public interface BookInfoService extends IService<BookInfo> {

    /**
     * 获取图书信息视图
     *
     * @param bookInfo
     * @return
     */
    BookInfoVO getBookInfoVO(BookInfo bookInfo);

    /**
     * 获取图书信息视图列表
     *
     * @param bookInfoList
     * @return
     */
    List<BookInfoVO> getBookInfoVO(List<BookInfo> bookInfoList);

    /**
     * 获取查询条件
     *
     * @param bookInfoQueryRequest
     * @return
     */
    QueryWrapper<BookInfo> getQueryWrapper(BookInfoQueryRequest bookInfoQueryRequest);

}
