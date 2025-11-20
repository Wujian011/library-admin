<template>
  <div class="p-6 max-w-[1400px] mx-auto">
    <div class="flex flex-col md:flex-row md:items-center justify-between mb-8 gap-4">
      <div>
        <h1 class="text-2xl font-bold text-gray-800 tracking-tight">用户管理</h1>
        <p class="text-gray-500 text-sm mt-1">管理系统内的所有账号与权限</p>
      </div>
      <button 
        @click="handleAdd" 
        class="flex items-center gap-2 px-5 py-2.5 bg-[#07C36E] text-white rounded-lg hover:bg-[#05a65d] transition-colors shadow-md hover:shadow-lg font-medium"
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M5 12h14"/><path d="M12 5v14"/></svg>
        添加用户
      </button>
    </div>

    <div class="bg-white dark:bg-gray-800 rounded-xl shadow-sm border border-gray-100 dark:border-gray-700 overflow-hidden">
      
      <div class="p-5 border-b border-gray-100 dark:border-gray-700 bg-gray-50/50 dark:bg-gray-800/50 flex flex-wrap gap-3 items-center">
        <div class="relative group">
          <svg class="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 group-focus-within:text-[#07C36E] transition-colors" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>
          <input
            v-model="searchForm.username"
            placeholder="搜索登录账号..."
            class="pl-10 pr-4 py-2 bg-white border border-gray-200 rounded-lg w-56 focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] transition-all text-sm"
          />
        </div>

        <div class="relative group">
          <svg class="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 group-focus-within:text-[#07C36E] transition-colors" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
          <input
            v-model="searchForm.name"
            placeholder="搜索真实姓名..."
            class="pl-10 pr-4 py-2 bg-white border border-gray-200 rounded-lg w-56 focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] transition-all text-sm"
          />
        </div>

        <div class="relative group">
          <svg class="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 group-focus-within:text-[#07C36E] transition-colors" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/></svg>
          <input
            v-model="searchForm.phone"
            placeholder="搜索联系电话..."
            class="pl-10 pr-4 py-2 bg-white border border-gray-200 rounded-lg w-56 focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] transition-all text-sm"
          />
        </div>

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
              <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">真实姓名</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">登录账号</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">用户类型</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">联系方式</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">状态</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">创建时间</th>
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
                <span>暂无用户数据</span>
              </td>
            </tr>
            <tr v-else v-for="row in tableData" :key="row.userId" class="hover:bg-gray-50/80 transition-colors group">
              <td class="px-6 py-4 text-gray-500 text-sm font-mono">#{{ row.userId }}</td>
              <td class="px-6 py-4">
                <div class="flex items-center gap-3">
                  <div class="w-9 h-9 rounded-full bg-gradient-to-br from-gray-100 to-gray-200 flex items-center justify-center text-[#07C36E] font-bold text-sm border border-white shadow-sm">
                    {{ row.name ? row.name.charAt(0).toUpperCase() : 'U' }}
                  </div>
                  <div>
                    <div class="font-medium text-gray-900">{{ row.name || '未命名' }}</div>
                    <div v-if="row.sex" class="text-xs text-gray-400">{{ row.sex === 1 ? '男' : row.sex === 2 ? '女' : '' }}</div>
                  </div>
                </div>
              </td>
              <td class="px-6 py-4 text-gray-600 text-sm">{{ row.username }}</td>
              <td class="px-6 py-4">
                <span
                  :class="row.userType === 1
                    ? 'bg-[#07C36E]/10 text-[#07C36E] border border-[#07C36E]/20'
                    : 'bg-blue-50 text-blue-600 border border-blue-100'"
                  class="px-2.5 py-1 rounded-full text-xs font-medium inline-flex items-center gap-1">
                   <span class="w-1.5 h-1.5 rounded-full" :class="row.userType === 1 ? 'bg-[#07C36E]' : 'bg-blue-500'"></span>
                  {{ row.userType === 1 ? '管理员' : '读者' }}
                </span>
              </td>
              <td class="px-6 py-4">
                <div class="text-sm text-gray-600">
                  <div v-if="row.phone" class="flex items-center gap-1">
                    <svg class="w-3 h-3 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/></svg>
                    {{ row.phone }}
                  </div>
                  <div v-if="row.cardNo" class="text-xs text-gray-400 mt-0.5">证号: {{ row.cardNo }}</div>
                </div>
              </td>
              <td class="px-6 py-4">
                <span
                  :class="row.status === 1
                    ? 'bg-green-50 text-green-600 border border-green-100'
                    : 'bg-red-50 text-red-600 border border-red-100'"
                  class="px-2 py-0.5 rounded text-xs font-medium">
                  {{ row.status === 1 ? '启用' : '禁用' }}
                </span>
              </td>
              <td class="px-6 py-4 text-gray-500 text-sm whitespace-nowrap">{{ formatDate(row.createTime) }}</td>
              <td class="px-6 py-4 text-right">
                <div class="flex items-center justify-end gap-3 opacity-60 group-hover:opacity-100 transition-opacity">
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
            <h2 class="text-lg font-bold text-gray-800">{{ isEdit ? '编辑用户' : '添加用户' }}</h2>
            <button @click="modalOpen = false" class="text-gray-400 hover:text-gray-600">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
            </button>
          </div>
          
          <form @submit.prevent="handleSubmit" class="p-6 space-y-4 max-h-[70vh] overflow-y-auto">
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1.5">用户类型 <span class="text-red-500">*</span></label>
                <select v-model="formData.userType" class="w-full px-4 py-2.5 bg-white border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] appearance-none transition-all text-sm" required>
                  <option :value="1">管理员</option>
                  <option :value="2">读者</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1.5">账号状态 <span class="text-red-500">*</span></label>
                <select v-model="formData.status" class="w-full px-4 py-2.5 bg-white border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] appearance-none transition-all text-sm" required>
                  <option :value="1">启用</option>
                  <option :value="0">禁用</option>
                </select>
              </div>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1.5">登录用户名 <span class="text-red-500">*</span></label>
              <input
                v-model="formData.username"
                :disabled="isEdit"
                class="w-full px-4 py-2.5 bg-white border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] transition-all disabled:bg-gray-100 disabled:text-gray-500 text-sm"
                required
                placeholder="请输入登录用户名"
              />
            </div>

            <div v-if="!isEdit">
              <label class="block text-sm font-medium text-gray-700 mb-1.5">密码 <span class="text-red-500">*</span></label>
              <input
                v-model="formData.password"
                type="password"
                class="w-full px-4 py-2.5 bg-white border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] transition-all text-sm"
                required
                placeholder="请输入密码"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1.5">真实姓名 <span class="text-red-500">*</span></label>
              <input
                v-model="formData.name"
                class="w-full px-4 py-2.5 bg-white border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] transition-all text-sm"
                required
                placeholder="请输入真实姓名"
              />
            </div>

            <div v-if="formData.userType === 2">
              <label class="block text-sm font-medium text-gray-700 mb-1.5">借阅证号</label>
              <input
                v-model="formData.cardNo"
                class="w-full px-4 py-2.5 bg-white border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] transition-all text-sm"
                placeholder="请输入借阅证号（仅读者需要）"
              />
            </div>

            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1.5">性别</label>
                <select v-model="formData.sex" class="w-full px-4 py-2.5 bg-white border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] appearance-none transition-all text-sm">
                  <option :value="0">未知</option>
                  <option :value="1">男</option>
                  <option :value="2">女</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1.5">出生日期</label>
                <input
                  v-model="formData.birth"
                  type="date"
                  class="w-full px-4 py-2.5 bg-white border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] transition-all text-sm"
                />
              </div>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1.5">联系电话</label>
              <input
                v-model="formData.phone"
                class="w-full px-4 py-2.5 bg-white border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] transition-all text-sm"
                placeholder="请输入联系电话"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1.5">联系地址</label>
              <textarea
                v-model="formData.address"
                rows="2"
                class="w-full px-4 py-2.5 bg-white border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#07C36E]/20 focus:border-[#07C36E] transition-all text-sm resize-none"
                placeholder="请输入联系地址"
              ></textarea>
            </div>

            <div class="pt-4 flex justify-end gap-3 sticky bottom-0 bg-white border-t border-gray-100 -mx-6 px-6 -mb-6 pb-6">
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
                <h3 class="text-lg font-bold text-gray-900 mb-1">确认删除用户</h3>
                <p class="text-sm text-gray-500">此操作无法撤销，请谨慎操作</p>
              </div>
            </div>

            <div v-if="deleteTarget" class="bg-gray-50 rounded-lg p-4 mb-6">
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 rounded-full bg-gradient-to-br from-gray-100 to-gray-200 flex items-center justify-center text-[#07C36E] font-bold text-sm border border-white shadow-sm">
                  {{ deleteTarget.name ? deleteTarget.name.charAt(0).toUpperCase() : 'U' }}
                </div>
                <div>
                  <div class="font-medium text-gray-900">{{ deleteTarget.name || '未命名' }}</div>
                  <div class="text-sm text-gray-500">{{ deleteTarget.username }}</div>
                </div>
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
import { listUserByPage, deleteUser, addUser, updateUser } from '@/api/user'
import dayjs from 'dayjs'

// 假设 useToast 是全局可用的，或者从你的 UI 库引入
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
  return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
}

