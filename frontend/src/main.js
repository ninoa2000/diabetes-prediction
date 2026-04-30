import { createApp } from 'vue';
import { createPinia } from 'pinia';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import App from './App.vue';
import router from './router';

// Import all Element Plus icons
import * as ElementPlusIconsVue from '@element-plus/icons-vue';

// Create Vue app
const app = createApp(App);

// Register all Element Plus icons globally
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

// Use plugins
app.use(createPinia());
app.use(router);
app.use(ElementPlus);

// Mount app
app.mount('#app'); 