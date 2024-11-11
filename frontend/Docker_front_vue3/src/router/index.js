import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import MembersView from '../views/MembersView.vue'

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
      component: LoginView
    },
    {
      path: '/members',
      name: 'members',
      component: MembersView
    }
  ]
})

// 添加全局路由守卫
router.beforeEach((to, from, next) => {
  const userId = localStorage.getItem('userId')
  
  // 如果用户未登录且访问的不是登录页，则重定向到登录页
  if (!userId && to.path !== '/login') {
    next('/login')
  }
  // 如果用户已登录且访问登录页，则重定向到成员页面
  else if (userId && to.path === '/login') {
    next('/members')
  }
  else {
    next()
  }
})

export default router 