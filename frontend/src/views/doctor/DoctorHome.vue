<template>
  <div class="doctor-home">
    <div class="welcome-container">
      <div class="welcome-content">
        <h1 class="welcome-title">欢迎回来，{{ doctorName || '医生' }}</h1>
        <p class="welcome-subtitle">糖尿病预测系统</p>
        <div class="stats-container">
          <div class="stat-card">
            <div class="stat-icon">👨‍⚕️</div>
            <div class="stat-value">{{ stats?.patientCount || 0 }}</div>
            <div class="stat-label">当前患者</div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">📊</div>
            <div class="stat-value">{{ stats?.totalPredictions || 0 }}</div>
            <div class="stat-label">预测记录</div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">💬</div>
            <div class="stat-value">{{ stats?.unreadMessages || 0 }}</div>
            <div class="stat-label">未读消息</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { doctorService } from '@/api/doctor';

export default {
  name: 'DoctorHome',
  setup() {
    const stats = ref(null);
    const doctorName = ref('');

    const loadDashboardStats = async () => {
      try {
        const response = await doctorService.getDashboardStats();
        stats.value = response.data;
        
        // 从localStorage获取医生姓名
        const userStr = localStorage.getItem('user');
        if (userStr) {
          const user = JSON.parse(userStr);
          doctorName.value = user.name || '';
        }
      } catch (error) {
        console.error('Failed to load dashboard stats:', error);
      }
    };

    onMounted(() => {
      loadDashboardStats();
    });

    return {
      stats,
      doctorName
    };
  }
};
</script>

<style scoped>
.doctor-home {
  height: calc(100vh - 180px); /* 减去顶部导航栏的高度 */
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.welcome-container {
  max-width: 1200px;
  width: 100%;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  padding: 2rem 2rem 6rem 2rem;
  margin: 2rem;
  animation: fadeIn 1s ease-in-out;
}

.welcome-content {
  text-align: center;
}

.welcome-title {
  font-size: 3rem;
  color: #2c3e50;
  margin-bottom: 0.5rem;
  background: linear-gradient(45deg, #2c3e50, #3498db);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  animation: slideInDown 1s ease-out;
}

.welcome-subtitle {
  font-size: 1.5rem;
  color: #7f8c8d;
  margin-bottom: 2rem;
  animation: slideInUp 1s ease-out;
}

.stats-container {
  display: flex;
  justify-content: center;
  gap: 1.5rem;
  margin-top: 1rem;
}

.stat-card {
  background: white;
  padding: 1.5rem;
  border-radius: 15px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease;
  min-width: 180px;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-icon {
  font-size: 2rem;
  margin-bottom: 0.5rem;
}

.stat-value {
  font-size: 2rem;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 1rem;
  color: #7f8c8d;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideInDown {
  from {
    transform: translateY(-30px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

@keyframes slideInUp {
  from {
    transform: translateY(30px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

@media (max-width: 768px) {
  .welcome-title {
    font-size: 2rem;
  }
  
  .welcome-subtitle {
    font-size: 1.2rem;
  }
  
  .stats-container {
    flex-direction: column;
    align-items: center;
    gap: 1rem;
  }
  
  .stat-card {
    width: 100%;
    max-width: 250px;
    padding: 1rem;
  }
  
  .stat-icon {
    font-size: 1.5rem;
  }
  
  .stat-value {
    font-size: 1.5rem;
  }
}
</style> 