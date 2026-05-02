<template>
  <div class="dashboard-container">
    <el-row>
      <el-col :span="24">
        <h1 class="dashboard-title">Health Overview</h1>
      </el-col>
    </el-row>

    <!-- Health Metrics Cards -->
    <el-row :gutter="20">
      <el-col v-for="(metric, index) in healthMetrics" :key="index" :xs="24" :sm="12" :md="6">
        <el-card shadow="hover" :class="['metric-card', `metric-card-${metric.color}`]">
          <div class="metric-header">
            <h3>{{ metric.title }}</h3>
            <el-icon :class="`metric-icon-${metric.color}`">
              <component :is="getIconComponent(metric.icon)"></component>
            </el-icon>
          </div>
          <div class="metric-content">
            <div class="metric-value">{{ metric.value }}</div>
            <div class="metric-change">
              <span>{{ metric.change > 0 ? '↑' : '↓' }}</span>
              <span :class="metric.change > 0 ? 'text-danger' : 'text-success'">
                {{ Math.abs(metric.change) }}%
              </span>
              <span class="text-muted">vs Last Check</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <!-- Recent Prediction Results -->
      <el-col :xs="24" :md="12">
        <el-card shadow="hover" class="full-height-card">
          <template #header>
            <div class="card-header">
              <span>Recent Prediction Results</span>
              <el-button text type="primary" @click="$router.push('/prediction/history')">
                View All
              </el-button>
            </div>
          </template>
          <el-scrollbar height="350px">
            <el-empty v-if="recentPredictions.length === 0" description="No prediction records available">
              <el-button type="primary" @click="$router.push('/prediction')">
                Start Prediction
              </el-button>
            </el-empty>
            <div v-else>
              <div v-for="(prediction, i) in recentPredictions" :key="i" class="prediction-item">
                <div class="prediction-main">
                  <el-icon :class="getRiskIconClass(prediction.riskLevel)">
                    <component :is="getRiskIconComponent(prediction.riskLevel)"></component>
                  </el-icon>
                  <div class="prediction-info">
                    <div class="prediction-title">
                      {{ prediction.disease }}
                      <el-tag :type="getRiskTagType(prediction.riskLevel)" size="small">
                        {{ prediction.riskLevel }}
                      </el-tag>
                    </div>
                    <div class="prediction-date">{{ prediction.date }}</div>
                  </div>
                </div>
                <el-button 
                  text 
                  type="primary" 
                  size="small" 
                  @click="$router.push(`/prediction/detail/${prediction.id}`)"
                >
                  Details
                </el-button>
              </div>
            </div>
          </el-scrollbar>
        </el-card>
      </el-col>

      <!-- Doctor Information -->
      <el-col :xs="24" :md="12">
        <el-card shadow="hover" class="full-height-card">
          <template #header>
            <div class="card-header">
              <span>Doctor Information</span>
              <el-button text type="primary" @click="$router.push('/doctors')">
                Find Doctor
              </el-button>
            </div>
          </template>
          <div v-if="boundDoctor" class="doctor-info">
            <div class="doctor-profile">
              <el-avatar :size="64" src="https://cdn.vuetifyjs.com/images/john.jpg"></el-avatar>
              <div class="doctor-details">
                <h3>{{ boundDoctor.name }}</h3>
                <p>{{ boundDoctor.specializations.join(', ') }}</p>
                <p class="text-muted">{{ boundDoctor.email }} | {{ boundDoctor.phone }}</p>
              </div>
            </div>
            <el-divider></el-divider>
            <div class="doctor-actions">
              <el-button type="primary" text @click="$router.push('/message')">
                <el-icon><Message /></el-icon>Send Message
              </el-button>
              <el-button type="primary">
                <el-icon><Calendar /></el-icon>Book Appointment
              </el-button>
            </div>
          </div>
          <el-empty v-else description="You haven't bound a doctor yet">
            <el-button type="primary" @click="$router.push('/doctors')">
              Find Doctor
            </el-button>
          </el-empty>
        </el-card>
      </el-col>
    </el-row>

    <!-- Health News -->
    <el-row class="mt-20">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>Health Information</span>
              <el-button text type="primary" @click="$router.push('/education')">
                View More
              </el-button>
            </div>
          </template>
          <el-row :gutter="20">
            <el-col v-for="(article, i) in healthArticles" :key="i" :xs="24" :sm="12" :md="8">
              <el-card shadow="hover" class="article-card">
                <el-image :src="article.image" fit="cover" class="article-image"></el-image>
                <h3 class="article-title">{{ article.title }}</h3>
                <p class="article-summary">{{ article.summary }}</p>
                <div class="article-action">
                  <el-button text type="primary" @click="$router.push(article.link)">
                    Read More
                  </el-button>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { 
  ArrowUp, ArrowDown, Warning, CircleCheck, 
  Message, Calendar, More, View
} from '@element-plus/icons-vue';

