import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '../views/LoginView.vue';
import MainView from '../views/MainView.vue';
import HomeView from '../views/HomeView.vue';
import DeviceCenterView from '../views/DeviceCenterView.vue';
import MonitorCenterView from '../views/MonitorCenterView.vue';
import StatisticsCenterView from '../views/StatisticsCenterView.vue';
import ReportCenterView from '../views/ReportCenterView.vue';
import UserCenterView from '../views/UserCenterView.vue';

const routes = [
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
    path: '/main',
    component: MainView,
    meta: { requiresAuth: true },
    children: [
      { path: '', redirect: '/main/home' },
      { path: 'home', name: 'home', component: HomeView },
      { path: 'device', name: 'device-center', component: DeviceCenterView },
      { path: 'monitor', name: 'monitor-center', component: MonitorCenterView },
      { path: 'statistics', name: 'statistics-center', component: StatisticsCenterView },
      { path: 'report', name: 'report-center', component: ReportCenterView },
      { path: 'user', name: 'user-center', component: UserCenterView }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  // 只对需要鉴权的路由做校验（/main 及其子路由）
  if (!to.matched.some(r => r.meta?.requiresAuth)) {
    return next();
  }

  const token = localStorage.getItem('accessToken');
  if (!token) {
    return next({ name: 'login' });
  }
  return next();
});

export default router;

