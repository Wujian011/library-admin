<template>
  <div class="min-h-screen flex bg-white dark:bg-gray-900">
    <!-- Left Side - Hero Section -->
    <div class="hidden lg:flex lg:w-1/2 relative bg-gray-900">
      <div class="absolute inset-0 bg-cover bg-center" style="background-image: url('https://images.unsplash.com/photo-1481627834876-b7833e8f5570?q=80&w=2070&auto=format&fit=crop')"></div>
      <div class="absolute inset-0 bg-black/40 backdrop-blur-[2px]"></div>

      <div class="relative z-10 flex flex-col justify-between w-full p-12 text-white">
        <div class="flex items-center gap-3">
          <UIcon name="i-heroicons-book-open" class="w-8 h-8 text-primary-400" />
          <span class="text-xl font-bold tracking-wide">Library Admin</span>
        </div>

        <div class="space-y-6 max-w-lg">
          <h1 class="text-5xl font-bold leading-tight">
            开启您的<br/>
            <span class="text-primary-400">知识之旅</span>
          </h1>
          <p class="text-lg text-gray-200 leading-relaxed">
            加入我们的图书管理系统，体验高效便捷的图书管理服务，让阅读变得更加简单美好。
          </p>
        </div>

        <div class="flex items-center gap-4 text-sm text-gray-300">
          <span>© 2025 Library Admin System</span>
          <span class="w-1 h-1 bg-gray-500 rounded-full"></span>
          <span>Privacy Policy</span>
        </div>
      </div>
    </div>

    <!-- Right Side - Register Form -->
    <div class="flex-1 flex items-center justify-center p-4 sm:p-12">
      <div class="w-full max-w-md space-y-8">
        <div class="text-center lg:text-left">
          <h2 class="text-3xl font-bold text-gray-900 dark:text-white tracking-tight">创建新账户</h2>
          <p class="mt-2 text-gray-500 dark:text-gray-400">
            填写以下信息完成注册
          </p>
        </div>

        <UForm :state="state" :schema="schema" @submit="onSubmit" class="space-y-5">
          <div class="space-y-2">
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">账号</label>
            <UInput
              v-model="state.userAccount"
              name="userAccount"
              placeholder="4-16个字符，支持字母、数字、下划线"
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
              placeholder="6-20个字符"
              size="xl"
              icon="i-heroicons-lock-closed"
              class="w-full"
            />
          </div>

          <div class="space-y-2">
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">确认密码</label>
            <UInput
              v-model="state.checkPassword"
              name="checkPassword"
              type="password"
              placeholder="请再次输入密码"
              size="xl"
              icon="i-heroicons-lock-closed"
              class="w-full"
            />
          </div>

          <UFormGroup name="agreement">
            <UCheckbox v-model="state.agreement">
              <template #label>
                <span class="text-sm text-gray-600 dark:text-gray-400">
                  我已阅读并同意
                  <UButton variant="link" size="sm" :padded="false" color="primary" class="font-medium">
                    用户协议
                  </UButton>
                  和
                  <UButton variant="link" size="sm" :padded="false" color="primary" class="font-medium">
                    隐私政策
                  </UButton>
                </span>
              </template>
            </UCheckbox>
          </UFormGroup>

          <UButton
            type="submit"
            block
            size="xl"
            color="primary"
            :loading="loading"
            :disabled="loading || !state.agreement"
            class="font-bold shadow-lg shadow-primary-500/20 transition-all hover:shadow-primary-500/40"
          >
            注册
          </UButton>
        </UForm>

        <div class="relative">
          <div class="absolute inset-0 flex items-center">
            <div class="w-full border-t border-gray-200 dark:border-gray-700"></div>
          </div>
          <div class="relative flex justify-center text-sm">
            <span class="px-2 bg-white dark:bg-gray-900 text-gray-500">
              已有账户？
            </span>
          </div>
        </div>

        <div class="text-center">
          <UButton
            variant="ghost"
            color="gray"
            block
            size="lg"
            @click="$router.push('/login')"
            class="font-medium hover:bg-gray-50 dark:hover:bg-gray-800"
          >
            返回登录
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
  userAccount: z.string()
    .min(4, '账号至少4个字符')
    .max(16, '账号最多16个字符')
    .regex(/^[a-zA-Z0-9_]+$/, '账号只能包含字母、数字和下划线'),
  userPassword: z.string()
    .min(6, '密码至少6个字符')
    .max(20, '密码最多20个字符'),
  checkPassword: z.string()
    .min(6, '密码至少6个字符')
}).refine((data) => data.userPassword === data.checkPassword, {
  message: '两次输入的密码不一致',
  path: ['checkPassword']
})

const state = reactive({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
  agreement: false
})

const onSubmit = async () => {
  if (!state.agreement) {
    toast.add({
      title: '请先同意用户协议',
      color: 'orange',
      icon: 'i-heroicons-exclamation-triangle'
    })
    return
  }

  loading.value = true
  try {
    const result = await userStore.register(
      state.userAccount,
      state.userPassword,
      state.checkPassword
    )

    if (result.code === 0) {
      toast.add({
        title: '注册成功',
        description: '即将跳转到登录页面',
        color: 'green',
        icon: 'i-heroicons-check-circle'
      })

      setTimeout(() => {
        router.push('/login')
      }, 1500)
    } else {
      throw new Error(result.message || '注册失败')
    }
  } catch (error: any) {
    toast.add({
      title: '注册失败',
      description: error.message || '请稍后重试',
      color: 'red',
      icon: 'i-heroicons-x-circle'
    })
  } finally {
    loading.value = false
  }
}
</script>
