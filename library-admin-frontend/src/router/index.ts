import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import AdminLayout from '@/layouts/AdminLayout.vue'
import UserLayout from '@/layouts/UserLayout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/RegisterView.vue'),
      meta: { requiresAuth: false }
    },
    // 管理员路由
    {
      path: '/admin',
      component: AdminLayout,
      meta: { requiresAuth: true, requiresAdmin: true },
      children: [
        {
          path: '',
          redirect: '/admin/dashboard'
        },
        {
          path: 'dashboard',
          name: 'admin-dashboard',
          component: () => import('../views/admin/DashboardView.vue')
        },
        {
          path: 'users',
          name: 'admin-users',
          component: () => import('../views/admin/UsersView.vue')
        },
        {
          path: 'books',
          name: 'admin-books',
          component: () => import('../views/admin/BooksView.vue')
        },
        {
          path: 'categories',
          name: 'admin-categories',
          component: () => import('../views/admin/ClassInfoView.vue')
        },
        {
          path: 'borrows',
          name: 'admin-borrows',
          component: () => import('../views/admin/BorrowsView.vue')
        }
      ]
    },
    // 用户路由
    {
      path: '/user',
      component: UserLayout,
      meta: { requiresAuth: true, requiresUser: true },
      children: [
        {
          path: '',
          redirect: '/user/home'
        },
        {
          path: 'home',
          name: 'user-home',
          component: () => import('../views/user/HomeView.vue')
        },
        {
          path: 'books',
          name: 'user-books',
          component: () => import('../views/user/BooksView.vue')
        },
        {
          path: 'borrows',
          name: 'user-borrows',
          component: () => import('../views/user/BorrowsView.vue')
        },
        {
          path: 'profile',
          name: 'user-profile',
          component: () => import('../views/user/ProfileView.vue')
        }
      ]
    }
  ],
})

// 路由守卫：检查登录状态和权限
router.beforeEach((to, _from, next) => {
  const userStore = useUserStore()
  const isLoggedIn = userStore.isLoggedIn()
  const userType = userStore.userInfo?.userType

  // 未登录且需要认证
  if (to.meta.requiresAuth && !isLoggedIn) {
    next({ name: 'login' })
    return
  }

  // 已登录访问登录/注册页，根据角色跳转
  if ((to.name === 'login' || to.name === 'register') && isLoggedIn) {
    next(userType === 1 ? '/admin/dashboard' : '/user/home')
    return
  }

  // 检查管理员权限
  if (to.meta.requiresAdmin && userType !== 1) {
    next('/user/home')
    return
  }

  // 检查用户权限
  if (to.meta.requiresUser && userType !== 2) {
    next('/admin/dashboard')
    return
  }

  next()
})

export default router
