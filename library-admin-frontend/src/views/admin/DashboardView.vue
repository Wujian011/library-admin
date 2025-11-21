<template>
  <div>
    <h1 class="text-2xl font-bold mb-6">数据统计</h1>
    <div v-if="loading" class="flex justify-center items-center py-20">
      <UIcon name="i-heroicons-arrow-path" class="w-8 h-8 animate-spin text-primary" />
    </div>
    <div v-else-if="error" class="text-center py-20">
      <p class="text-red-500">{{ error }}</p>
      <UButton @click="loadStatistics" class="mt-4">重新加载</UButton>
    </div>
    <div v-else class="grid grid-cols-1 md:grid-cols-4 gap-4">
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
import { ref, onMounted, computed } from 'vue'
import { getOverviewStatistics, type DashboardOverviewVO } from '@/api/dashboard'

const loading = ref(false)
const error = ref('')
const data = ref<DashboardOverviewVO | null>(null)

const stats = computed(() => {
  if (!data.value) {
    return []
  }
  return [
    {
      label: '总用户数',
      value: data.value.totalUsers.toLocaleString(),
      icon: 'i-heroicons-users'
    },
    {
      label: '总图书数',
      value: data.value.totalBooks.toLocaleString(),
      icon: 'i-heroicons-book-open'
    },
    {
      label: '借阅中',
      value: data.value.borrowingCount.toLocaleString(),
      icon: 'i-heroicons-clipboard-document-list'
    },
    {
      label: '今日新增',
      value: data.value.todayNewBorrows.toLocaleString(),
      icon: 'i-heroicons-arrow-trending-up'
    }
  ]
})

const loadStatistics = async () => {
  loading.value = true
  error.value = ''
  try {
    const response = await getOverviewStatistics()
    if (response.code === 0) {
      data.value = response.data
    } else {
      error.value = response.message || '加载统计数据失败'
    }
  } catch (err: any) {
    error.value = err.message || '加载统计数据失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadStatistics()
})
</script>
