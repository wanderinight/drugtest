import { ref, watch } from 'vue';

const STORAGE_KEY = 'app-background-color';
const DEFAULT_COLOR = '#ffffff'; // 默认白色

// 预设的背景色选项（只有黑白）
export const backgroundColors = [
  { name: '白色', value: '#ffffff', preview: '#ffffff', textColor: '#000000' },
  { name: '黑色', value: '#000000', preview: '#000000', textColor: '#ffffff' },
];

// 判断颜色是否为浅色（用于自动调整文字颜色）
const isLightColor = (color) => {
  // 将颜色转换为RGB
  const hex = color.replace('#', '');
  const r = parseInt(hex.substr(0, 2), 16);
  const g = parseInt(hex.substr(2, 2), 16);
  const b = parseInt(hex.substr(4, 2), 16);
  // 计算亮度
  const brightness = (r * 299 + g * 587 + b * 114) / 1000;
  return brightness > 128;
};

// 根据背景色获取文字颜色
const getTextColor = (backgroundColor) => {
  if (isLightColor(backgroundColor)) {
    return '#000000'; // 浅色背景用黑色文字
  } else {
    return '#ffffff'; // 深色背景用白色文字
  }
};

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
      // 获取对应的文字颜色
      const textColor = getTextColor(color);
      
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
      document.documentElement.style.setProperty('--app-text-color', textColor);
      
      // 应用文字颜色到body
      document.body.style.setProperty('color', textColor, 'important');
      
      // 更新所有卡片的背景色和文字颜色
      updateCardStyles(color, textColor);
    }, 0);
  }
};

// 更新卡片样式
const updateCardStyles = (bgColor, textColor) => {
  // 计算卡片背景色（根据主背景色调整透明度，增加对比度）
  const isLight = isLightColor(bgColor);
  let cardBgColor;
  if (isLight) {
    // 白色背景：卡片使用稍微深一点的灰色，增加对比度
    cardBgColor = 'rgba(0, 0, 0, 0.08)';
  } else {
    // 黑色背景：卡片使用稍微亮一点的灰色，增加对比度
    cardBgColor = 'rgba(255, 255, 255, 0.15)';
  }
  
  // 应用CSS变量
  document.documentElement.style.setProperty('--card-background-color', cardBgColor);
  document.documentElement.style.setProperty('--card-text-color', textColor);
  
  // 延迟更新，确保DOM已渲染
  setTimeout(() => {
    // 更新所有section元素
    const sections = document.querySelectorAll('.section');
    sections.forEach(section => {
      section.style.setProperty('background-color', cardBgColor, 'important');
      section.style.setProperty('color', textColor, 'important');
    });
    
    // 更新所有theme-section元素
    const themeSections = document.querySelectorAll('.theme-section');
    themeSections.forEach(section => {
      section.style.setProperty('background-color', cardBgColor, 'important');
      section.style.setProperty('color', textColor, 'important');
    });
    
    // 更新所有role-list-container和permission-list-container
    const containers = document.querySelectorAll('.role-list-container, .permission-list-container');
    containers.forEach(container => {
      container.style.setProperty('background-color', cardBgColor, 'important');
      container.style.setProperty('color', textColor, 'important');
    });
  }, 50);
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
    getTextColor,
    isLightColor,
  };
}

