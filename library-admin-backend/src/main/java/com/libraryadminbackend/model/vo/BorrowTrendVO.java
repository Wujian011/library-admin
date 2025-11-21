package com.libraryadminbackend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 借阅趋势统计 VO
 */
@Data
public class BorrowTrendVO implements Serializable {

    /**
     * 日期列表
     */
    private List<String> dates;

    /**
     * 借阅数量列表
     */
    private List<Long> borrowCounts;

    /**
     * 归还数量列表
     */
    private List<Long> returnCounts;

    private static final long serialVersionUID = 1L;
}
