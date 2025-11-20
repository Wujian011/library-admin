import request from './request'
import type { BaseResponse } from './request'

/**
 * 分页获取图书分类列表
 */
export const listClassInfoByPage = (data: any) => {
  return request.post<any, BaseResponse<any>>('/classinfo/list/page', data)
}

/**
 * 添加图书分类
 */
export const addClassInfo = (data: any) => {
  return request.post<any, BaseResponse<number>>('/classinfo/add', data)
}

/**
 * 更新图书分类
 */
export const updateClassInfo = (data: any) => {
  return request.post<any, BaseResponse<boolean>>('/classinfo/update', data)
}

/**
 * 删除图书分类
 */
export const deleteClassInfo = (id: number) => {
  return request.post<any, BaseResponse<boolean>>('/classinfo/delete', { id })
}

/**
 * 获取图书分类详情
 */
export const getClassInfo = (classId: number) => {
  return request.get<any, BaseResponse<any>>('/classinfo/get', { params: { classId } })
}
