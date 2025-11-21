package com.libraryadminbackend.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 仪表盘概览统计 VO
 */
@Data
public class DashboardOverviewVO implements Serializable {

    /**
     * 总用户数
     */
    private Long totalUsers;

    /**
     * 总图书数（藏书总数）
     */
    private Long totalBooks;

    /**
     * 借阅中数量
     */
    private Long borrowingCount;

    /**
     * 今日新增借阅
     */
    private Long todayNewBorrows;

    private static final long serialVersionUID = 1L;
}
