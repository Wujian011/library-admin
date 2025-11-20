<template>
  <div class="min-h-screen bg-gray-50 dark:bg-gray-900">
    <AppHeader />

    <div class="border-b border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800">
      <div class="px-6">
        <UTabs :items="tabs" v-model="activeTab" @change="handleTabChange" />
      </div>
    </div>

    <main class="p-6">
      <RouterView />
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import AppHeader from '@/components/AppHeader.vue'

const router = useRouter()
const route = useRoute()

const tabs = [
  { key: 'home', label: '首页', icon: 'i-heroicons-home' },
  { key: 'books', label: '图书列表', icon: 'i-heroicons-book-open' },
  { key: 'borrows', label: '我的借阅', icon: 'i-heroicons-clipboard-document-list' },
  { key: 'profile', label: '个人中心', icon: 'i-heroicons-user' }
]

const activeTab = ref(0)

// 根据当前路由设置激活的 tab
watch(() => route.path, (path) => {
  const index = tabs.findIndex(tab => path.includes(`/user/${tab.key}`))
  if (index !== -1) activeTab.value = index
}, { immediate: true })

const handleTabChange = (index: number) => {
  router.push(`/user/${tabs[index].key}`)
}
</script>
