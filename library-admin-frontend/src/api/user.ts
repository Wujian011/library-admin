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
  userType: number // 1=管理员，2=读者
  username: string // 登录用户名
  name?: string // 真实姓名
  cardNo?: string // 借阅证号（仅读者）
  sex?: number // 性别：1男 2女 0未知
  birth?: string // 出生日期
  address?: string // 联系地址
  phone?: string // 联系电话
  status?: number // 账号状态：1启用 0禁用
  lastLoginAt?: string // 最后登录时间
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
