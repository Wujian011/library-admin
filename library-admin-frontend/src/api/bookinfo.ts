import request from './request'
import type { BaseResponse } from './request'

/**
 * 分页获取图书信息列表（管理员）
 */
export const listBookInfoByPage = (data: any) => {
  return request.post<any, BaseResponse<any>>('/bookinfo/list/page', data)
}

/**
 * 分页获取图书信息列表（公开）
 */
export const listBookInfoVOByPage = (data: any) => {
  return request.post<any, BaseResponse<any>>('/bookinfo/list/page/vo', data)
}

/**
 * 添加图书信息
 */
export const addBookInfo = (data: any) => {
  return request.post<any, BaseResponse<number>>('/bookinfo/add', data)
}

/**
 * 更新图书信息
 */
export const updateBookInfo = (data: any) => {
  return request.post<any, BaseResponse<boolean>>('/bookinfo/update', data)
}

/**
 * 删除图书信息
 */
export const deleteBookInfo = (id: number) => {
  return request.post<any, BaseResponse<boolean>>('/bookinfo/delete', { id })
}

/**
 * 获取图书信息详情（管理员）
 */
export const getBookInfo = (bookId: number) => {
  return request.get<any, BaseResponse<any>>('/bookinfo/get', { params: { bookId } })
}

/**
 * 获取图书信息详情（公开）
 */
export const getBookInfoVO = (bookId: number) => {
  return request.get<any, BaseResponse<any>>('/bookinfo/get/vo', { params: { bookId } })
}