const searchForm = reactive({
  username: '',
  name: '',
  phone: ''
})

const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref<any[]>([])

const formData = reactive({
  userId: null,
  userType: 1,
  username: '',
  password: '',
  name: '',
  cardNo: '',
  sex: 0,
  birth: '',
  address: '',
  phone: '',
  status: 1
})

const fetchData = async () => {
  loading.value = true
  try {
    // 模拟延迟，让 Loading 效果展示出来（实际使用时请去掉）
    // await new Promise(resolve => setTimeout(resolve, 300)) 
    
    const res = await listUserByPage({
      current: pagination.current,
      pageSize: pagination.pageSize,
      ...searchForm
    })
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
  searchForm.username = ''
  searchForm.name = ''
  searchForm.phone = ''
  pagination.current = 1
  fetchData()
}

const handleAdd = () => {
  isEdit.value = false
  formData.userId = null
  formData.userType = 1
  formData.username = ''
  formData.password = ''
  formData.name = ''
  formData.cardNo = ''
  formData.sex = 0
  formData.birth = ''
  formData.address = ''
  formData.phone = ''
  formData.status = 1
  modalOpen.value = true
}

const handleEdit = (row: any) => {
  isEdit.value = true
  formData.userId = row.userId
  formData.userType = row.userType
  formData.username = row.username
  formData.password = ''
  formData.name = row.name
  formData.cardNo = row.cardNo || ''
  formData.sex = row.sex || 0
  formData.birth = row.birth || ''
  formData.address = row.address || ''
  formData.phone = row.phone || ''
  formData.status = row.status
  modalOpen.value = true
}

const handleSubmit = async () => {
  submitting.value = true
  try {
    // 准备提交数据，处理空字符串字段
    const submitData: any = { ...formData }

    // 如果借阅证号为空字符串，设置为 null
    if (!submitData.cardNo || submitData.cardNo.trim() === '') {
      submitData.cardNo = null
    }

    // 如果出生日期为空字符串，设置为 null
    if (!submitData.birth || submitData.birth.trim() === '') {
      submitData.birth = null
    }

    if (isEdit.value) {
      await updateUser(submitData)
    } else {
      await addUser(submitData)
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

const handleDelete = (row: any) => {
  deleteTarget.value = row
  deleteModalOpen.value = true
}

const confirmDelete = async () => {
  if (!deleteTarget.value) return
  deleting.value = true
  try {
    await deleteUser(deleteTarget.value.userId)
    toast.add({ title: '删除成功', color: 'green' })
    deleteModalOpen.value = false

    // 如果删除的是当前页最后一条数据且不是第一页，向前翻页
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