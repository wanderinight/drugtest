<template>
  <div class="user-center">
    <h2>用户中心</h2>
    <p class="hint">用于管理系统用户、角色权限等信息。</p>

    <div class="tabs">
      <button
        v-for="tab in tabs"
        :key="tab.key"
        :class="['tab-btn', { active: activeTab === tab.key }]"
        @click="activeTab = tab.key"
      >
        {{ tab.label }}
      </button>
    </div>

    <!-- 人员管理 -->
    <div v-show="activeTab === 'staff'" class="tab-content">
      <section class="section">
        <header class="section-header">
          <div>
            <h3>人员管理</h3>
            <p class="sub-hint">管理系统员工信息</p>
          </div>
          <button class="add-btn" @click="showStaffDialog = true">添加人员</button>
        </header>

        <div class="table-container">
          <table class="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>姓名</th>
                <th>工号</th>
                <th>部门</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="staff in staffList" :key="staff.staffId">
                <td>{{ staff.staffId }}</td>
                <td>{{ staff.staffname || '-' }}</td>
                <td>{{ staff.staffcode || '-' }}</td>
                <td>{{ staff.department || '-' }}</td>
                <td>
                  <button class="delete-btn" @click="deleteStaff(staff.staffId)">删除</button>
                </td>
              </tr>
              <tr v-if="!staffList.length">
                <td colspan="5" class="empty-cell">暂无人员数据</td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>

      <!-- 添加人员对话框 -->
      <div v-if="showStaffDialog" class="dialog-overlay" @click.self="showStaffDialog = false">
        <div class="dialog">
          <h3>添加人员</h3>
          <form @submit.prevent="addStaff">
            <label>
              姓名
              <input v-model.trim="staffForm.staffname" type="text" required />
            </label>
            <label>
              工号
              <input v-model.trim="staffForm.staffcode" type="text" required />
            </label>
            <label>
              密码
              <input v-model.trim="staffForm.password" type="password" required />
            </label>
            <label>
              部门
              <input v-model.trim="staffForm.department" type="text" />
            </label>
            <label>
              角色
              <select v-model.number="staffForm.roleId" required>
                <option :value="null">请选择角色</option>
                <option v-for="role in roleList" :key="role.roleId" :value="role.roleId">
                  {{ role.roleName }}
                </option>
              </select>
            </label>
            <div class="dialog-actions">
              <button type="button" class="cancel-btn" @click="showStaffDialog = false">取消</button>
              <button type="submit" class="submit-btn" :disabled="staffLoading">
                {{ staffLoading ? '添加中...' : '确定' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- 设备管理 -->
    <div v-show="activeTab === 'device'" class="tab-content">
      <section class="section">
        <header class="section-header">
          <div>
            <h3>设备管理</h3>
            <p class="sub-hint">管理系统设备信息</p>
          </div>
          <button class="add-btn" @click="showDeviceDialog = true">添加设备</button>
        </header>

        <div class="table-container">
          <table class="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>设备名称</th>
                <th>设备编号</th>
                <th>位置</th>
                <th>设备类型</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="device in deviceList" :key="device.deviceId">
                <td>{{ device.deviceId }}</td>
                <td>{{ device.deviceName || '-' }}</td>
                <td>{{ device.deviceCode || '-' }}</td>
                <td>{{ device.location || '-' }}</td>
                <td>{{ device.deviceType || '-' }}</td>
                <td>
                  <button class="delete-btn" @click="deleteDevice(device.deviceCode)">删除</button>
                </td>
              </tr>
              <tr v-if="!deviceList.length">
                <td colspan="6" class="empty-cell">暂无设备数据</td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>

      <!-- 添加设备对话框 -->
      <div v-if="showDeviceDialog" class="dialog-overlay" @click.self="showDeviceDialog = false">
        <div class="dialog">
          <h3>添加设备</h3>
          <form @submit.prevent="addDevice">
            <label>
              设备名称
              <input v-model.trim="deviceForm.deviceName" type="text" required />
            </label>
            <label>
              设备编号
              <input v-model.trim="deviceForm.deviceCode" type="text" required />
            </label>
            <label>
              位置
              <input v-model.trim="deviceForm.location" type="text" />
            </label>
            <label>
              设备类型
              <select v-model="deviceForm.deviceType" required>
                <option value="">请选择类型</option>
                <option value="D4">D4</option>
                <option value="DB">DB</option>
              </select>
            </label>
            <div class="dialog-actions">
              <button type="button" class="cancel-btn" @click="showDeviceDialog = false">取消</button>
              <button type="submit" class="submit-btn" :disabled="deviceLoading">
                {{ deviceLoading ? '添加中...' : '确定' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- 权限管理 -->
    <div v-show="activeTab === 'permission'" class="tab-content">
      <section class="section">
        <header class="section-header">
          <div>
            <h3>权限管理</h3>
            <p class="sub-hint">管理系统权限信息</p>
          </div>
          <button class="add-btn" @click="showPermissionDialog = true">添加权限</button>
        </header>

        <div class="table-container">
          <table class="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>权限名称</th>
                <th>权限代码</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="permission in permissionList" :key="permission.permissionId">
                <td>{{ permission.permissionId }}</td>
                <td>{{ permission.permission || '-' }}</td>
                <td>{{ permission.permissionCode || '-' }}</td>
                <td>
                  <button class="delete-btn" @click="deletePermission(permission.permissionId)">删除</button>
                </td>
              </tr>
              <tr v-if="!permissionList.length">
                <td colspan="4" class="empty-cell">暂无权限数据</td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>

      <!-- 添加权限对话框 -->
      <div v-if="showPermissionDialog" class="dialog-overlay" @click.self="showPermissionDialog = false">
        <div class="dialog">
          <h3>添加权限</h3>
          <form @submit.prevent="addPermission">
            <label>
              权限名称
              <input v-model.trim="permissionForm.permission" type="text" required />
            </label>
            <label>
              权限代码
              <input v-model.trim="permissionForm.permissionCode" type="text" required />
            </label>
            <div class="dialog-actions">
              <button type="button" class="cancel-btn" @click="showPermissionDialog = false">取消</button>
              <button type="submit" class="submit-btn" :disabled="permissionLoading">
                {{ permissionLoading ? '添加中...' : '确定' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, watch } from 'vue';
import axios from 'axios';

const apiBase = import.meta.env.VITE_API_BASE_URL || '';
const activeTab = ref('staff');

const tabs = [
  { key: 'staff', label: '人员管理' },
  { key: 'device', label: '设备管理' },
  { key: 'permission', label: '权限管理' }
];

// 人员管理
const staffList = ref([]);
const roleList = ref([]);
const showStaffDialog = ref(false);
const staffLoading = ref(false);
const staffForm = reactive({
  staffname: '',
  staffcode: '',
  password: '',
  department: '',
  roleId: null
});

// 设备管理
const deviceList = ref([]);
const showDeviceDialog = ref(false);
const deviceLoading = ref(false);
const deviceForm = reactive({
  deviceName: '',
  deviceCode: '',
  location: '',
  deviceType: ''
});

// 权限管理
const permissionList = ref([]);
const showPermissionDialog = ref(false);
const permissionLoading = ref(false);
const permissionForm = reactive({
  permission: '',
  permissionCode: ''
});

// 获取所有角色
const fetchRoles = async () => {
  try {
    const { data } = await axios.get(`${apiBase}/api/roles/list`);
    roleList.value = data?.data ?? [];
  } catch (error) {
    console.error('获取角色列表失败:', error);
  }
};

// 获取所有人员
const fetchStaff = async () => {
  try {
    const { data } = await axios.get(`${apiBase}/api/admin/users/list`);
    staffList.value = data?.data ?? [];
  } catch (error) {
    console.error('获取人员列表失败:', error);
    alert('获取人员列表失败: ' + (error.response?.data?.msg || error.message));
  }
};

// 添加人员
const addStaff = async () => {
  if (staffLoading.value) return;
  staffLoading.value = true;
  try {
    const { data } = await axios.post(`${apiBase}/api/admin/users/add`, {
      staff: {
        staffname: staffForm.staffname,
        staffcode: staffForm.staffcode,
        password: staffForm.password,
        department: staffForm.department || null
      },
      roleId: staffForm.roleId
    });
    if (data?.code === '200') {
      alert('人员添加成功');
      showStaffDialog.value = false;
      Object.assign(staffForm, {
        staffname: '',
        staffcode: '',
        password: '',
        department: '',
        roleId: null
      });
      await fetchStaff();
    } else {
      alert('添加失败: ' + (data?.msg || '未知错误'));
    }
  } catch (error) {
    console.error('添加人员失败:', error);
    alert('添加人员失败: ' + (error.response?.data?.msg || error.message));
  } finally {
    staffLoading.value = false;
  }
};

// 删除人员
const deleteStaff = async (staffId) => {
  if (!confirm('确定要删除该人员吗？')) return;
  try {
    const { data } = await axios.delete(`${apiBase}/api/admin/users/delete/${staffId}`);
    if (data?.code === '200') {
      alert('人员删除成功');
      await fetchStaff();
    } else {
      alert('删除失败: ' + (data?.msg || '未知错误'));
    }
  } catch (error) {
    console.error('删除人员失败:', error);
    alert('删除人员失败: ' + (error.response?.data?.msg || error.message));
  }
};

// 获取所有设备
const fetchDevices = async () => {
  try {
    const { data } = await axios.get(`${apiBase}/api/device/getall`);
    deviceList.value = data?.data ?? [];
  } catch (error) {
    console.error('获取设备列表失败:', error);
    alert('获取设备列表失败: ' + (error.response?.data?.msg || error.message));
  }
};

// 添加设备
const addDevice = async () => {
  if (deviceLoading.value) return;
  deviceLoading.value = true;
  try {
    const { data } = await axios.post(`${apiBase}/api/device/add`, deviceForm);
    if (data?.code === '200') {
      alert('设备添加成功');
      showDeviceDialog.value = false;
      Object.assign(deviceForm, {
        deviceName: '',
        deviceCode: '',
        location: '',
        deviceType: ''
      });
      await fetchDevices();
    } else {
      alert('添加失败: ' + (data?.msg || '未知错误'));
    }
  } catch (error) {
    console.error('添加设备失败:', error);
    alert('添加设备失败: ' + (error.response?.data?.msg || error.message));
  } finally {
    deviceLoading.value = false;
  }
};

// 删除设备
const deleteDevice = async (deviceCode) => {
  if (!confirm('确定要删除该设备吗？')) return;
  try {
    const { data } = await axios.delete(`${apiBase}/api/device/delete/${deviceCode}`);
    if (data?.code === '200') {
      alert('设备删除成功');
      await fetchDevices();
    } else {
      alert('删除失败: ' + (data?.msg || '未知错误'));
    }
  } catch (error) {
    console.error('删除设备失败:', error);
    alert('删除设备失败: ' + (error.response?.data?.msg || error.message));
  }
};

// 获取所有权限
const fetchPermissions = async () => {
  try {
    const { data } = await axios.get(`${apiBase}/api/permissions/list`);
    permissionList.value = data?.data ?? [];
  } catch (error) {
    console.error('获取权限列表失败:', error);
    alert('获取权限列表失败: ' + (error.response?.data?.msg || error.message));
  }
};

// 添加权限
const addPermission = async () => {
  if (permissionLoading.value) return;
  permissionLoading.value = true;
  try {
    const { data } = await axios.post(`${apiBase}/api/permissions/add`, permissionForm);
    if (data?.code === '200') {
      alert('权限添加成功');
      showPermissionDialog.value = false;
      Object.assign(permissionForm, {
        permission: '',
        permissionCode: ''
      });
      await fetchPermissions();
    } else {
      alert('添加失败: ' + (data?.msg || '未知错误'));
    }
  } catch (error) {
    console.error('添加权限失败:', error);
    alert('添加权限失败: ' + (error.response?.data?.msg || error.message));
  } finally {
    permissionLoading.value = false;
  }
};

// 删除权限
const deletePermission = async (permissionId) => {
  if (!confirm('确定要删除该权限吗？')) return;
  try {
    const { data } = await axios.delete(`${apiBase}/api/permissions/delete/${permissionId}`);
    if (data?.code === '200') {
      alert('权限删除成功');
      await fetchPermissions();
    } else {
      alert('删除失败: ' + (data?.msg || '未知错误'));
    }
  } catch (error) {
    console.error('删除权限失败:', error);
    alert('删除权限失败: ' + (error.response?.data?.msg || error.message));
  }
};

// 根据当前选中的标签页加载数据
const loadDataForTab = async () => {
  if (activeTab.value === 'staff') {
    await fetchStaff();
    await fetchRoles();
  } else if (activeTab.value === 'device') {
    await fetchDevices();
  } else if (activeTab.value === 'permission') {
    await fetchPermissions();
  }
};

// 监听标签页切换，自动加载数据
watch(activeTab, () => {
  loadDataForTab();
});

onMounted(() => {
  loadDataForTab();
});
</script>

<style scoped>
.user-center h2 {
  margin: 0 0 0.5rem;
}

.hint {
  color: #9ca3af;
  margin-bottom: 1.5rem;
}

.tabs {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
  border-bottom: 1px solid rgba(148, 163, 184, 0.2);
}

.tab-btn {
  padding: 0.75rem 1.5rem;
  background: transparent;
  border: none;
  border-bottom: 2px solid transparent;
  color: #94a3b8;
  cursor: pointer;
  font-size: 0.95rem;
  transition: all 0.2s;
}

.tab-btn:hover {
  color: #cbd5f5;
}

.tab-btn.active {
  color: #38bdf8;
  border-bottom-color: #38bdf8;
}

.tab-content {
  animation: fadeIn 0.3s;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.section {
  background: rgba(15, 23, 42, 0.9);
  border-radius: 16px;
  border: 1px solid rgba(148, 163, 184, 0.2);
  padding: 1.5rem;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.section-header h3 {
  margin: 0;
}

.sub-hint {
  margin: 0.2rem 0 0;
  color: #94a3b8;
  font-size: 0.85rem;
}

.add-btn {
  padding: 0.5rem 1rem;
  background: #38bdf8;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background 0.2s;
}

.add-btn:hover {
  background: #0ea5e9;
}

.table-container {
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  color: #cbd5f5;
}

.data-table thead {
  background: rgba(148, 163, 184, 0.1);
}

.data-table th {
  padding: 0.75rem;
  text-align: left;
  font-weight: 600;
  font-size: 0.9rem;
  color: #94a3b8;
  border-bottom: 1px solid rgba(148, 163, 184, 0.2);
}

.data-table td {
  padding: 0.75rem;
  border-bottom: 1px solid rgba(148, 163, 184, 0.1);
  font-size: 0.9rem;
}

.data-table tbody tr:hover {
  background: rgba(148, 163, 184, 0.05);
}

.empty-cell {
  text-align: center;
  color: #94a3b8;
  padding: 2rem;
}

.delete-btn {
  padding: 0.4rem 0.8rem;
  background: #ef4444;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.85rem;
  transition: background 0.2s;
}

.delete-btn:hover {
  background: #dc2626;
}

.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.dialog {
  background: rgba(15, 23, 42, 0.95);
  border: 1px solid rgba(148, 163, 184, 0.2);
  border-radius: 16px;
  padding: 2rem;
  width: 90%;
  max-width: 500px;
  color: #cbd5f5;
}

.dialog h3 {
  margin: 0 0 1.5rem;
  color: #cbd5f5;
}

.dialog label {
  display: block;
  margin-bottom: 1rem;
  font-size: 0.9rem;
  color: #94a3b8;
}

.dialog input,
.dialog select {
  width: 100%;
  padding: 0.6rem;
  margin-top: 0.4rem;
  background: rgba(2, 6, 23, 0.9);
  border: 1px solid rgba(148, 163, 184, 0.3);
  border-radius: 8px;
  color: #cbd5f5;
  font-size: 0.9rem;
}

.dialog input:focus,
.dialog select:focus {
  outline: none;
  border-color: #38bdf8;
}

.dialog-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  margin-top: 1.5rem;
}

.cancel-btn,
.submit-btn {
  padding: 0.6rem 1.2rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.2s;
}

.cancel-btn {
  background: rgba(148, 163, 184, 0.2);
  color: #cbd5f5;
}

.cancel-btn:hover {
  background: rgba(148, 163, 184, 0.3);
}

.submit-btn {
  background: #38bdf8;
  color: white;
}

.submit-btn:hover:not(:disabled) {
  background: #0ea5e9;
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
