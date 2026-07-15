import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/HomeView.vue')
    },
    {
      path: '/useredit', // useredit のURLでUserEditFormコンポーネントを表示
      name: 'useredit',
      component: () => import('@/views/UserEditForm.vue')                                                                                      
    },
    {
      path: '/usersearch', // /usersearch のURLでUserSearchListFormコンポーネントを表示
      name: 'usersearch',
      component: () => import('@/views/UserSearchListForm.vue')
    }
  ]
})

export default router