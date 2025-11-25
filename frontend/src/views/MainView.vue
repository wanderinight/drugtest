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
        <div class="stats-row">
          <div>
            <span class="stat-label">设备总数：</span>
            <span class="stat-value">{{ summary.totalDevices }}</span>
          </div>
          <div>
            <span class="stat-label">今日报警数量：</span>
            <span class="stat-value alert">{{ summary.todayAlerts }}</span>
          </div>
          <div>
            <span class="stat-label">待校准设备数量：</span>
            <span class="stat-value">{{ summary.pendingCalibration }}</span>
          </div>
        </div>
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

const router = useRouter();
const apiBase = import.meta.env.VITE_API_BASE_URL || '';
const loading = ref(false);

const staffInfo = reactive({
  name: localStorage.getItem('staffName') || '未登录',
  department: '',
  roleName: ''
});

const summary = reactive({
  totalDevices: 0,
  todayAlerts: 0,
  pendingCalibration: 0
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

const fetchSummary = async () => {
  const now = new Date().toISOString();
  const [totalRes, alertRes, pendingRes] = await Promise.all([
    axios.get(`${apiBase}/api/device/countall`).catch(() => ({ data: { data: 0 } })),
    axios.post(`${apiBase}/api/alert/count-today`, { currentTime: now }).catch(() => ({ data: { data: 0 } })),
    axios.get(`${apiBase}/api/device/waitcalibration-countnow`).catch(() => ({ data: { data: 0 } }))
  ]);
  summary.totalDevices = Number(totalRes.data?.data ?? 0);
  summary.todayAlerts = Number(alertRes.data?.data ?? 0);
  summary.pendingCalibration = Number(pendingRes.data?.data ?? 0);
};

const refreshOverview = async () => {
  loading.value = true;
  try {
    await Promise.all([fetchStaffInfo(), fetchSummary()]);
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
  gap: 1.25rem;
  overflow: hidden;
}

.fixed-panel {
  position: sticky;
  top: 0;
  z-index: 10;
  background: rgba(2, 6, 23, 0.9);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid rgba(148, 163, 184, 0.2);
  padding: 0.8rem 0;
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
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

.stats-row {
  display: flex;
  gap: 2rem;
  flex-wrap: wrap;
  font-size: 0.9rem;
  color: #94a3b8;
}

.stat-label {
  margin-right: 0.25rem;
}

.stat-value {
  color: #e2e8f0;
  font-weight: 600;
}

.stat-value.alert {
  color: #f97316;
}

.page-body {
  flex: 1;
  border-radius: 18px;
  background: radial-gradient(circle at top left, #1f2937, #020617);
  padding: 1.5rem 1.75rem;
  box-shadow: 0 18px 40px rgba(15, 23, 42, 0.5);
}
</style>

