<template>
  <div>
    <h2>设备管理中心</h2>
    <p class="hint">后续可以在这里实现设备列表、绑定关系、运行状态管理等功能。</p>
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