const healthMetrics = reactive([
  {
    title: 'Blood Pressure',
    value: '125/85',
    icon: 'Monitor',
    color: 'blue',
    change: -2
  },
  {
    title: 'Blood Sugar',
    value: '5.7',
    icon: 'Histogram',
    color: 'green',
    change: -3.5
  },
  {
    title: 'Weight',
    value: '75kg',
    icon: 'ScaleToOriginal',
    color: 'amber',
    change: 1.2
  },
  {
    title: 'Exercise',
    value: '5.2k steps',
    icon: 'Bicycle',
    color: 'orange',
    change: 2.5
  }
]);

const recentPredictions = ref([
  {
    id: '1',
    disease: 'Coronary Heart Disease',
    riskLevel: 'Low Risk',
    date: '2025-04-20'
  },
  {
    id: '2',
    disease: 'Diabetes',
    riskLevel: 'Moderate Risk',
    date: '2025-04-15'
  },
  {
    id: '3',
    disease: 'Hypertension',
    riskLevel: 'Low Risk',
    date: '2025-04-10'
  }
]);

const boundDoctor = ref({
  name: 'Dr. Zhang',
  specializations: ['Cardiology', 'Internal Medicine'],
  email: 'doctor@example.com',
  phone: '13800138000'
});

const healthArticles = ref([
  {
    id: 1,
    title: 'Spring Diabetes Prevention Guide',
    summary: 'Spring is a peak season for diabetes. Learn how to prevent it effectively...',
    image: 'https://picsum.photos/id/237/400/200',
    link: '/education/seasonal-tips'
  },
  {
    id: 2,
    title: 'Diet and Cardiovascular Health',
    summary: 'Proper eating habits are crucial for cardiovascular health...',
    image: 'https://picsum.photos/id/238/400/200',
    link: '/education/health-guide'
  },
  {
    id: 3,
    title: 'Latest Research: Exercise Reduces Diabetes Risk',
    summary: 'Recent studies show that moderate weekly exercise can significantly lower risk...',
    image: 'https://picsum.photos/id/239/400/200',
    link: '/education/latest-research'
  }
]);

// Helper functions
const getIconComponent = (iconName) => {
  return iconName;
};

const getRiskIconComponent = (riskLevel) => {
  if (riskLevel === 'High Risk') return 'Warning';
  if (riskLevel === 'Moderate Risk') return 'Warning';
  return 'CircleCheck';
};

const getRiskIconClass = (riskLevel) => {
  if (riskLevel === 'High Risk') return 'text-danger';
  if (riskLevel === 'Moderate Risk') return 'text-warning';
  return 'text-success';
};

const getRiskTagType = (riskLevel) => {
  if (riskLevel === 'High Risk') return 'danger';
  if (riskLevel === 'Moderate Risk') return 'warning';
  return 'success';
};
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.dashboard-title {
  font-size: 24px;
  margin-bottom: 24px;
  font-weight: 600;
}

.mt-20 {
  margin-top: 20px;
}

