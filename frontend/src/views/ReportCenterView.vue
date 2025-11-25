<template>
  <div>
    <h2>测量报告中心</h2>
    <p class="hint">后续可与后端的 PDF 报告生成与下载接口对接，展示测量报告列表。</p>
    <DeviceStatusStrip title="监控网络概览" />
  </div>
</template>

<script setup>
import DeviceStatusStrip from '../components/DeviceStatusStrip.vue';
</script>

<style scoped>
h2 {
  margin: 0 0 0.75rem;
}
.hint {
  color: #9ca3af;
}
</style>


