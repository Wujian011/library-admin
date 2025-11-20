import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null as any,
    token: ''
  }),

  actions: {
    setUser(user: any) {
      this.user = user
    },

    setToken(token: string) {
      this.token = token
      const tokenCookie = useCookie('token')
      tokenCookie.value = token
    },

    logout() {
      this.user = null
      this.token = ''
      const tokenCookie = useCookie('token')
      tokenCookie.value = null
    }
  },

  getters: {
    isLoggedIn: (state) => !!state.token
  }
})
