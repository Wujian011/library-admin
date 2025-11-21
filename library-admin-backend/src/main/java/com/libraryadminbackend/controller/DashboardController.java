package com.libraryadminbackend.controller;

import com.libraryadminbackend.annotation.AuthCheck;
import com.libraryadminbackend.common.BaseResponse;
import com.libraryadminbackend.common.ResultUtils;
import com.libraryadminbackend.constant.UserConstant;
import com.libraryadminbackend.model.vo.BorrowTrendVO;
import com.libraryadminbackend.model.vo.DashboardOverviewVO;
import com.libraryadminbackend.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 仪表盘接口
 */
@RestController
@RequestMapping("/dashboard")
@Slf4j
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    /**
     * 获取首页概览统计数据
     *
     * @return 统计数据
     */
    @GetMapping("/overview")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<DashboardOverviewVO> getOverviewStatistics() {
        DashboardOverviewVO statistics = dashboardService.getOverviewStatistics();
        return ResultUtils.success(statistics);
    }

    /**
     * 获取借阅趋势统计数据（最近7天）
     *
     * @return 借阅趋势数据
     */
    @GetMapping("/borrow-trend")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<BorrowTrendVO> getBorrowTrend() {
        BorrowTrendVO trend = dashboardService.getBorrowTrend();
        return ResultUtils.success(trend);
    }
}
