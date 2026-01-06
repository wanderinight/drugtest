<template>
  <section class="layout">
    <aside class="sidebar">
      <SideBar />
    </aside>
    <main class="content">
      <div class="fixed-panel">
        <div class="info-row">
          <div class="info-left">
            <span class="info-item">登录者：{{ staffInfo.name }}</span>
            <span class="info-item">部门：{{ staffInfo.department || '-' }}</span>
            <span class="info-item">权限：{{ staffInfo.roleName || '-' }}</span>
          </div>
          <div class="info-right">
            <span class="info-item">日期：{{ today }}</span>
            <button class="link" @click="refreshOverview" :disabled="loading">
              {{ loading ? '刷新中...' : '刷新总览' }}
            </button>
            <button class="link" @click="handleLogout">退出登录</button>
          </div>
        </div>
        <DeviceStatusStrip
          ref="overviewStrip"
          class="inline-strip"
          title="监控网络概览"
          :show-refresh="false"
        />
      </div>

      <section class="page-body">
        <router-view />
      </section>
    </main>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import SideBar from '../components/SideBar.vue';
import DeviceStatusStrip from '../components/DeviceStatusStrip.vue';

const router = useRouter();
const apiBase = import.meta.env.VITE_API_BASE_URL || '';
const loading = ref(false);
const overviewStrip = ref(null);

const staffInfo = reactive({
  name: localStorage.getItem('staffName') || '未登录',
  department: '',
  roleName: ''
});

const today = computed(() => {
  const date = new Date();
  return date.toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit' });
});

const fetchStaffInfo = async () => {
  const staffCode = localStorage.getItem('staffCode');
  if (!staffCode) return;
  const { data } = await axios.get(`${apiBase}/api/auth/staff-role-dept`, {
    params: { staffcode: staffCode }
  });
  const payload = data?.data;
  if (payload) {
    staffInfo.name = payload.staffname || staffInfo.name;
    staffInfo.department = payload.department || '';
    staffInfo.roleName = payload.role?.roleName || '';
  }
};

const refreshOverview = async () => {
  loading.value = true;
  try {
    await fetchStaffInfo();
    overviewStrip.value?.refreshStats();
  } finally {
    loading.value = false;
  }
};

const handleLogout = () => {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
  localStorage.removeItem('staffName');
  localStorage.removeItem('staffCode');
  router.replace({ name: 'login' });
};

onMounted(() => {
  refreshOverview();
});
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  background: transparent;
  color: #e2e8f0;
}

.sidebar {
  width: 220px;
  background: transparent;
  border-right: 1px solid rgba(148, 163, 184, 0.2);
}

.content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 1.5rem 2rem;
  gap: 1.25rem;
  overflow: hidden;
}

.fixed-panel {
  position: sticky;
  top: 0;
  z-index: 10;
  border-radius: 16px;
  background: radial-gradient(circle at top left, #1f2937, #020617);
  border: 1px solid rgba(148, 163, 184, 0.25);
  box-shadow: 0 18px 40px rgba(15, 23, 42, 0.35);
  padding: 1rem 1.25rem;
  margin-bottom: 0.75rem;
  backdrop-filter: blur(12px);
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
}

.info-row {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
  flex-wrap: wrap;
  font-size: 0.95rem;
}

.info-left,
.info-right {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.info-item {
  color: #e2e8f0;
}

.link {
  border: none;
  background: none;
  color: #38bdf8;
  cursor: pointer;
  padding: 0;
}

.inline-strip :deep(.stats-strip) {
  margin: 0;
  padding: 0;
}

.page-body {
  flex: 1;
  border-radius: 18px;
  background: radial-gradient(circle at top left, #1f2937, #020617);
  padding: 1.5rem 1.75rem;
  box-shadow: 0 18px 40px rgba(15, 23, 42, 0.5);
}
</style>

