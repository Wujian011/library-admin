import { defineStore } from 'pinia'
import { ref } from 'vue'
import { userLogin, userRegister, userLogout, type LoginUserVO } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref<LoginUserVO | null>(null)

  // 登录
  const login = async (userAccount: string, userPassword: string) => {
    const res = await userLogin({ userAccount, userPassword })
    userInfo.value = res.data
  }

  // 注册
  const register = async (userAccount: string, userPassword: string, checkPassword: string) => {
    await userRegister({ userAccount, userPassword, checkPassword })
    return { code: 0, message: '注册成功' }
  }

  // 登出
  const logout = async () => {
    await userLogout()
    userInfo.value = null
  }

  // 检查是否已登录
  const isLoggedIn = () => {
    return !!userInfo.value
  }

  return {
    userInfo,
    login,
    register,
    logout,
    isLoggedIn
  }
}, {
  persist: true
})
