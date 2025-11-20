<template>
  <div class="min-h-screen bg-gray-50 dark:bg-gray-900">
    <AppHeader />

    <div class="flex">
      <!-- 左侧菜单 -->
      <aside class="w-64 bg-white dark:bg-gray-800 border-r border-gray-200 dark:border-gray-700 min-h-[calc(100vh-4rem)]">
        <nav class="p-4 space-y-1">
          <UButton
            v-for="item in menuItems"
            :key="item.path"
            :to="item.path"
            :icon="item.icon"
            :label="item.label"
            color="gray"
            variant="ghost"
            block
            class="justify-start"
            :class="{ 'bg-gray-100 dark:bg-gray-700': $route.path === item.path }"
          />
        </nav>
      </aside>

      <!-- 主内容区 -->
      <main class="flex-1 p-6">
        <RouterView />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import AppHeader from '@/components/AppHeader.vue'

const menuItems = [
  { path: '/admin/dashboard', label: '首页', icon: 'i-heroicons-home' },
  { path: '/admin/users', label: '用户管理', icon: 'i-heroicons-users' },
  { path: '/admin/books', label: '图书管理', icon: 'i-heroicons-book-open' },
  { path: '/admin/categories', label: '分类管理', icon: 'i-heroicons-tag' },
  { path: '/admin/borrows', label: '借阅管理', icon: 'i-heroicons-clipboard-document-list' },
  { path: '/admin/settings', label: '系统设置', icon: 'i-heroicons-cog-6-tooth' }
]
</script>
