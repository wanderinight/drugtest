<template>
  <router-view />
</template>

<script setup>
import { onMounted } from 'vue';
import { useTheme } from './composables/useTheme';

const { loadBackgroundColor } = useTheme();

onMounted(() => {
  loadBackgroundColor();
});
</script>

