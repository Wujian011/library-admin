import request from './request'

// 用户注册请求参数
export interface UserRegisterRequest {
  userAccount: string
  userPassword: string
  checkPassword: string
}

// 用户登录请求参数
export interface UserLoginRequest {
  userAccount: string
  userPassword: string
}

// 登录用户信息
export interface LoginUserVO {
  userId: number
  userAccount: string
  userName?: string
  userAvatar?: string
  userProfile?: string
  userRole: string
  userType: number // 1=管理员，2=读者
  createTime: string
  updateTime: string
}

// 用户分页查询参数
export interface UserQueryRequest {
  current: number
  pageSize: number
  userAccount?: string
  userName?: string
  userRole?: string
}

// 分页响应
export interface PageResponse<T> {
  records: T[]
  total: number
  current: number
  size: number
}

// 基础响应结构
export interface BaseResponse<T = any> {
  code: number
  data: T
  message: string
}

/**
 * 用户注册
 */
export const userRegister = (data: UserRegisterRequest) => {
  return request.post<any, BaseResponse<number>>('/user/register', data)
}

/**
 * 用户登录
 */
export const userLogin = (data: UserLoginRequest) => {
  return request.post<any, BaseResponse<LoginUserVO>>('/user/login', data)
}

/**
 * 用户注销
 */
export const userLogout = () => {
  return request.post<any, BaseResponse<boolean>>('/user/logout')
}

/**
 * 获取当前登录用户
 */
export const getLoginUser = () => {
  return request.get<any, BaseResponse<LoginUserVO>>('/user/get/login')
}

/**
 * 分页获取用户列表
 */
export const listUserByPage = (data: UserQueryRequest) => {
  return request.post<any, BaseResponse<PageResponse<LoginUserVO>>>('/user/list/page', data)
}

/**
 * 删除用户
 */
export const deleteUser = (id: number) => {
  return request.post<any, BaseResponse<boolean>>('/user/delete', { id })
}

/**
 * 添加用户
 */
export const addUser = (data: any) => {
  return request.post<any, BaseResponse<number>>('/user/add', data)
}

/**
 * 更新用户
 */
export const updateUser = (data: any) => {
  return request.post<any, BaseResponse<boolean>>('/user/update', data)
}
