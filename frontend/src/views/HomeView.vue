<template>
  <div class="home">
    <section class="section">
      <header class="section-header">
        <h2>实时报警数据</h2>
      </header>
      <div class="table-wrapper">
        <table>
          <thead>
            <tr>
              <th>时间</th>
              <th>设备编号</th>
              <th>设备位置</th>
              <th>报警内容</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="alert in alerts" :key="alert.id">
              <td>{{ alert.alertTime }}</td>
              <td>{{ alert.deviceCode }}</td>
              <td>{{ alert.location }}</td>
              <td>{{ alert.message }}</td>
            </tr>
            <tr v-if="!alerts.length">
              <td colspan="4" class="empty">暂无报警记录</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>

    <section class="section">
      <header class="section-header">
        <h2>我关注的设备</h2>
        <p class="hint">每 5 分钟自动刷新一次，也可以手动刷新。</p>
      </header>
      <div class="table-wrapper">
        <table>
          <thead>
            <tr>
              <th>设备编号</th>
              <th>类别</th>
              <th>名称</th>
              <th>位置</th>
              <th>监控状态</th>
              <th>运行状态</th>
              <th>报警状态</th>
              <th>校准状态</th>
              <th>上次校准时间</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="d in followedDevices" :key="d.deviceId">
              <td>{{ d.deviceCode }}</td>
              <td>{{ formatDeviceType(d.deviceType) }}</td>
              <td>{{ d.deviceName }}</td>
              <td>{{ d.location }}</td>
              <td>{{ formatMonitorStatus(d.monitorStatus) }}</td>
              <td>{{ formatOperationalStatus(d.operationalStatus) }}</td>
              <td>{{ formatAlertStatus(d.alertStatus) }}</td>
              <td>{{ formatCalibrationStatus(d.calibrationStatus) }}</td>
              <td>{{ formatDateTime(d.lastCalibration) }}</td>
            </tr>
            <tr v-if="!followedDevices.length">
              <td colspan="9" class="empty">暂无关注设备</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from 'vue';
import axios from 'axios';

const apiBase = import.meta.env.VITE_API_BASE_URL || '';

const alerts = ref([]);
const followedDevices = ref([]);
let timerId = null;

const formatDateTime = (value) => {
  if (!value) return '-';
  return value.toString().replace('T', ' ');
};

const formatDeviceType = (type) => {
  if (!type) return '-';
  const map = {
    D4: 'D4 检测仪',
    DB: 'DB 检测仪'
  };
  return map[type] || type;
};

const formatMonitorStatus = (status) => {
  const map = {
    MONITORON: '监控中',
    MONITORINTERRUPT: '监控中断',
    MONITOROFF: '未监控'
  };
  return map[status] || status || '-';
};

const formatOperationalStatus = (status) => {
  const map = {
    RUNNING: '运行中',
    OFFLINE: '离线'
  };
  return map[status] || status || '-';
};

const formatAlertStatus = (status) => {
  const map = {
    ON: '报警中',
    OFF: '无报警'
  };
  return map[status] || status || '-';
};

const formatCalibrationStatus = (status) => {
  const map = {
    NORMAL: '已校准',
    PENDING: '待校准'
  };
  return map[status] || status || '-';
};

const fetchAlerts = async () => {
  const { data } = await axios.get(`${apiBase}/api/alert/details`, {
    params: { page: 0, size: 10 }
  });
  alerts.value = data?.data?.content ?? data?.data ?? [];
};

const fetchFollowedDevices = async () => {
  const staffCode = localStorage.getItem('staffCode');
  if (!staffCode) return;

  const { data } = await axios.get(`${apiBase}/api/device/follow`, {
    params: { staffcode: staffCode }
  });
  followedDevices.value = data?.data ?? [];
};

const refreshAll = async () => {
  await Promise.all([fetchAlerts(), fetchFollowedDevices()]);
};

onMounted(() => {
  refreshAll();
  timerId = setInterval(refreshAll, 5 * 60 * 1000);
});

onUnmounted(() => {
  if (timerId) {
    clearInterval(timerId);
  }
});
</script>

<style scoped>
.home {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.section {
  background: rgba(15, 23, 42, 0.9);
  border-radius: 16px;
  padding: 1.2rem 1.3rem;
  border: 1px solid rgba(148, 163, 184, 0.35);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 0.8rem;
}

.hint {
  font-size: 0.85rem;
  color: #9ca3af;
}

.table-wrapper {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.9rem;
}

th,
td {
  padding: 0.5rem 0.6rem;
  text-align: left;
  border-bottom: 1px solid rgba(30, 64, 175, 0.4);
}

th {
  color: #9ca3af;
  font-weight: 500;
}

.empty {
  text-align: center;
  color: #6b7280;
}
</style>


