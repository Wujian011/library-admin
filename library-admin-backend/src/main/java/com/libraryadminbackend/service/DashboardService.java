package com.libraryadminbackend.service;

import com.libraryadminbackend.model.vo.DashboardOverviewVO;

/**
 * 仪表盘服务
 */
public interface DashboardService {

    /**
     * 获取首页概览统计数据
     *
     * @return 统计数据
     */
    DashboardOverviewVO getOverviewStatistics();
}
