<template>
  <div>
    <h1 class="text-2xl font-bold mb-6">数据统计</h1>
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
      <UCard v-for="stat in stats" :key="stat.label">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm text-gray-500">{{ stat.label }}</p>
            <p class="text-2xl font-bold mt-1">{{ stat.value }}</p>
          </div>
          <UIcon :name="stat.icon" class="w-8 h-8 text-primary" />
        </div>
      </UCard>
    </div>
  </div>
</template>

<script setup lang="ts">
const stats = [
  { label: '总用户数', value: '1,234', icon: 'i-heroicons-users' },
  { label: '总图书数', value: '5,678', icon: 'i-heroicons-book-open' },
  { label: '借阅中', value: '234', icon: 'i-heroicons-clipboard-document-list' },
  { label: '今日新增', value: '12', icon: 'i-heroicons-arrow-trending-up' }
]
</script>
