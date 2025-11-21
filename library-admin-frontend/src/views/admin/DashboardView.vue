<template>
  <div>
    <div class="flex items-center justify-between mb-6">
      <h1 class="text-2xl font-bold">数据统计</h1>
      <UButton
        @click="loadStatistics"
        color="gray"
        variant="soft"
        icon="i-heroicons-arrow-path"
        :loading="loading"
      >
        刷新数据
      </UButton>
    </div>

    <div v-if="loading && !data" class="flex justify-center items-center py-20">
      <UIcon name="i-heroicons-arrow-path" class="w-8 h-8 animate-spin text-primary" />
    </div>
    <div v-else-if="error" class="text-center py-20">
      <UIcon name="i-heroicons-exclamation-triangle" class="w-16 h-16 text-red-500 mx-auto mb-4" />
      <p class="text-red-500 text-lg mb-4">{{ error }}</p>
      <UButton @click="loadStatistics" color="primary">重新加载</UButton>
    </div>
    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <UCard
        v-for="stat in stats"
        :key="stat.label"
        :ui="{ body: { padding: 'p-6' } }"
      >
        <div class="flex items-center justify-between">
          <div class="flex-1">
            <p class="text-sm font-medium text-gray-500 dark:text-gray-400 mb-1">
              {{ stat.label }}
            </p>
            <p class="text-3xl font-bold" :class="stat.color">
              {{ stat.value }}
            </p>
            <p v-if="stat.description" class="text-xs text-gray-400 mt-2">
              {{ stat.description }}
            </p>
          </div>
          <div
            class="flex items-center justify-center w-14 h-14 rounded-full"
            :class="stat.bgColor"
          >
            <UIcon :name="stat.icon" class="w-7 h-7" :class="stat.iconColor" />
          </div>
        </div>
      </UCard>
    </div>

    <!-- 借阅趋势图表 -->
    <div class="mt-8">
      <h2 class="text-xl font-bold mb-4">借阅趋势（最近7天）</h2>
      <UCard class="p-6">
        <div v-if="chartLoading" class="flex justify-center items-center" style="height: 400px;">
          <UIcon name="i-heroicons-arrow-path" class="w-8 h-8 animate-spin text-primary" />
        </div>
        <div v-else-if="chartError" class="flex flex-col justify-center items-center text-center" style="height: 400px;">
          <p class="text-red-500">{{ chartError }}</p>
          <UButton @click="loadBorrowTrend" class="mt-4" color="primary">重新加载</UButton>
        </div>
        <div v-else ref="chartRef" style="width: 100%; height: 400px;"></div>
      </UCard>
    </div>

    <!-- 快捷操作区域 -->
    <div class="mt-8">
      <h2 class="text-xl font-bold mb-4">快捷操作</h2>
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <UCard
          v-for="action in quickActions"
          :key="action.label"
          class="cursor-pointer hover:shadow-lg transition-shadow"
          @click="action.onClick"
        >
          <div class="flex items-center gap-4">
            <div
              class="flex items-center justify-center w-12 h-12 rounded-lg"
              :class="action.bgColor"
            >
              <UIcon :name="action.icon" class="w-6 h-6" :class="action.iconColor" />
            </div>
            <div>
              <p class="font-semibold">{{ action.label }}</p>
              <p class="text-xs text-gray-500">{{ action.description }}</p>
            </div>
          </div>
        </UCard>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, onBeforeUnmount, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import type { ECharts } from 'echarts'
import { getOverviewStatistics, getBorrowTrend, type DashboardOverviewVO, type BorrowTrendVO } from '@/api/dashboard'

const router = useRouter()
const loading = ref(false)
const error = ref('')
const data = ref<DashboardOverviewVO | null>(null)

// 图表相关
const chartRef = ref<HTMLElement>()
const chartInstance = ref<ECharts>()
const chartLoading = ref(false)
const chartError = ref('')
const chartData = ref<BorrowTrendVO | null>(null)

const stats = computed(() => {
  if (!data.value) {
    return []
  }
  return [
    {
      label: '总用户数',
      value: data.value.totalUsers.toLocaleString(),
      icon: 'i-heroicons-users',
      color: 'text-blue-600 dark:text-blue-400',
      bgColor: 'bg-blue-100 dark:bg-blue-900/30',
      iconColor: 'text-blue-600 dark:text-blue-400',
      description: '系统注册用户总数'
    },
    {
      label: '总图书数',
      value: data.value.totalBooks.toLocaleString(),
      icon: 'i-heroicons-book-open',
      color: 'text-green-600 dark:text-green-400',
      bgColor: 'bg-green-100 dark:bg-green-900/30',
      iconColor: 'text-green-600 dark:text-green-400',
      description: '图书馆藏书总量'
    },
    {
      label: '借阅中',
      value: data.value.borrowingCount.toLocaleString(),
      icon: 'i-heroicons-clipboard-document-list',
      color: 'text-orange-600 dark:text-orange-400',
      bgColor: 'bg-orange-100 dark:bg-orange-900/30',
      iconColor: 'text-orange-600 dark:text-orange-400',
      description: '当前借阅记录数'
    },
    {
      label: '今日新增',
      value: data.value.todayNewBorrows.toLocaleString(),
      icon: 'i-heroicons-arrow-trending-up',
      color: 'text-purple-600 dark:text-purple-400',
      bgColor: 'bg-purple-100 dark:bg-purple-900/30',
      iconColor: 'text-purple-600 dark:text-purple-400',
      description: '今日新增借阅数'
    }
  ]
})

