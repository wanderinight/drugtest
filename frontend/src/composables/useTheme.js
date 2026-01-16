import { ref, watch } from 'vue';

const STORAGE_KEY = 'app-background-color';
const DEFAULT_COLOR = '#ffffff'; // 默认白色

// 预设的背景色选项
export const backgroundColors = [
  { name: '白色', value: '#ffffff', preview: '#ffffff' },
  { name: '浅灰', value: '#f5f5f5', preview: '#f5f5f5' },
  { name: '浅蓝', value: '#f0f9ff', preview: '#f0f9ff' },
  { name: '浅绿', value: '#f0fdf4', preview: '#f0fdf4' },
  { name: '浅黄', value: '#fefce8', preview: '#fefce8' },
  { name: '深色', value: '#0f172a', preview: '#0f172a' },
  { name: '深蓝', value: '#020617', preview: '#020617' },
];

// 从localStorage读取背景色（在模块加载时立即执行）
const getInitialColor = () => {
  if (typeof window !== 'undefined') {
    const saved = localStorage.getItem(STORAGE_KEY);
    return saved || DEFAULT_COLOR;
  }
  return DEFAULT_COLOR;
};

const backgroundColor = ref(getInitialColor());

// 应用背景色到body和html
const applyBackgroundColor = (color) => {
  if (typeof document !== 'undefined') {
    // 使用setTimeout确保DOM已完全加载
    setTimeout(() => {
      // 应用到html和body，使用!important确保优先级
      document.documentElement.style.setProperty('background-color', color, 'important');
      document.body.style.setProperty('background-color', color, 'important');
      // 同时应用到#app
      const app = document.getElementById('app');
      if (app) {
        app.style.setProperty('background-color', color, 'important');
      }
      // 使用CSS变量，方便全局使用
      document.documentElement.style.setProperty('--app-background-color', color);
    }, 0);
  }
};

// 初始化时立即应用背景色（使用setTimeout确保DOM已加载）
if (typeof document !== 'undefined') {
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
      applyBackgroundColor(backgroundColor.value);
    });
  } else {
    // DOM已加载，立即应用
    setTimeout(() => {
      applyBackgroundColor(backgroundColor.value);
    }, 0);
  }
}

// 从localStorage读取背景色
const loadBackgroundColor = () => {
  if (typeof window !== 'undefined') {
    const saved = localStorage.getItem(STORAGE_KEY);
    if (saved) {
      backgroundColor.value = saved;
    } else {
      backgroundColor.value = DEFAULT_COLOR;
    }
    applyBackgroundColor(backgroundColor.value);
  }
};

// 保存背景色到localStorage
const saveBackgroundColor = (color) => {
  if (typeof window !== 'undefined') {
    localStorage.setItem(STORAGE_KEY, color);
    backgroundColor.value = color;
    applyBackgroundColor(color);
  }
};

// 监听背景色变化
watch(backgroundColor, (newColor) => {
  applyBackgroundColor(newColor);
});

export function useTheme() {
  return {
    backgroundColor,
    backgroundColors,
    setBackgroundColor: saveBackgroundColor,
    loadBackgroundColor,
  };
}

