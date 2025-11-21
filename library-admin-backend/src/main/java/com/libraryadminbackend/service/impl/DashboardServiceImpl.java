package com.libraryadminbackend.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.libraryadminbackend.mapper.BookInfoMapper;
import com.libraryadminbackend.mapper.LendListMapper;
import com.libraryadminbackend.mapper.UserMapper;
import com.libraryadminbackend.model.entity.BookInfo;
import com.libraryadminbackend.model.entity.LendList;
import com.libraryadminbackend.model.entity.User;
import com.libraryadminbackend.model.vo.BorrowTrendVO;
import com.libraryadminbackend.model.vo.DashboardOverviewVO;
import com.libraryadminbackend.service.DashboardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 仪表盘服务实现
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private BookInfoMapper bookInfoMapper;

    @Resource
    private LendListMapper lendListMapper;

    @Override
    public DashboardOverviewVO getOverviewStatistics() {
        DashboardOverviewVO vo = new DashboardOverviewVO();

        // 1. 统计总用户数（读者，启用状态）
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_type", 2)  // 读者
                .eq("status", 1);  // 启用状态
        Long totalUsers = userMapper.selectCount(userQueryWrapper);
        vo.setTotalUsers(totalUsers);

        // 2. 统计总图书数（藏书总数）
        QueryWrapper<BookInfo> bookQueryWrapper = new QueryWrapper<>();
        bookQueryWrapper.eq("status", 1)  // 上架状态
                .select("IFNULL(SUM(total_qty), 0) as total_qty");
        BookInfo bookInfo = bookInfoMapper.selectOne(bookQueryWrapper);
        Long totalBooks = bookInfo != null && bookInfo.getTotalQty() != null
                ? bookInfo.getTotalQty().longValue()
                : 0L;
        vo.setTotalBooks(totalBooks);

        // 3. 统计借阅中数量
        QueryWrapper<LendList> borrowingQueryWrapper = new QueryWrapper<>();
        borrowingQueryWrapper.eq("status", 1);  // 借出状态
        Long borrowingCount = lendListMapper.selectCount(borrowingQueryWrapper);
        vo.setBorrowingCount(borrowingCount);

        // 4. 统计今日新增借阅
        Date todayStart = DateUtil.beginOfDay(new Date());
        Date todayEnd = DateUtil.endOfDay(new Date());
        QueryWrapper<LendList> todayQueryWrapper = new QueryWrapper<>();
        todayQueryWrapper.between("lend_date", todayStart, todayEnd);
        Long todayNewBorrows = lendListMapper.selectCount(todayQueryWrapper);
        vo.setTodayNewBorrows(todayNewBorrows);

        return vo;
    }

    @Override
    public BorrowTrendVO getBorrowTrend() {
        BorrowTrendVO vo = new BorrowTrendVO();
        List<String> dates = new ArrayList<>();
        List<Long> borrowCounts = new ArrayList<>();
        List<Long> returnCounts = new ArrayList<>();

        // 统计最近7天的借阅和归还数据
        for (int i = 6; i >= 0; i--) {
            Date date = DateUtil.offsetDay(new Date(), -i);
            Date dayStart = DateUtil.beginOfDay(date);
            Date dayEnd = DateUtil.endOfDay(date);

            // 格式化日期（MM-DD）
            String dateStr = DateUtil.format(date, "MM-dd");
            dates.add(dateStr);

            // 统计当天借出数量
            QueryWrapper<LendList> borrowQueryWrapper = new QueryWrapper<>();
            borrowQueryWrapper.between("lend_date", dayStart, dayEnd);
            Long borrowCount = lendListMapper.selectCount(borrowQueryWrapper);
            borrowCounts.add(borrowCount);

            // 统计当天归还数量
            QueryWrapper<LendList> returnQueryWrapper = new QueryWrapper<>();
            returnQueryWrapper.between("back_date", dayStart, dayEnd)
                    .eq("status", 2);  // 已归还状态
            Long returnCount = lendListMapper.selectCount(returnQueryWrapper);
            returnCounts.add(returnCount);
        }

        vo.setDates(dates);
        vo.setBorrowCounts(borrowCounts);
        vo.setReturnCounts(returnCounts);

        return vo;
    }
}
