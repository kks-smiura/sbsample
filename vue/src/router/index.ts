import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import UserEditForm from '../views/UserEditForm.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/useredit', // /useredit のURLでUserEditFormコンポーネントを表示
      name: 'useredit',
      component: UserEditForm
    }
  ]
})

export default router