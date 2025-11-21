import axios, { type AxiosInstance, type AxiosResponse } from 'axios'

// 创建 axios 实例
const request: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8123/api',
  timeout: 10000,
  withCredentials: true, // 携带 cookie
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 可以在这里添加 token 等
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response: AxiosResponse) => {
    const { data } = response

    // 后端返回的数据结构：{ code, data, message }
    if (data.code === 0) {
      return data
    } else if (data.code === 40100) {
      // 用户未登录，清除本地存储并跳转到登录页
      localStorage.removeItem('user')
      window.location.href = '/login'
      return Promise.reject(new Error(data.message || '未登录'))
    } else {
      // 业务错误
      return Promise.reject(new Error(data.message || '请求失败'))
    }
  },
  (error) => {
    // HTTP 错误
    let message = '请求失败'

    if (error.response) {
      switch (error.response.status) {
        case 401:
          message = '未授权，请重新登录'
          break
        case 403:
          message = '拒绝访问'
          break
        case 404:
          message = '请求地址不存在'
          break
        case 500:
          message = '服务器内部错误'
          break
        default:
          message = error.response.data?.message || '请求失败'
      }
    } else if (error.request) {
      message = '网络错误，请检查网络连接'
    }

    return Promise.reject(new Error(message))
  }
)

export default request
