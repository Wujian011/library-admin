<template>
  <div class="p-6 max-w-[1400px] mx-auto">
    <div class="flex flex-col md:flex-row md:items-center justify-between mb-8 gap-4">
      <div>
        <h1 class="text-2xl font-bold text-gray-800 tracking-tight">借阅管理</h1>
        <p class="text-gray-500 text-sm mt-1">管理系统内的所有借阅记录</p>
      </div>
      <button
        @click="handleAdd"
        class="flex items-center gap-2 px-5 py-2.5 bg-[#07C36E] text-white rounded-lg hover:bg-[#05a65d] transition-colors shadow-md hover:shadow-lg font-medium"
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M5 12h14"/><path d="M12 5v14"/></svg>
        新增借阅
      </button>
    </div>

    <div class="bg-white dark:bg-gray-800 rounded-xl shadow-sm border border-gray-100 dark:border-gray-700 overflow-hidden">

      <div class="p-5 border-b border-gray-100 dark:border-gray-700 bg-gray-50/50 dark:bg-gray-800/50 flex flex-wrap gap-3 items-center">
        <div class="relative group">
          <svg class="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 group-focus-within:text-[#07C36E] transition-colors" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
          <input
            v-model="searchForm.userId"
            placeholder="读者ID..."
            type="number"
            class="pl-10 pr-4 py-2 bg-white border border-gray-200 rounded-lg w-36 focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] transition-all text-sm"
          />
        </div>

        <div class="relative group">
          <svg class="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 group-focus-within:text-[#07C36E] transition-colors" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/></svg>
          <input
            v-model="searchForm.bookId"
            placeholder="图书ID..."
            type="number"
            class="pl-10 pr-4 py-2 bg-white border border-gray-200 rounded-lg w-36 focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] transition-all text-sm"
          />
        </div>

        <select
          v-model="searchForm.status"
          class="px-4 py-2 bg-white border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] appearance-none transition-all text-sm w-32"
        >
          <option :value="null">全部状态</option>
          <option :value="1">借出</option>
          <option :value="2">已还</option>
          <option :value="3">逾期</option>
          <option :value="4">丢失</option>
        </select>

        <div class="flex items-center gap-2 ml-auto">
          <button @click="handleSearch" class="px-4 py-2 bg-[#07C36E] text-white text-sm rounded-lg hover:bg-[#05a65d] transition-colors font-medium">搜索</button>
          <button @click="handleReset" class="px-4 py-2 bg-white border border-gray-200 text-gray-600 text-sm rounded-lg hover:bg-gray-50 hover:text-gray-900 transition-colors">重置</button>
        </div>
      </div>

      <div class="overflow-x-auto">
        <table class="w-full">
          <thead class="bg-gray-50 dark:bg-gray-700/50 border-b border-gray-100 dark:border-gray-700">
            <tr>
              <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">ID</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">读者信息</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">图书信息</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">借出日期</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">应还日期</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">归还日期</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">状态</th>
              <th class="px-6 py-4 text-right text-xs font-semibold text-gray-500 uppercase tracking-wider">操作</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100 dark:divide-gray-700">
            <tr v-if="loading">
              <td colspan="8" class="px-6 py-10 text-center">
                <div class="flex justify-center items-center space-x-2">
                  <div class="w-2 h-2 bg-[#07C36E] rounded-full animate-bounce"></div>
                  <div class="w-2 h-2 bg-[#07C36E] rounded-full animate-bounce delay-100"></div>
                  <div class="w-2 h-2 bg-[#07C36E] rounded-full animate-bounce delay-200"></div>
                </div>
              </td>
            </tr>
            <tr v-else-if="tableData.length === 0">
              <td colspan="8" class="px-6 py-16 text-center text-gray-400 flex flex-col items-center justify-center">
                <svg class="mb-2 text-gray-300" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1"><circle cx="12" cy="12" r="10"/><line x1="8" y1="12" x2="16" y2="12"/></svg>
                <span>暂无借阅记录</span>
              </td>
            </tr>
            <tr v-else v-for="row in tableData" :key="row.lendId" class="hover:bg-gray-50/80 transition-colors group">
              <td class="px-6 py-4 text-gray-500 text-sm font-mono">#{{ row.lendId }}</td>
              <td class="px-6 py-4">
                <div class="text-sm">
                  <div class="font-medium text-gray-900">{{ row.userName || '未知' }}</div>
                  <div v-if="row.cardNo" class="text-xs text-gray-400">证号: {{ row.cardNo }}</div>
                </div>
              </td>
              <td class="px-6 py-4">
                <div class="text-sm">
                  <div class="font-medium text-gray-900 max-w-[150px] truncate" :title="row.bookName">{{ row.bookName || '未知' }}</div>
                  <div v-if="row.isbn" class="text-xs text-gray-400">ISBN: {{ row.isbn }}</div>
                </div>
              </td>
              <td class="px-6 py-4 text-gray-600 text-sm whitespace-nowrap">{{ formatDate(row.lendDate) }}</td>
              <td class="px-6 py-4 text-sm whitespace-nowrap" :class="isOverdue(row) ? 'text-red-600 font-medium' : 'text-gray-600'">
                {{ formatDate(row.dueDate) }}
              </td>
              <td class="px-6 py-4 text-gray-600 text-sm whitespace-nowrap">{{ row.backDate ? formatDate(row.backDate) : '-' }}</td>
              <td class="px-6 py-4">
                <span
                  :class="getStatusClass(row.status)"
                  class="px-2 py-0.5 rounded text-xs font-medium">
                  {{ getStatusText(row.status) }}
                </span>
              </td>
              <td class="px-6 py-4 text-right">
                <div class="flex items-center justify-end gap-3 opacity-60 group-hover:opacity-100 transition-opacity">
                  <button v-if="row.status === 1" @click="handleReturn(row)" class="text-gray-500 hover:text-[#07C36E] transition-colors" title="还书">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="9 11 12 14 22 4"></polyline><path d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11"></path></svg>
                  </button>
                  <button @click="handleEdit(row)" class="text-gray-500 hover:text-[#07C36E] transition-colors" title="编辑">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 3a2.828 2.828 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5L17 3z"/></svg>
                  </button>
                  <div class="w-px h-4 bg-gray-300"></div>
                  <button @click="handleDelete(row)" class="text-gray-500 hover:text-red-500 transition-colors" title="删除">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 6h18"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/></svg>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="px-6 py-4 border-t border-gray-100 dark:border-gray-700 flex items-center justify-between bg-gray-50/30">
        <span class="text-sm text-gray-500">共 {{ pagination.total }} 条记录</span>
        <div class="flex items-center gap-2">
          <button
            @click="pagination.current--"
            :disabled="pagination.current === 1"
            class="px-3 py-1.5 border border-gray-200 rounded bg-white text-gray-600 text-sm hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
          >
            上一页
          </button>
          <span class="text-sm font-medium px-2 text-gray-700">{{ pagination.current }} / {{ Math.max(1, Math.ceil(pagination.total / pagination.pageSize)) }}</span>
          <button
            @click="pagination.current++"
            :disabled="pagination.current >= Math.ceil(pagination.total / pagination.pageSize)"
            class="px-3 py-1.5 border border-gray-200 rounded bg-white text-gray-600 text-sm hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
          >
            下一页
          </button>
        </div>
      </div>
    </div>

    <!-- 添加/编辑弹窗 -->
    <Transition
      enter-active-class="transition duration-200 ease-out"
      enter-from-class="opacity-0"
      enter-to-class="opacity-100"
      leave-active-class="transition duration-150 ease-in"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0"
    >
      <div v-if="modalOpen" class="fixed inset-0 bg-gray-900/40 backdrop-blur-sm flex items-center justify-center z-50" @click.self="modalOpen = false">
        <div class="bg-white dark:bg-gray-800 rounded-xl shadow-2xl w-full max-w-md overflow-hidden transform transition-all scale-100">
          <div class="px-6 py-4 border-b border-gray-100 dark:border-gray-700 flex justify-between items-center bg-gray-50/50">
            <h2 class="text-lg font-bold text-gray-800">{{ isEdit ? '编辑借阅' : '新增借阅' }}</h2>
            <button @click="modalOpen = false" class="text-gray-400 hover:text-gray-600">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
            </button>
          </div>

          <form @submit.prevent="handleSubmit" class="p-6 space-y-4">
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1.5">读者ID <span class="text-red-500">*</span></label>
                <input
                  v-model="formData.userId"
                  type="number"
                  class="w-full px-4 py-2.5 bg-white border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] transition-all text-sm"
                  required
                  :disabled="isEdit"
                  placeholder="输入读者ID"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1.5">图书ID <span class="text-red-500">*</span></label>
                <input
                  v-model="formData.bookId"
                  type="number"
                  class="w-full px-4 py-2.5 bg-white border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] transition-all text-sm"
                  required
                  :disabled="isEdit"
                  placeholder="输入图书ID"
                />
              </div>
            </div>

            <div v-if="!isEdit">
              <label class="block text-sm font-medium text-gray-700 mb-1.5">应还日期 <span class="text-red-500">*</span></label>
              <input
                v-model="formData.dueDate"
                type="date"
                class="w-full px-4 py-2.5 bg-white border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] transition-all text-sm"
                required
              />
            </div>

            <div v-if="isEdit">
              <label class="block text-sm font-medium text-gray-700 mb-1.5">借阅状态</label>
              <select v-model="formData.status" class="w-full px-4 py-2.5 bg-white border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] appearance-none transition-all text-sm">
                <option :value="1">借出</option>
                <option :value="2">已还</option>
                <option :value="3">逾期</option>
                <option :value="4">丢失</option>
              </select>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1.5">备注</label>
              <textarea
                v-model="formData.note"
                rows="2"
                class="w-full px-4 py-2.5 bg-white border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] transition-all text-sm resize-none"
                placeholder="请输入备注信息"
              ></textarea>
            </div>

            <div class="pt-4 flex justify-end gap-3">
              <button type="button" @click="modalOpen = false" class="px-5 py-2.5 bg-white border border-gray-200 text-gray-700 rounded-lg hover:bg-gray-50 font-medium transition-colors text-sm">取消</button>
              <button
                type="submit"
                :disabled="submitting"
                class="px-5 py-2.5 bg-[#07C36E] text-white rounded-lg hover:bg-[#05a65d] shadow-md shadow-green-500/20 disabled:opacity-50 disabled:cursor-not-allowed font-medium transition-all text-sm flex items-center"
              >
                <svg v-if="submitting" class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
                {{ submitting ? '提交中...' : '确定' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </Transition>

    <!-- 删除确认弹窗 -->
    <Transition
      enter-active-class="transition duration-200 ease-out"
      enter-from-class="opacity-0"
      enter-to-class="opacity-100"
      leave-active-class="transition duration-150 ease-in"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0"
    >
      <div v-if="deleteModalOpen" class="fixed inset-0 bg-gray-900/40 backdrop-blur-sm flex items-center justify-center z-50" @click.self="deleteModalOpen = false">
        <div class="bg-white dark:bg-gray-800 rounded-xl shadow-2xl w-full max-w-md overflow-hidden transform transition-all scale-100">
          <div class="p-6">
            <div class="flex items-center gap-4 mb-4">
              <div class="w-12 h-12 rounded-full bg-red-50 flex items-center justify-center flex-shrink-0">
                <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                </svg>
              </div>
              <div class="flex-1">
                <h3 class="text-lg font-bold text-gray-900 mb-1">确认删除记录</h3>
                <p class="text-sm text-gray-500">此操作无法撤销，请谨慎操作</p>
              </div>
            </div>

            <div v-if="deleteTarget" class="bg-gray-50 rounded-lg p-4 mb-6">
              <div class="text-sm">
                <div class="font-medium text-gray-900">{{ deleteTarget.bookName || '未知图书' }}</div>
                <div class="text-gray-500">借阅人: {{ deleteTarget.userName || '未知' }}</div>
              </div>
            </div>

            <div class="flex justify-end gap-3">
              <button
                type="button"
                @click="deleteModalOpen = false"
                :disabled="deleting"
                class="px-5 py-2.5 bg-white border border-gray-200 text-gray-700 rounded-lg hover:bg-gray-50 font-medium transition-colors text-sm disabled:opacity-50 disabled:cursor-not-allowed"
              >
                取消
              </button>
              <button
                type="button"
                @click="confirmDelete"
                :disabled="deleting"
                class="px-5 py-2.5 bg-red-600 text-white rounded-lg hover:bg-red-700 shadow-md shadow-red-500/20 disabled:opacity-50 disabled:cursor-not-allowed font-medium transition-all text-sm flex items-center"
              >
                <svg v-if="deleting" class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
                {{ deleting ? '删除中...' : '确认删除' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </Transition>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { listLendListByPage, deleteLendList, addLendList, updateLendList, returnBook } from '@/api/lendlist'
import dayjs from 'dayjs'

const toast = useToast()
const loading = ref(false)
const submitting = ref(false)
const modalOpen = ref(false)
const isEdit = ref(false)
const deleteModalOpen = ref(false)
const deleteTarget = ref<any>(null)
const deleting = ref(false)

// 格式化时间
const formatDate = (date: string | null | undefined) => {
  if (!date) return '-'
  return dayjs(date).format('YYYY-MM-DD')
}

// 判断是否逾期
const isOverdue = (row: any) => {
  if (row.status !== 1) return false
  return dayjs().isAfter(dayjs(row.dueDate))
}

// 获取状态文本
const getStatusText = (status: number) => {
  const statusMap: Record<number, string> = {
    1: '借出',
    2: '已还',
    3: '逾期',
    4: '丢失'
  }
  return statusMap[status] || '未知'
}

// 获取状态样式
const getStatusClass = (status: number) => {
  const classMap: Record<number, string> = {
    1: 'bg-blue-50 text-blue-600 border border-blue-100',
    2: 'bg-green-50 text-green-600 border border-green-100',
    3: 'bg-orange-50 text-orange-600 border border-orange-100',
    4: 'bg-red-50 text-red-600 border border-red-100'
  }
  return classMap[status] || 'bg-gray-50 text-gray-600 border border-gray-100'
}

const searchForm = reactive({
  userId: null as number | null,
  bookId: null as number | null,
  status: null as number | null
})

const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref<any[]>([])

const formData = reactive({
  lendId: null as number | null,
  userId: null as number | null,
  bookId: null as number | null,
  dueDate: '',
  status: 1,
  note: ''
})

const fetchData = async () => {
  loading.value = true
  try {
    const params: any = {
      current: pagination.current,
      pageSize: pagination.pageSize
    }
    if (searchForm.userId) params.userId = searchForm.userId
    if (searchForm.bookId) params.bookId = searchForm.bookId
    if (searchForm.status) params.status = searchForm.status

    const res = await listLendListByPage(params)
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error: any) {
    toast.add({ title: '获取数据失败', description: error.message, color: 'red' })
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.current = 1
  fetchData()
}

const handleReset = () => {
  searchForm.userId = null
  searchForm.bookId = null
  searchForm.status = null
  pagination.current = 1
  fetchData()
}

const handleAdd = () => {
  isEdit.value = false
  formData.lendId = null
  formData.userId = null
  formData.bookId = null
  formData.dueDate = dayjs().add(30, 'day').format('YYYY-MM-DD')
  formData.status = 1
  formData.note = ''
  modalOpen.value = true
}

const handleEdit = (row: any) => {
  isEdit.value = true
  formData.lendId = row.lendId
  formData.userId = row.userId
  formData.bookId = row.bookId
  formData.dueDate = row.dueDate ? dayjs(row.dueDate).format('YYYY-MM-DD') : ''
  formData.status = row.status
  formData.note = row.note || ''
  modalOpen.value = true
}

const handleSubmit = async () => {
  submitting.value = true
  try {
    if (isEdit.value) {
      await updateLendList({
        lendId: formData.lendId,
        status: formData.status,
        note: formData.note
      })
    } else {
      await addLendList({
        userId: formData.userId,
        bookId: formData.bookId,
        dueDate: formData.dueDate,
        note: formData.note
      })
    }

    toast.add({ title: '操作成功', color: 'green' })
    modalOpen.value = false
    fetchData()
  } catch (error: any) {
    toast.add({ title: '操作失败', description: error.message, color: 'red' })
  } finally {
    submitting.value = false
  }
}

const handleReturn = async (row: any) => {
  try {
    await returnBook(row.lendId)
    toast.add({ title: '还书成功', color: 'green' })
    fetchData()
  } catch (error: any) {
    toast.add({ title: '还书失败', description: error.message, color: 'red' })
  }
}

const handleDelete = (row: any) => {
  deleteTarget.value = row
  deleteModalOpen.value = true
}

const confirmDelete = async () => {
  if (!deleteTarget.value) return
  deleting.value = true
  try {
    await deleteLendList(deleteTarget.value.lendId as number)
    toast.add({ title: '删除成功', color: 'green' })
    deleteModalOpen.value = false

    if (tableData.value.length === 1 && pagination.current > 1) {
      pagination.current--
    } else {
      fetchData()
    }
  } catch (error: any) {
    toast.add({ title: '删除失败', description: error.message, color: 'red' })
  } finally {
    deleting.value = false
  }
}

watch(() => pagination.current, fetchData)

onMounted(fetchData)
</script>
