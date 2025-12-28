import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import './styles/base.css';
import { useTheme } from './composables/useTheme';

// 在应用启动时立即加载背景色
const { loadBackgroundColor } = useTheme();

// 确保在DOM加载后应用背景色
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', () => {
    loadBackgroundColor();
  });
} else {
  loadBackgroundColor();
}

const app = createApp(App);

app.use(router);
app.mount('#app');