.full-height-card {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.metric-card {
  height: 100%;
  border-radius: 8px;
}

.metric-card-blue {
  border-left: 4px solid var(--el-color-primary);
}

.metric-card-green {
  border-left: 4px solid var(--el-color-success);
}

.metric-card-amber {
  border-left: 4px solid var(--el-color-warning);
}

.metric-card-orange {
  border-left: 4px solid var(--el-color-danger);
}

.metric-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.metric-icon-blue {
  color: var(--el-color-primary);
  font-size: 24px;
}

.metric-icon-green {
  color: var(--el-color-success);
  font-size: 24px;
}

.metric-icon-amber {
  color: var(--el-color-warning);
  font-size: 24px;
}

.metric-icon-orange {
  color: var(--el-color-danger);
  font-size: 24px;
}

.metric-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 5px;
}

.metric-change {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 14px;
}

.text-danger {
  color: var(--el-color-danger);
}

.text-success {
  color: var(--el-color-success);
}

.text-warning {
  color: var(--el-color-warning);
}

.text-muted {
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

.prediction-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.prediction-main {
  display: flex;
  align-items: center;
  gap: 10px;
}

.prediction-info {
  display: flex;
  flex-direction: column;
}

.prediction-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}

.prediction-date {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

.doctor-info {
  padding: 10px 0;
}

.doctor-profile {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 15px;
}

.doctor-details h3 {
  margin: 0 0 5px 0;
  font-size: 18px;
}

.doctor-details p {
  margin: 0 0 5px 0;
  font-size: 14px;
}

.doctor-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 15px;
}

.article-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.article-image {
  height: 200px;
  object-fit: cover;
  margin-bottom: 10px;
}

.article-title {
  font-size: 16px;
  margin: 10px 0;
}

.article-summary {
  flex-grow: 1;
  font-size: 14px;
  color: var(--el-text-color-regular);
  margin-bottom: 15px;
}

.article-action {
  display: flex;
  justify-content: flex-end;
}
</style>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.dashboard-title {
  font-size: 24px;
  margin-bottom: 24px;
  font-weight: 600;
}

.mt-20 {
  margin-top: 20px;
}

.full-height-card {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.metric-card {
  height: 100%;
  border-radius: 8px;
}

.metric-card-blue {
  border-left: 4px solid var(--el-color-primary);
}

.metric-card-green {
  border-left: 4px solid var(--el-color-success);
}

.metric-card-amber {
  border-left: 4px solid var(--el-color-warning);
}

.metric-card-orange {
  border-left: 4px solid var(--el-color-danger);
}

.metric-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.metric-icon-blue {
  color: var(--el-color-primary);
  font-size: 24px;
}

.metric-icon-green {
  color: var(--el-color-success);
  font-size: 24px;
}

.metric-icon-amber {
  color: var(--el-color-warning);
  font-size: 24px;
}

.metric-icon-orange {
  color: var(--el-color-danger);
  font-size: 24px;
}

.metric-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 5px;
}

.metric-change {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 14px;
}

.text-danger {
  color: var(--el-color-danger);
}

.text-success {
  color: var(--el-color-success);
}

.text-warning {
  color: var(--el-color-warning);
}

.text-muted {
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

.prediction-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.prediction-main {
  display: flex;
  align-items: center;
  gap: 10px;
}

.prediction-info {
  display: flex;
  flex-direction: column;
}

.prediction-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}

.prediction-date {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

.doctor-info {
  padding: 10px 0;
}

.doctor-profile {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 15px;
}

.doctor-details h3 {
  margin: 0 0 5px 0;
  font-size: 18px;
}

.doctor-details p {
  margin: 0 0 5px 0;
  font-size: 14px;
}

.doctor-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 15px;
}

.article-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.article-image {
  height: 200px;
  object-fit: cover;
  margin-bottom: 10px;
}

.article-title {
  font-size: 16px;
  margin: 10px 0;
}

.article-summary {
  flex-grow: 1;
  font-size: 14px;
  color: var(--el-text-color-regular);
  margin-bottom: 15px;
}

.article-action {
  display: flex;
  justify-content: flex-end;
}
</style> 