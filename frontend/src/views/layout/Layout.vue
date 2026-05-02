<template>
  <div class="app-layout">
    <el-container class="layout-container">
      <el-aside width="220px" class="sidebar">
        <div class="logo">
          <h2>Diabetes Prediction</h2>
        </div>
        <el-menu
          :default-active="activeMenu"
          router
          class="sidebar-menu"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
        >
          <!-- Non-admin Home -->
          <el-menu-item v-if="!userStore.isAdmin" index="/home">
            <el-icon><House /></el-icon>
            <span>Home</span>
          </el-menu-item>
          
          <!-- User specific menu items -->
          <template v-if="userStore.isUser">
            <el-menu-item index="/prediction">
              <el-icon><DataAnalysis /></el-icon>
              <span>Prediction</span>
            </el-menu-item>
            
            <el-menu-item index="/doctor-binding">
              <el-icon><User /></el-icon>
              <span>Bind Doctor</span>
            </el-menu-item>
            
            <el-menu-item index="/message">
              <el-icon><Message /></el-icon>
              <span>Messages</span>
            </el-menu-item>
          </template>
          
          <!-- Doctor specific menu items -->
          <template v-if="userStore.isDoctor">
            <el-menu-item index="/doctor/patients">
              <el-icon><User /></el-icon>
              <span>My Patients</span>
            </el-menu-item>
            
            <el-menu-item index="/doctor/messages">
              <el-icon><ChatDotRound /></el-icon>
              <span>Patient Messages</span>
            </el-menu-item>
          </template>
          
          <!-- Admin specific menu items -->
          <template v-if="userStore.isAdmin">
            <el-menu-item index="/admin/doctors">
              <el-icon><User /></el-icon>
              <span>Doctor Management</span>
            </el-menu-item>
            
            <el-menu-item index="/admin/users">
              <el-icon><User /></el-icon>
              <span>User Management</span>
            </el-menu-item>

            <el-menu-item index="/admin/KnowledgeManage">
              <el-icon><User /></el-icon>
              <span>Knowledge Management</span>
            </el-menu-item>
          </template>
          
          <!-- Education menu items -->
          <el-sub-menu v-if="userStore.isUser" index="/education">
            <template #title>
              <el-icon><Document /></el-icon>
              <span>Education</span>
            </template>
            <el-menu-item index="/education/disease-info">Diabetes Info</el-menu-item>
            <el-menu-item index="/education/health-guide">Health Guide</el-menu-item>
            <el-menu-item index="/education/latest-research">Latest Research</el-menu-item>
            <el-menu-item index="/education/seasonal-tips">Health Tips</el-menu-item>
          </el-sub-menu>
          
          <!-- Non-admin Profile -->
          <el-menu-item v-if="!userStore.isAdmin" index="/profile">
            <el-icon><Setting /></el-icon>
            <span>Profile</span>
          </el-menu-item>

          <!-- Non-admin Password -->
          <el-menu-item v-if="!userStore.isAdmin" index="/user/change-password">
            <el-icon><Lock /></el-icon>
            <span>Change Password</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-container class="main-container">
        <el-header class="header" @click="handleFirstInteraction">
          <div class="header-left">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">Home</el-breadcrumb-item>
              <el-breadcrumb-item v-if="$route.meta.title">{{ $route.meta.title }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          
          <div class="header-right">
            <span class="welcome-text">Welcome, {{ userStore.user?.name || userStore.user?.username }}</span>
            <el-dropdown @command="handleCommand">
              <span class="user-dropdown">
                <el-avatar :size="32" :src="avatarUrl" />
                <i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item v-if="!userStore.isAdmin" command="profile">Profile</el-dropdown-item>
                  <el-dropdown-item command="logout">Logout</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <el-main class="main-content">
          <router-view />
        </el-main>
        
        <el-footer class="footer">
          © {{ currentYear }} Nino - Diabetes Prediction System
        </el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from '@/store/user';
// Import Element Plus Icons
import { 
  House, 
  DataAnalysis, 
  User, 
  Message, 
  ChatDotRound, 
  Document, 
  Setting,
  Lock
} from '@element-plus/icons-vue';

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();

// Current year
const currentYear = new Date().getFullYear();

// Default avatar
const avatarUrl = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png');

// Sidebar active item
const activeMenu = computed(() => route.path);

// Initialize AudioContext on first interaction (optional)
const hasInteracted = ref(false);
const handleFirstInteraction = () => {
  if (!hasInteracted.value) {
    hasInteracted.value = true;
    if (window.AudioContext || window.webkitAudioContext) {
      const AudioContext = window.AudioContext || window.webkitAudioContext;
      new AudioContext().resume();
    }
  }
};

// Handle dropdown commands
const handleCommand = (command) => {
  if (command === 'profile') {
    router.push('/profile');
  } else if (command === 'logout') {
    userStore.logout();
    router.push('/login');
  }
};
</script>

<style scoped>
.app-layout {
  height: 100vh;
  overflow: hidden;
}

/* 其余样式保持不变 */
.layout-container { height: 100%; }
.sidebar { background-color: #304156; color: #fff; height: 100%; overflow: hidden; }
.logo { height: 60px; display: flex; align-items: center; justify-content: center; border-bottom: 1px solid #1f2d3d; }
.logo h2 { color: #fff; margin: 0; font-size: 18px; font-weight: 600; }
.sidebar-menu { border-right: none; height: calc(100% - 60px); overflow-y: auto; overflow-x: hidden; }
.sidebar-menu::-webkit-scrollbar { width: 6px; }
.sidebar-menu::-webkit-scrollbar-thumb { background-color: rgba(255, 255, 255, 0.2); border-radius: 3px; }
.sidebar-menu::-webkit-scrollbar-track { background-color: transparent; }
.main-container { height: 100%; display: flex; flex-direction: column; }
.header { background-color: #fff; box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08); display: flex; align-items: center; justify-content: space-between; padding: 0 20px; height: 60px; flex-shrink: 0; }
.header-right { display: flex; align-items: center; }
.welcome-text { margin-right: 15px; font-size: 14px; color: #606266; }
.user-dropdown { cursor: pointer; display: flex; align-items: center; }
.main-content { background-color: #f0f2f5; padding: 20px; flex: 1; overflow-y: auto; }
.footer { text-align: center; background-color: #fff; color: #606266; font-size: 14px; padding: 0; height: 60px; line-height: 60px; border-top: 1px solid #e6e6e6; flex-shrink: 0; }
</style>
