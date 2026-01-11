<template>
  <div class="report-center">
    <h2>测量报告中心</h2>
    <p class="hint">按照设备 / 批次 / 产品 / 时间组合筛选，生成 PDF 并支持预览、保存、打印。</p>
<!--    <DeviceStatusStrip title="监控网络概览" />-->

    <section class="section">
      <header class="section-header">
        <div>
          <h3>组合查询条件</h3>
          <p class="sub-hint">设备支持多选，批次与产品可输入多个值（逗号或换行分隔）</p>
        </div>
        <button class="ghost-btn" @click="resetFilters">重置条件</button>
      </header>

      <div class="layout">
        <div class="form-panel">
          <label>
            报告标题
            <input v-model="filters.reportTitle" type="text" placeholder="示例：XX车间-设备组合报告" />
          </label>

          <label>
            设备（多选）
            <div class="select-wrapper">
              <select multiple v-model="filters.deviceCodes">
                <option
                  v-for="device in deviceOptions"
                  :key="device.deviceCode"
                  :value="device.deviceCode"
                >
                  {{ device.deviceName || device.deviceCode }} ({{ device.deviceCode }})
                </option>
              </select>
            </div>
          </label>

          <label>
            批次（多个值用逗号、空格或换行分隔）
            <textarea v-model="filters.batchNos" rows="2" placeholder="如：BATCH001, BATCH002"></textarea>
          </label>

          <label>
            产品（多个值用逗号、空格或换行分隔）
            <textarea v-model="filters.productIds" rows="2" placeholder="如：P001, P002"></textarea>
          </label>

          <div class="grid-two">
            <label>
              起始时间
              <input type="datetime-local" v-model="filters.startTime" />
            </label>
            <label>
              结束时间
              <input type="datetime-local" v-model="filters.endTime" />
            </label>
          </div>

          <label class="checkbox-row">
            <input type="checkbox" v-model="filters.saveFile" />
            <span>生成时保存到服务器（路径：{{ storagePathHint }}）</span>
          </label>

          <div class="actions">
            <button class="primary" :disabled="loading" @click="previewReport">
              {{ loading && actionType === 'preview' ? '预览中...' : '预览 PDF' }}
            </button>
            <button class="secondary" :disabled="loading" @click="generateReport">
              {{ loading && actionType === 'save' ? '生成中...' : '生成并保存' }}
            </button>
            <button class="ghost" :disabled="loading" @click="printReport">
              打印（预留）
            </button>
          </div>

          <p v-if="message" class="status">{{ message }}</p>
          <ul class="summary" v-if="fileName || savedPath || recordCount">
            <li v-if="fileName">文件名：{{ fileName }}</li>
            <li v-if="savedPath">保存路径：{{ savedPath }}</li>
            <li v-if="recordCount">数据条目：{{ recordCount }}</li>
          </ul>
        </div>

        <div class="preview-panel">
          <div class="preview-header">
            <h4>PDF 预览</h4>
            <span v-if="criteriaDescription" class="criteria">{{ criteriaDescription }}</span>
          </div>
          <div class="preview-body">
            <iframe v-if="previewSrc" :src="previewSrc" title="report-preview"></iframe>
            <div v-else class="placeholder">生成或预览后可在此查看 PDF</div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import DeviceStatusStrip from '../components/DeviceStatusStrip.vue';

const apiBase = import.meta.env.VITE_API_BASE_URL || '';
const filters = reactive({
  reportTitle: '组合条件报告',
  deviceCodes: [],
  batchNos: '',
  productIds: '',
  startTime: '',
  endTime: '',
  saveFile: true
});

const deviceOptions = ref([]);
const previewSrc = ref('');
const recordCount = ref(0);
const savedPath = ref('');
const fileName = ref('');
const criteriaDescription = ref('');
const message = ref('');
const loading = ref(false);
const actionType = ref('');
const storagePathHint = './src/report';
const route = useRoute();

const parseListInput = (value) => {
  if (!value) return [];
  return value
    .split(/[\n,，、\s]+/g)
    .map(item => item.trim())
    .filter(Boolean);
};

const buildPayload = (saveFile) => ({
  reportTitle: filters.reportTitle,
  deviceCodes: filters.deviceCodes,
  batchNos: parseListInput(filters.batchNos),
  productIds: parseListInput(filters.productIds),
  startTime: filters.startTime ? new Date(filters.startTime).toISOString() : null,
  endTime: filters.endTime ? new Date(filters.endTime).toISOString() : null,
  saveFile
});

const handleResponse = (result, showSavedPath = false) => {
  if (!result) return;
  if (result.previewBase64) {
    previewSrc.value = `data:application/pdf;base64,${result.previewBase64}`;
  }
  fileName.value = result.fileName || '';
  recordCount.value = result.recordCount || 0;
  criteriaDescription.value = result.criteria || '';
  savedPath.value = showSavedPath ? result.savedPath || '' : '';
};

const previewReport = async () => {
  actionType.value = 'preview';
  await submitGenerate(false);
};

const generateReport = async () => {
  actionType.value = 'save';
  await submitGenerate(true);
};

