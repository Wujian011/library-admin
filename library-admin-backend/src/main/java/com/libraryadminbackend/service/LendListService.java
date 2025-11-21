package com.libraryadminbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.libraryadminbackend.model.dto.lendlist.LendListQueryRequest;
import com.libraryadminbackend.model.entity.LendList;
import com.libraryadminbackend.model.vo.lendlist.LendListVO;

import java.util.List;

/**
 * 借阅记录服务
 */
public interface LendListService extends IService<LendList> {

    /**
     * 获取查询条件
     *
     * @param lendListQueryRequest
     * @return
     */
    QueryWrapper<LendList> getQueryWrapper(LendListQueryRequest lendListQueryRequest);

    /**
     * 获取借阅记录视图
     *
     * @param lendList
     * @return
     */
    LendListVO getLendListVO(LendList lendList);

    /**
     * 批量获取借阅记录视图
     *
     * @param lendListList
     * @return
     */
    List<LendListVO> getLendListVO(List<LendList> lendListList);

    /**
     * 借书
     *
     * @param userId
     * @param bookId
     * @param dueDate
     * @param note
     * @return
     */
    Long borrowBook(Long userId, Long bookId, java.util.Date dueDate, String note);

    /**
     * 还书
     *
     * @param lendId
     * @return
     */
    boolean returnBook(Long lendId);
}
