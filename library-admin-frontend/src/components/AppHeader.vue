<template>
  <header class="bg-white dark:bg-gray-800 border-b border-gray-200 dark:border-gray-700">
    <div class="flex items-center justify-between h-16 px-6">
      <div class="flex items-center gap-3">
        <UIcon name="i-heroicons-book-open" class="w-8 h-8 text-primary" />
        <span class="text-xl font-bold">图书管理系统</span>
      </div>

      <div class="relative">
        <UButton
          color="gray"
          variant="ghost"
          class="gap-2"
          @click="isMenuOpen = !isMenuOpen"
        >
          <UAvatar :alt="userStore.userInfo?.name || userStore.userInfo?.username" size="sm" />
          <span>{{ userStore.userInfo?.name || userStore.userInfo?.username }}</span>
          <UIcon name="i-heroicons-chevron-down" />
        </UButton>

        <!-- 下拉菜单 -->
        <div
          v-if="isMenuOpen"
          class="absolute right-0 mt-2 w-48 bg-white dark:bg-gray-800 rounded-lg shadow-lg border border-gray-200 dark:border-gray-700 py-1 z-50"
          @click="isMenuOpen = false"
        >
          <button
            @click="handleProfile"
            class="w-full px-4 py-2 text-left text-sm text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 flex items-center gap-2"
          >
            <UIcon name="i-heroicons-user" class="w-4 h-4" />
            个人中心
          </button>
          <button
            @click="handleLogout"
            class="w-full px-4 py-2 text-left text-sm text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 flex items-center gap-2"
          >
            <UIcon name="i-heroicons-arrow-right-on-rectangle" class="w-4 h-4" />
            退出登录
          </button>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()
const toast = useToast()
const isMenuOpen = ref(false)

const handleProfile = () => {
  const isAdmin = userStore.userInfo?.userType === 1
  router.push(isAdmin ? '/admin/profile' : '/user/profile')
}

const handleLogout = async () => {
  try {
    await userStore.logout()
    toast.add({
      title: '退出成功',
      color: 'green'
    })
    router.push('/login')
  } catch (error: any) {
    toast.add({
      title: '退出失败',
      description: error.message,
      color: 'red'
    })
  }
}
</script>