const quickActions = [
  {
    label: '用户管理',
    description: '管理系统用户',
    icon: 'i-heroicons-user-group',
    bgColor: 'bg-blue-100 dark:bg-blue-900/30',
    iconColor: 'text-blue-600 dark:text-blue-400',
    onClick: () => router.push('/admin/users')
  },
  {
    label: '图书管理',
    description: '管理图书信息',
    icon: 'i-heroicons-book-open',
    bgColor: 'bg-green-100 dark:bg-green-900/30',
    iconColor: 'text-green-600 dark:text-green-400',
    onClick: () => router.push('/admin/books')
  },
  {
    label: '借阅管理',
    description: '查看借阅记录',
    icon: 'i-heroicons-clipboard-document-check',
    bgColor: 'bg-orange-100 dark:bg-orange-900/30',
    iconColor: 'text-orange-600 dark:text-orange-400',
    onClick: () => router.push('/admin/borrows')
  },
  {
    label: '分类管理',
    description: '管理图书分类',
    icon: 'i-heroicons-tag',
    bgColor: 'bg-purple-100 dark:bg-purple-900/30',
    iconColor: 'text-purple-600 dark:text-purple-400',
    onClick: () => router.push('/admin/categories')
  }
]

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

// 加载借阅趋势数据
const loadBorrowTrend = async () => {
  chartLoading.value = true
  chartError.value = ''
  try {
    const response = await getBorrowTrend()
    console.log('借阅趋势 API 响应：', response)
    if (response.code === 0) {
      chartData.value = response.data
      // 等待 DOM 更新完成
      await nextTick()
      // 再等待一小段时间确保元素完全渲染
      setTimeout(() => {
        initChart()
      }, 100)
    } else {
      chartError.value = response.message || '加载图表数据失败'
    }
  } catch (err: any) {
    console.error('加载图表数据失败：', err)
    chartError.value = err.message || '加载图表数据失败，请稍后重试'
  } finally {
    chartLoading.value = false
  }
}

// 初始化图表
const initChart = () => {
  if (!chartRef.value || !chartData.value) {
    console.log('图表初始化失败：缺少必要元素', {
      hasChartRef: !!chartRef.value,
      hasChartData: !!chartData.value
    })
    return
  }

  // 移除旧的 resize 监听器
  window.removeEventListener('resize', handleResize)

  // 如果图表实例已存在，先销毁
  if (chartInstance.value) {
    chartInstance.value.dispose()
  }

  // 创建图表实例
  chartInstance.value = echarts.init(chartRef.value)

  // 将字符串数组转换为数字数组（后端 Long 类型会被序列化为字符串）
  const borrowCounts = chartData.value.borrowCounts.map(count => Number(count))
  const returnCounts = chartData.value.returnCounts.map(count => Number(count))

  console.log('图表数据：', {
    dates: chartData.value.dates,
    borrowCounts,
    returnCounts
  })

  // 配置图表选项
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['借出', '归还'],
      top: 10
    },
    grid: {
      left: '50px',
      right: '20px',
      bottom: '30px',
      top: '50px',
      containLabel: false
    },
    xAxis: {
      type: 'category',
      data: chartData.value.dates,
      axisLabel: {
        rotate: 0,
        fontSize: 12
      }
    },
    yAxis: {
      type: 'value',
      minInterval: 1,
      axisLabel: {
        fontSize: 12
      }
    },
    series: [
      {
        name: '借出',
        type: 'bar',
        data: borrowCounts,
        itemStyle: {
          color: '#3b82f6'
        },
        barWidth: '30%'
      },
      {
        name: '归还',
        type: 'bar',
        data: returnCounts,
        itemStyle: {
          color: '#10b981'
        },
        barWidth: '30%'
      }
    ]
  }

  chartInstance.value.setOption(option)
  console.log('图表配置已设置')

  // 监听窗口大小变化，自动调整图表大小
  window.addEventListener('resize', handleResize)
}

// 处理窗口大小变化
const handleResize = () => {
  chartInstance.value?.resize()
}

// 组件卸载时清理
onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  if (chartInstance.value) {
    chartInstance.value.dispose()
  }
})

onMounted(() => {
  loadStatistics()
  loadBorrowTrend()
})
</script>
