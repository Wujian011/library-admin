import request from './request'
import type { BaseResponse } from './request'

/**
 * 分页获取借阅记录列表（管理员）
 */
export const listLendListByPage = (data: any) => {
  return request.post<any, BaseResponse<any>>('/lendlist/list/page', data)
}

/**
 * 获取当前用户的借阅记录
 */
export const listMyLendListByPage = (data: any) => {
  return request.post<any, BaseResponse<any>>('/lendlist/my/list/page', data)
}

/**
 * 添加借阅记录（借书）
 */
export const addLendList = (data: any) => {
  return request.post<any, BaseResponse<number>>('/lendlist/add', data)
}

/**
 * 更新借阅记录
 */
export const updateLendList = (data: any) => {
  return request.post<any, BaseResponse<boolean>>('/lendlist/update', data)
}

/**
 * 删除借阅记录
 */
export const deleteLendList = (id: number) => {
  return request.post<any, BaseResponse<boolean>>('/lendlist/delete', { id })
}

/**
 * 还书
 */
export const returnBook = (lendId: number) => {
  return request.post<any, BaseResponse<boolean>>('/lendlist/return', null, { params: { lendId } })
}

/**
 * 获取借阅记录详情
 */
export const getLendList = (id: number) => {
  return request.get<any, BaseResponse<any>>('/lendlist/get', { params: { id } })
}