const submitGenerate = async (saveFile) => {
  message.value = '';
  loading.value = true;
  try {
    const payload = buildPayload(saveFile && filters.saveFile);
    const { data } = await axios.post(`${apiBase}/api/inspect-data/report/generate`, payload);
    if (data?.code !== '200') {
      message.value = data?.msg || '生成失败';
      return;
    }
    handleResponse(data.data, saveFile && filters.saveFile);
    message.value = saveFile && filters.saveFile ? '生成并保存成功' : '生成成功，可预览';
  } catch (error) {
    console.error(error);
    message.value = error?.response?.data?.msg || '生成失败，请稍后重试';
  } finally {
    loading.value = false;
  }
};

const printReport = async () => {
  if (!fileName.value) {
    message.value = '请先生成报告再尝试打印';
    return;
  }
  try {
    await axios.post(`${apiBase}/api/inspect-data/report/print`, {
      fileName: fileName.value,
      filePath: savedPath.value,
      copies: 1,
      remark: 'Web端触发打印'
    });
    message.value = '打印接口已预留，目前仅返回成功提示';
  } catch (error) {
    console.error(error);
    message.value = '打印接口调用失败';
  }
};

const resetFilters = () => {
  filters.reportTitle = '组合条件报告';
  filters.deviceCodes = [];
  filters.batchNos = '';
  filters.productIds = '';
  filters.startTime = '';
  filters.endTime = '';
  filters.saveFile = true;
  message.value = '';
};

const applyRoutePrefill = () => {
  const preDevice = route.query.device;
  if (preDevice && !filters.deviceCodes.includes(preDevice)) {
    filters.deviceCodes = [preDevice];
  }
};

const fetchDevices = async () => {
  try {
    const { data } = await axios.get(`${apiBase}/api/device/getall`);
    deviceOptions.value = data?.data || [];
    applyRoutePrefill();
  } catch (error) {
    console.error('获取设备列表失败', error);
    deviceOptions.value = [];
  }
};

onMounted(() => {
  fetchDevices();
});

watch(
  () => route.query.device,
  () => applyRoutePrefill()
);
</script>

<style scoped>
.report-center h2 {
  margin: 0 0 0.5rem;
}

.hint {
  color: #9ca3af;
  margin-bottom: 1rem;
}

.section {
  background: rgba(15, 23, 42, 0.9);
  border-radius: 16px;
  padding: 1.2rem 1.3rem;
  border: 1px solid rgba(148, 163, 184, 0.35);
  margin-top: 1.5rem;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  gap: 1rem;
  flex-wrap: wrap;
}

.section-header h3 {
  margin: 0;
}

.sub-hint {
  margin: 0.2rem 0 0;
  color: #94a3b8;
  font-size: 0.85rem;
}

.layout {
  display: grid;
  grid-template-columns: minmax(320px, 420px) 1fr;
  gap: 1.5rem;
}

.form-panel {
  display: flex;
  flex-direction: column;
  gap: 0.85rem;
}

.form-panel label {
  display: flex;
  flex-direction: column;
  font-size: 0.85rem;
  color: #94a3b8;
}

.form-panel input,
.form-panel textarea,
.form-panel select {
  margin-top: 0.35rem;
  padding: 0.45rem 0.55rem;
  border-radius: 8px;
  border: 1px solid rgba(148, 163, 184, 0.4);
  background: rgba(2, 6, 23, 0.7);
  color: #e2e8f0;
  font-size: 0.9rem;
}

.form-panel textarea {
  resize: vertical;
}

.select-wrapper select {
  min-height: 110px;
}

.grid-two {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 0.75rem;
}

.checkbox-row {
  flex-direction: row;
  align-items: center;
  gap: 0.4rem;
}

.checkbox-row input {
  margin: 0;
}

.actions {
  display: flex;
  gap: 0.75rem;
  flex-wrap: wrap;
}

button {
  border: none;
  border-radius: 8px;
  padding: 0.45rem 1rem;
  cursor: pointer;
  font-size: 0.9rem;
}

button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.primary {
  background: #2563eb;
  color: #fff;
}

.secondary {
  background: rgba(59, 130, 246, 0.15);
  color: #93c5fd;
  border: 1px solid rgba(59, 130, 246, 0.4);
}

.ghost {
  background: transparent;
  color: #cbd5f5;
  border: 1px solid rgba(148, 163, 184, 0.5);
}

.ghost-btn {
  background: transparent;
  border: 1px solid rgba(148, 163, 184, 0.4);
  color: #cbd5f5;
  padding: 0.3rem 0.9rem;
  border-radius: 999px;
  cursor: pointer;
}

.status {
  color: #facc15;
  font-size: 0.85rem;
  margin: 0;
}

.summary {
  list-style: none;
  padding: 0;
  margin: 0;
  font-size: 0.85rem;
  color: #9ca3af;
}

.preview-panel {
  border-radius: 14px;
  border: 1px solid rgba(148, 163, 184, 0.2);
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
  min-height: 500px;
}

.preview-header {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
}

.preview-header h4 {
  margin: 0;
}

.criteria {
  color: #94a3b8;
  font-size: 0.8rem;
}

.preview-body {
  flex: 1;
  background: rgba(2, 6, 23, 0.7);
  border-radius: 12px;
  border: 1px dashed rgba(94, 234, 212, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.5rem;
}

.preview-body iframe {
  width: 100%;
  height: 100%;
  border: none;
  border-radius: 10px;
  background: #fff;
}

.placeholder {
  color: #64748b;
  font-size: 0.9rem;
}

@media (max-width: 1024px) {
  .layout {
    grid-template-columns: 1fr;
  }
}
</style>
