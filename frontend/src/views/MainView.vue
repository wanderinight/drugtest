<template>
  <section class="layout">
    <aside class="sidebar">
      <SideBar />
    </aside>
    <main class="content">
      <header class="topbar">
        <div>
          <p class="eyebrow">药品检测管理</p>
          <h1 class="title">综合管理平台</h1>
        </div>
        <div class="user-info">
          <span>{{ staffName }}</span>
          <button @click="handleLogout">退出登录</button>
        </div>
      </header>
      <section class="page-body">
        <router-view />
      </section>
    </main>
  </section>
</template>

<script setup>
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import SideBar from '../components/SideBar.vue';

const router = useRouter();
const staffName = computed(() => localStorage.getItem('staffName') || '未登录');

const handleLogout = () => {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
  localStorage.removeItem('staffName');
  router.replace({ name: 'login' });
};
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  background: #020617;
  color: #e2e8f0;
}

.sidebar {
  width: 220px;
  background: #020617;
  border-right: 1px solid rgba(148, 163, 184, 0.2);
}

.content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 1.5rem 2rem;
}

.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.eyebrow {
  font-size: 0.8rem;
  letter-spacing: 0.18em;
  text-transform: uppercase;
  color: #38bdf8;
}

.title {
  margin: 0.2rem 0 0;
  font-size: 1.4rem;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-info button {
  border: 1px solid rgba(248, 250, 252, 0.4);
  background: transparent;
  color: #f8fafc;
  padding: 0.4rem 1rem;
  border-radius: 999px;
  cursor: pointer;
}

.page-body {
  flex: 1;
  border-radius: 18px;
  background: radial-gradient(circle at top left, #1f2937, #020617);
  padding: 1.5rem 1.75rem;
  box-shadow: 0 18px 40px rgba(15, 23, 42, 0.5);
}
</style>

