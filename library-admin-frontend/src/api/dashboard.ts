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

/**
 * 获取首页概览统计数据
 */
export const getOverviewStatistics = () => {
  return request.get<any, BaseResponse<DashboardOverviewVO>>('/dashboard/overview')
}
