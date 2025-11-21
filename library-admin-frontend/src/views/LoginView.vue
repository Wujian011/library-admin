<template>
  <div class="min-h-screen flex bg-white dark:bg-gray-900">
    <!-- Left Side - Hero Section -->
    <div class="hidden lg:flex lg:w-1/2 relative bg-gray-900">
      <div class="absolute inset-0 bg-cover bg-center" style="background-image: url('https://images.unsplash.com/photo-1507842217121-9e9628376272?q=80&w=2070&auto=format&fit=crop')"></div>
      <div class="absolute inset-0 bg-black/40 backdrop-blur-[2px]"></div>
      
      <div class="relative z-10 flex flex-col justify-between w-full p-12 text-white">
        <div class="flex items-center gap-3">
          <!-- Logo placeholder if needed -->
          <UIcon name="i-heroicons-book-open" class="w-8 h-8 text-primary-400" />
          <span class="text-xl font-bold tracking-wide">Library Admin</span>
        </div>
        
        <div class="space-y-6 max-w-lg">
          <h1 class="text-5xl font-bold leading-tight">
            探索知识的<br/>
            <span class="text-primary-400">无限可能</span>
          </h1>
          <p class="text-lg text-gray-200 leading-relaxed">
            高效、智能的图书管理系统，助您轻松管理馆藏资源，为读者提供更优质的服务体验。
          </p>
        </div>

        <div class="flex items-center gap-4 text-sm text-gray-300">
          <span>© 2025 Library Admin System</span>
          <span class="w-1 h-1 bg-gray-500 rounded-full"></span>
          <span>Privacy Policy</span>
        </div>
      </div>
    </div>

    <!-- Right Side - Login Form -->
    <div class="flex-1 flex items-center justify-center p-4 sm:p-12">
      <div class="w-full max-w-md space-y-8">
        <div class="text-center lg:text-left">
          <h2 class="text-3xl font-bold text-gray-900 dark:text-white tracking-tight">欢迎回来</h2>
          <p class="mt-2 text-gray-500 dark:text-gray-400">
            请输入您的账号信息以继续
          </p>
        </div>

        <UForm :state="state" :schema="schema" @submit="onSubmit" class="space-y-6">
          <div class="space-y-2">
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">账号</label>
            <UInput
              v-model="state.userAccount"
              name="userAccount"
              placeholder="请输入您的账号"
              size="xl"
              icon="i-heroicons-user"
              class="w-full"
            />
          </div>

          <div class="space-y-2">
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">密码</label>
            <UInput
              v-model="state.userPassword"
              name="userPassword"
              type="password"
              placeholder="请输入您的密码"
              size="xl"
              icon="i-heroicons-lock-closed"
              class="w-full"
            />
          </div>

          <div class="flex items-center justify-between">
            <UCheckbox 
              v-model="state.rememberMe" 
              label="记住我" 
              :ui="{ label: 'text-sm text-gray-500 dark:text-gray-400' }"
            />
            <UButton
              variant="link"
              size="sm"
              :padded="false"
              color="primary"
              class="text-sm font-medium hover:text-primary-600 dark:hover:text-primary-400"
            >
              忘记密码？
            </UButton>
          </div>

          <UButton
            type="submit"
            block
            size="xl"
            color="primary"
            :loading="loading"
            :disabled="loading"
            class="font-bold shadow-lg shadow-primary-500/20 transition-all hover:shadow-primary-500/40"
          >
            登录
          </UButton>
        </UForm>

        <div class="relative">
          <div class="absolute inset-0 flex items-center">
            <div class="w-full border-t border-gray-200 dark:border-gray-700"></div>
          </div>
          <div class="relative flex justify-center text-sm">
            <span class="px-2 bg-white dark:bg-gray-900 text-gray-500">
              还没有账户？
            </span>
          </div>
        </div>

        <div class="text-center">
          <UButton
            variant="ghost"
            color="gray"
            block
            size="lg"
            @click="$router.push('/register')"
            class="font-medium hover:bg-gray-50 dark:hover:bg-gray-800"
          >
            注册新账户
          </UButton>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { z } from 'zod'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const toast = useToast()

const schema = z.object({
  userAccount: z.string().min(4, '账号至少4个字符').max(16, '账号最多16个字符'),
  userPassword: z.string().min(6, '密码至少6个字符').max(20, '密码最多20个字符')
})

const state = reactive({
  userAccount: '',
  userPassword: '',
  rememberMe: false
})

const onSubmit = async () => {
  loading.value = true
  try {
    await userStore.login(state.userAccount, state.userPassword)

    toast.add({
      title: '登录成功',
      description: `欢迎回来，${state.userAccount}！`,
      color: 'green',
      icon: 'i-heroicons-check-circle'
    })

    // 根据用户类型跳转到对应的首页
    const userType = userStore.userInfo?.userType
    if (userType === 1) {
      router.push('/admin/dashboard')
    } else {
      router.push('/user/home')
    }
  } catch (error: any) {
    toast.add({
      title: '登录失败',
      description: error.message || '账号或密码错误',
      color: 'red',
      icon: 'i-heroicons-x-circle'
    })
  } finally {
    loading.value = false
  }
}
</script>
