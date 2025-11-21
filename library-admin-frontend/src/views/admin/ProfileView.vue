<template>
  <div class="max-w-4xl mx-auto">
    <h1 class="text-2xl font-bold mb-6">个人中心</h1>

    <!-- 个人信息卡片 -->
    <UCard class="mb-6">
      <template #header>
        <div class="flex items-center justify-between">
          <h2 class="text-lg font-semibold">个人信息</h2>
          <UButton
            v-if="!isEditing"
            @click="startEdit"
            color="primary"
            variant="soft"
            icon="i-heroicons-pencil"
          >
            编辑资料
          </UButton>
          <div v-else class="flex gap-2">
            <UButton @click="cancelEdit" color="gray" variant="soft">
              取消
            </UButton>
            <UButton @click="saveProfile" color="primary" :loading="saving">
              保存
            </UButton>
          </div>
        </div>
      </template>

      <div v-if="loading" class="flex justify-center py-12">
        <UIcon name="i-heroicons-arrow-path" class="w-8 h-8 animate-spin text-primary" />
      </div>

      <div v-else class="space-y-4">
        <!-- 用户名（不可编辑） -->
        <div class="grid grid-cols-3 gap-4">
          <label class="text-sm font-medium text-gray-700 dark:text-gray-300 flex items-center">
            登录账号
          </label>
          <div class="col-span-2">
            <input
              :value="userInfo?.username"
              disabled
              class="w-full px-4 py-2 bg-gray-100 dark:bg-gray-700 border border-gray-200 dark:border-gray-600 rounded-lg text-gray-500 dark:text-gray-400"
            />
          </div>
        </div>

        <!-- 真实姓名 -->
        <div class="grid grid-cols-3 gap-4">
          <label class="text-sm font-medium text-gray-700 dark:text-gray-300 flex items-center">
            真实姓名
          </label>
          <div class="col-span-2">
            <input
              v-model="formData.name"
              :disabled="!isEditing"
              class="w-full px-4 py-2 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-600 rounded-lg disabled:bg-gray-100 disabled:text-gray-500 dark:disabled:bg-gray-700"
            />
          </div>
        </div>

        <!-- 借阅证号（仅读者） -->
        <div v-if="userInfo?.userType === 2" class="grid grid-cols-3 gap-4">
          <label class="text-sm font-medium text-gray-700 dark:text-gray-300 flex items-center">
            借阅证号
          </label>
          <div class="col-span-2">
            <input
              v-model="formData.cardNo"
              :disabled="!isEditing"
              class="w-full px-4 py-2 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-600 rounded-lg disabled:bg-gray-100 disabled:text-gray-500 dark:disabled:bg-gray-700"
            />
          </div>
        </div>

        <!-- 性别 -->
        <div class="grid grid-cols-3 gap-4">
          <label class="text-sm font-medium text-gray-700 dark:text-gray-300 flex items-center">
            性别
          </label>
          <div class="col-span-2">
            <select
              v-model="formData.sex"
              :disabled="!isEditing"
              class="w-full px-4 py-2 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-600 rounded-lg disabled:bg-gray-100 disabled:text-gray-500 dark:disabled:bg-gray-700"
            >
              <option :value="0">未知</option>
              <option :value="1">男</option>
              <option :value="2">女</option>
            </select>
          </div>
        </div>

        <!-- 出生日期 -->
        <div class="grid grid-cols-3 gap-4">
          <label class="text-sm font-medium text-gray-700 dark:text-gray-300 flex items-center">
            出生日期
          </label>
          <div class="col-span-2">
            <input
              v-model="formData.birth"
              type="date"
              :disabled="!isEditing"
              class="w-full px-4 py-2 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-600 rounded-lg disabled:bg-gray-100 disabled:text-gray-500 dark:disabled:bg-gray-700"
            />
          </div>
        </div>

        <!-- 联系电话 -->
        <div class="grid grid-cols-3 gap-4">
          <label class="text-sm font-medium text-gray-700 dark:text-gray-300 flex items-center">
            联系电话
          </label>
          <div class="col-span-2">
            <input
              v-model="formData.phone"
              :disabled="!isEditing"
              class="w-full px-4 py-2 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-600 rounded-lg disabled:bg-gray-100 disabled:text-gray-500 dark:disabled:bg-gray-700"
            />
          </div>
        </div>

        <!-- 联系地址 -->
        <div class="grid grid-cols-3 gap-4">
          <label class="text-sm font-medium text-gray-700 dark:text-gray-300 flex items-center">
            联系地址
          </label>
          <div class="col-span-2">
            <textarea
              v-model="formData.address"
              :disabled="!isEditing"
              rows="3"
              class="w-full px-4 py-2 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-600 rounded-lg disabled:bg-gray-100 disabled:text-gray-500 dark:disabled:bg-gray-700"
            />
          </div>
        </div>
      </div>
    </UCard>

    <!-- 修改密码卡片 -->
    <UCard>
      <template #header>
        <h2 class="text-lg font-semibold">修改密码</h2>
      </template>

      <div class="space-y-4">
        <div class="grid grid-cols-3 gap-4">
          <label class="text-sm font-medium text-gray-700 dark:text-gray-300 flex items-center">
            旧密码
          </label>
          <div class="col-span-2">
            <input
              v-model="passwordForm.oldPassword"
              type="password"
              placeholder="请输入旧密码"
              class="w-full px-4 py-2 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-600 rounded-lg"
            />
          </div>
        </div>

        <div class="grid grid-cols-3 gap-4">
          <label class="text-sm font-medium text-gray-700 dark:text-gray-300 flex items-center">
            新密码
          </label>
          <div class="col-span-2">
            <input
              v-model="passwordForm.newPassword"
              type="password"
              placeholder="请输入新密码（至少6个字符）"
              class="w-full px-4 py-2 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-600 rounded-lg"
            />
          </div>
        </div>

        <div class="grid grid-cols-3 gap-4">
          <label class="text-sm font-medium text-gray-700 dark:text-gray-300 flex items-center">
            确认密码
          </label>
          <div class="col-span-2">
            <input
              v-model="passwordForm.confirmPassword"
              type="password"
              placeholder="请再次输入新密码"
              class="w-full px-4 py-2 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-600 rounded-lg"
            />
          </div>
        </div>

        <div class="flex justify-end">
          <UButton @click="changePassword" color="primary" :loading="changingPassword">
            修改密码
          </UButton>
        </div>
      </div>
    </UCard>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getLoginUser, updateUser } from '@/api/user'
