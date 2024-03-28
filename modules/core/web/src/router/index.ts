import { createRouter, createWebHashHistory } from 'vue-router'

import authService from '@/services/auth.service'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/home',
      name: 'Home',
      component: () => import('../views/ViewHome.vue')
    },
    {
      path: '/modeling',
      name: 'Modeling',
      component: () => import('../views/ViewModeling.vue')
    },
    {
      path: '/diagramConfig',
      name: 'DiagramConfig',
      component: () => import('../views/ViewDiagramConfig.vue')
    },
    {
      path: '/diagramConfig/:id',
      name: 'DiagramConfigDetail',
      component: () => import('../views/ViewDiagramConfigDetail.vue')
    },
    {
      path: '/impressum',
      name: 'ViewImpressum',
      component: () => import('../views/ViewImpressum.vue')
    },
    {
      path: '/datenschutz',
      name: 'ViewDatenschutz',
      component: () => import('../views/ViewDatenschutz.vue')
    },
    {
      path: '/login',
      name: 'ViewLogin',
      component: () => import('../views/ViewLogin.vue')
    },
    {
      path: '/profile',
      name: 'ViewProfile',
      component: () => import('../views/ViewProfile.vue')
    },
    {
      path: '/course',
      name: 'ViewCourses',
      component: () => import('../views/ViewCourses.vue')
    },
    {
      path: '/course/:id/signup',
      name: 'ViewCourseSignup',
      component: () => import('../views/ViewCourseSignup.vue')
    },
    {
      path: '/course/:id',
      name: 'ViewCourse',
      component: () => import('../views/ViewCourse.vue')
    },
    {
      path: '/course/:courseId/task/:taskId',
      name: 'ViewTask',
      component: () => import('../views/ViewTask.vue')
    },
    {
      path: '/',
      name: 'ViewIntroduction',
      component: () => import('../views/ViewIntroduction.vue')
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'View404Page',
      component: () => import('../views/View404Page.vue')
    },
    {
      path: '/feedbackReport',
      name: 'ViewBugReport',
      component: () => import('../views/ViewFeedbackReport.vue')
    },
    {
      path: '/feedbackOverview',
      name: 'ViewBugOverview',
      component: () => import('../views/ViewFeedbackOverview.vue')
    },
    {
      path: '/course/:courseId/members',
      name: 'ViewMembers',
      component: () => import('../views/ViewMembers.vue')
    },
    {
      path: '/course/:courseId/task/:taskId/submissions',
      name: 'ViewTaskSubmissions',
      component: () => import('../views/ViewTaskSubmissions.vue')
    }
  ]
})

router.beforeEach(async (to, from, next) => {
  const publicPages = ['/login']
  const authRequired = !publicPages.includes(to.path)
  const loggedIn = localStorage.getItem('user')

  const nonAdminPages = ['Home', 'Modeling', 'ViewImpressum', 'ViewDatenschutz', 'ViewLogin', 'ViewCourses', 'ViewCourseSignup', 'ViewCourse', 'ViewTask', 'ViewIntroduction', 'View404Page', 'FeedbackReport', 'ViewMembers', 'ViewTaskSubmissions']
  const adminRequired = !nonAdminPages.includes(to.name as string)
  const role = localStorage.getItem('role')
  const admin = role?.includes('ADMIN')

  const loginValid = await authService.isValid().then((response) => {
    if (response === true) {
      return true
    } else {
      localStorage.removeItem('user')
      return false
    }
  })

  // trying to access a restricted page + not logged in
  // redirect to login page
  if ((authRequired && !loggedIn) || (authRequired && !loginValid)) {
    next('/login')
  } else {
    if (adminRequired && !admin) {
      next('/404')
    } else {
      next()
    }
  }
})

export default router
