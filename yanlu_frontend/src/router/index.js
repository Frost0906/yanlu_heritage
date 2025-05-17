import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: {
      title: '登录',
      requiresAuth: false
    }
  },
  {
    path: '/',
    component: () => import('../layouts/default.vue'),
    children: [
      {
        path: '',
        name: 'home',
        component: () => import('../views/home/index.vue'),
        meta: { 
          title: '首页',
          requiresAuth: false
        }
      },
      {
        path: 'courses',
        name: 'courses',
        component: () => import('../views/courses/index.vue'),
        meta: { 
          title: '研学课程',
          requiresAuth: false 
        }
      },
      {
        path: 'courses/:id',
        name: 'courseDetail',
        component: () => import('../views/courses/detail.vue'),
        meta: { 
          title: '课程详情',
          requiresAuth: false 
        }
      },
      {
        path: 'team-building',
        name: 'teamBuilding',
        component: () => import('../views/team-building/index.vue'),
        meta: { 
          title: '团建项目',
          requiresAuth: false 
        }
      },
      {
        path: 'team-building/:id',
        name: 'teamBuildingDetail',
        component: () => import('../views/team-building/detail.vue'),
        meta: { 
          title: '项目详情',
          requiresAuth: false 
        }
      },
      {
        path: 'registration',
        name: 'registration',
        component: () => import('../views/registration/index.vue'),
        meta: { 
          title: '在线报名',
          requiresAuth: true 
        }
      },
      {
        path: 'tracking',
        name: 'tracking',
        component: () => import('../views/tracking/index.vue'),
        meta: { 
          title: '行程跟踪',
          requiresAuth: true 
        }
      },
      {
        path: 'community',
        name: 'community',
        component: () => import('../views/community/index.vue'),
        meta: { 
          title: '学习交流',
          requiresAuth: true 
        }
      },
      {
        path: 'community/featured/:id',
        name: 'featuredDetail',
        component: () => import('../views/community/featured-detail.vue'),
        meta: { 
          title: '优秀研学成果',
          requiresAuth: true 
        }
      },
      {
        path: 'profile',
        name: 'profile',
        component: () => import('../views/profile/index.vue'),
        meta: { 
          title: '个人中心',
          requiresAuth: true 
        }
      },
      {
        path: 'customization',
        name: 'customization',
        component: () => import('../views/customization/index.vue'),
        meta: { 
          title: '线路定制',
          requiresAuth: true 
        }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    // 将目标路由信息存储在 localStorage 中
    localStorage.setItem('redirect', JSON.stringify({
      path: to.path,
      query: to.query
    }))
    next('/login')
  } else {
    document.title = to.meta.title ? `${to.meta.title} - 研学课程与团建项目平台` : '研学课程与团建项目平台'
    next()
  }
})

const handleSearch = () => {
  // TODO: 实现搜索逻辑
  ElMessage.success('搜索功能开发中')
}

export default router 