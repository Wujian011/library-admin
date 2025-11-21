import request from './request'

// 基础响应结构
export interface BaseResponse<T = any> {
  code: number
  data: T
  message: string
}

// 仪表盘概览统计数据
export interface DashboardOverviewVO {
  totalUsers: number      // 总用户数
  totalBooks: number      // 总图书数
  borrowingCount: number  // 借阅中数量
  todayNewBorrows: number // 今日新增借阅
}

// 借阅趋势统计数据
export interface BorrowTrendVO {
  dates: string[]         // 日期列表
  borrowCounts: number[]  // 借阅数量列表
  returnCounts: number[]  // 归还数量列表
}

/**
 * 获取首页概览统计数据
 */
export const getOverviewStatistics = () => {
  return request.get<any, BaseResponse<DashboardOverviewVO>>('/dashboard/overview')
}

/**
 * 获取借阅趋势统计数据（最近7天）
 */
export const getBorrowTrend = () => {
  return request.get<any, BaseResponse<BorrowTrendVO>>('/dashboard/borrow-trend')
}
