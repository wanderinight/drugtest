<template>
  <div>
    <h2>计量校准中心</h2>
    <p class="hint">后续可接入设备校准计划、执行情况与趋势分析。</p>
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


