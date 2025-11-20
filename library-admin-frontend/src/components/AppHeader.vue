<template>
  <header class="bg-white dark:bg-gray-800 border-b border-gray-200 dark:border-gray-700">
    <div class="flex items-center justify-between h-16 px-6">
      <div class="flex items-center gap-3">
        <UIcon name="i-heroicons-book-open" class="w-8 h-8 text-primary" />
        <span class="text-xl font-bold">图书管理系统</span>
      </div>

      <UDropdown :items="userMenuItems">
        <UButton color="gray" variant="ghost" class="gap-2">
          <UAvatar :alt="userStore.userInfo?.userName || userStore.userInfo?.userAccount" size="sm" />
          <span>{{ userStore.userInfo?.userName || userStore.userInfo?.userAccount }}</span>
          <UIcon name="i-heroicons-chevron-down" />
        </UButton>
      </UDropdown>
    </div>
  </header>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()
const toast = useToast()

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

const userMenuItems = [
  [{
    label: '个人中心',
    icon: 'i-heroicons-user',
    click: () => {
      const isAdmin = userStore.userInfo?.userType === 1
      router.push(isAdmin ? '/admin/settings' : '/user/profile')
    }
  }],
  [{
    label: '退出登录',
    icon: 'i-heroicons-arrow-right-on-rectangle',
    click: handleLogout
  }]
]
</script>