import { useUserStore } from '@/stores/user'
import dayjs from 'dayjs'

const userStore = useUserStore()
const toast = useToast()

const loading = ref(false)
const isEditing = ref(false)
const saving = ref(false)
const changingPassword = ref(false)

const userInfo = ref<any>(null)
const formData = ref({
  userId: 0,
  name: '',
  cardNo: '',
  sex: 0,
  birth: '',
  phone: '',
  address: ''
})

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 加载用户信息
const loadUserInfo = async () => {
  loading.value = true
  try {
    const response = await getLoginUser()
    if (response.code === 0) {
      userInfo.value = response.data
      // 填充表单数据
      formData.value = {
        userId: response.data.userId,
        name: response.data.name || '',
        cardNo: response.data.cardNo || '',
        sex: response.data.sex || 0,
        birth: response.data.birth ? dayjs(response.data.birth).format('YYYY-MM-DD') : '',
        phone: response.data.phone || '',
        address: response.data.address || ''
      }
    } else {
      toast.add({
        title: '加载失败',
        description: response.message,
        color: 'red'
      })
    }
  } catch (error: any) {
    toast.add({
      title: '加载失败',
      description: error.message || '获取用户信息失败',
      color: 'red'
    })
  } finally {
    loading.value = false
  }
}

// 开始编辑
const startEdit = () => {
  isEditing.value = true
}

// 取消编辑
const cancelEdit = () => {
  isEditing.value = false
  // 恢复原始数据
  if (userInfo.value) {
    formData.value = {
      userId: userInfo.value.userId,
      name: userInfo.value.name || '',
      cardNo: userInfo.value.cardNo || '',
      sex: userInfo.value.sex || 0,
      birth: userInfo.value.birth ? dayjs(userInfo.value.birth).format('YYYY-MM-DD') : '',
      phone: userInfo.value.phone || '',
      address: userInfo.value.address || ''
    }
  }
}

// 保存个人信息
const saveProfile = async () => {
  saving.value = true
  try {
    const response = await updateUser(formData.value)
    if (response.code === 0) {
      toast.add({
        title: '保存成功',
        color: 'green'
      })
      isEditing.value = false
      // 重新加载用户信息
      await loadUserInfo()
    } else {
      toast.add({
        title: '保存失败',
        description: response.message,
        color: 'red'
      })
    }
  } catch (error: any) {
    toast.add({
      title: '保存失败',
      description: error.message || '更新用户信息失败',
      color: 'red'
    })
  } finally {
    saving.value = false
  }
}

// 修改密码
const changePassword = async () => {
  // 验证表单
  if (!passwordForm.value.oldPassword) {
    toast.add({
      title: '请输入旧密码',
      color: 'red'
    })
    return
  }
  if (!passwordForm.value.newPassword) {
    toast.add({
      title: '请输入新密码',
      color: 'red'
    })
    return
  }
  if (passwordForm.value.newPassword.length < 6) {
    toast.add({
      title: '新密码至少6个字符',
      color: 'red'
    })
    return
  }
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    toast.add({
      title: '两次输入的密码不一致',
      color: 'red'
    })
    return
  }

  changingPassword.value = true
  try {
    // 这里需要调用修改密码的接口
    // 暂时使用 updateUser 接口，实际应该有专门的修改密码接口
    toast.add({
      title: '修改密码功能开发中',
      description: '请联系管理员修改密码',
      color: 'yellow'
    })
    // 清空表单
    passwordForm.value = {
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    }
  } catch (error: any) {
    toast.add({
      title: '修改失败',
      description: error.message || '修改密码失败',
      color: 'red'
    })
  } finally {
    changingPassword.value = false
  }
}

onMounted(() => {
  loadUserInfo()
})
</script>
