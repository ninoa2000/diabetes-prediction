<template>
  <div class="dashboard-container">
    <el-row>
      <el-col :span="24">
        <h1 class="dashboard-title">健康概览</h1>
      </el-col>
    </el-row>

    <!-- 健康指标卡片 -->
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
              <span class="text-muted">较上次检测</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <!-- 最近预测结果 -->
      <el-col :xs="24" :md="12">
        <el-card shadow="hover" class="full-height-card">
          <template #header>
            <div class="card-header">
              <span>最近预测结果</span>
              <el-button text type="primary" @click="$router.push('/prediction/history')">
                查看全部
              </el-button>
            </div>
          </template>
          <el-scrollbar height="350px">
            <el-empty v-if="recentPredictions.length === 0" description="暂无预测记录">
              <el-button type="primary" @click="$router.push('/prediction')">
                开始预测
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
                  详情
                </el-button>
              </div>
            </div>
          </el-scrollbar>
        </el-card>
      </el-col>

      <!-- 医生信息 -->
      <el-col :xs="24" :md="12">
        <el-card shadow="hover" class="full-height-card">
          <template #header>
            <div class="card-header">
              <span>医生信息</span>
              <el-button text type="primary" @click="$router.push('/doctors')">
                查找医生
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
                <el-icon><Message /></el-icon>发送消息
              </el-button>
              <el-button type="primary">
                <el-icon><Calendar /></el-icon>预约咨询
              </el-button>
            </div>
          </div>
          <el-empty v-else description="您还未绑定医生">
            <el-button type="primary" @click="$router.push('/doctors')">
              寻找医生
            </el-button>
          </el-empty>
        </el-card>
      </el-col>
    </el-row>

    <!-- 健康资讯 -->
    <el-row class="mt-20">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>健康资讯</span>
              <el-button text type="primary" @click="$router.push('/education')">
                查看更多
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
                    阅读更多
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
    title: '血压',
    value: '125/85',
    icon: 'Monitor',
    color: 'blue',
    change: -2
  },
  {
    title: '血糖',
    value: '5.7',
    icon: 'Histogram',
    color: 'green',
    change: -3.5
  },
  {
    title: '体重',
    value: '75kg',
    icon: 'ScaleToOriginal',
    color: 'amber',
    change: 1.2
  },
  {
    title: '运动量',
    value: '5.2k步',
    icon: 'Bicycle',
    color: 'orange',
    change: 2.5
  }
]);

const recentPredictions = ref([
  {
    id: '1',
    disease: '冠心病',
    riskLevel: '低风险',
    date: '2025-04-20'
  },
  {
    id: '2',
    disease: '糖尿病',
    riskLevel: '中风险',
    date: '2025-04-15'
  },
  {
    id: '3',
    disease: '高血压',
    riskLevel: '低风险',
    date: '2025-04-10'
  }
]);

const boundDoctor = ref({
  name: '张医生',
  specializations: ['心血管科', '内科'],
  email: 'doctor@example.com',
  phone: '13800138000'
});

const healthArticles = ref([
  {
    id: 1,
    title: '春季糖尿病预防指南',
    summary: '春季是糖尿病高发季节，本文介绍了如何在春季做好预防工作...',
    image: 'https://picsum.photos/id/237/400/200',
    link: '/education/seasonal-tips'
  },
  {
    id: 2,
    title: '饮食与心脑血管健康',
    summary: '合理的饮食习惯对心脑血管健康至关重要...',
    image: 'https://picsum.photos/id/238/400/200',
    link: '/education/health-guide'
  },
  {
    id: 3,
    title: '最新研究：适量运动降低糖尿病风险',
    summary: '最新研究表明，每周进行适量运动可显著降低糖尿病风险...',
    image: 'https://picsum.photos/id/239/400/200',
    link: '/education/latest-research'
  }
]);

// 辅助函数
const getIconComponent = (iconName) => {
  // 直接返回图标名称，因为所有Element Plus图标已经全局注册
  return iconName;
};

const getRiskIconComponent = (riskLevel) => {
  if (riskLevel === '高风险') return 'Warning';
  if (riskLevel === '中风险') return 'Warning';
  return 'CircleCheck';
};

const getRiskIconClass = (riskLevel) => {
  if (riskLevel === '高风险') return 'text-danger';
  if (riskLevel === '中风险') return 'text-warning';
  return 'text-success';
};

const getRiskTagType = (riskLevel) => {
  if (riskLevel === '高风险') return 'danger';
  if (riskLevel === '中风险') return 'warning';
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