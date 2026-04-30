<template>
  <div class="home-container">
    <!-- 欢迎卡片 -->
    <el-row :gutter="16">
      <el-col :span="24">
        <el-card class="welcome-card" shadow="hover">
          <div class="welcome-content">
            <div class="welcome-text">
              <h2>欢迎使用糖尿病预测系统</h2>
              <p>基于先进的机器学习算法，通过分析您的健康检查数据，预测糖尿病的风险。</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能卡片 -->
    <el-row :gutter="16" class="feature-cards">
      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="feature-card" shadow="hover">
          <div class="feature-content">
            <el-icon class="feature-icon"><Timer /></el-icon>
            <h3>快速预测</h3>
            <p>只需上传检查报告，即可快速获得预测结果</p>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="feature-card" shadow="hover">
          <div class="feature-content">
            <el-icon class="feature-icon"><Document /></el-icon>
            <h3>专业建议</h3>
            <p>根据预测结果，提供专业的健康建议</p>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="feature-card" shadow="hover">
          <div class="feature-content">
            <el-icon class="feature-icon"><Lock /></el-icon>
            <h3>数据安全</h3>
            <p>严格保护您的个人健康数据</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 使用指南 -->
    <el-row :gutter="16" class="guide-section">
      <el-col :span="24">
        <el-card class="guide-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <h3>使用指南</h3>
            </div>
          </template>
          <el-steps :active="4" finish-status="success" align-center>
            <el-step title="准备数据" description="准备您的健康检查报告（Excel格式）" />
            <el-step title="上传报告" description="点击'健康预测'菜单上传检查报告" />
            <el-step title="查看结果" description="系统自动分析并生成预测结果" />
            <el-step title="获取建议" description="根据预测结果获取专业的健康建议" />
          </el-steps>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { Timer, Document, Lock, Picture } from '@element-plus/icons-vue'
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 10px auto;
  padding: 0 10px;
}

.welcome-card {
  margin-bottom: 16px;
  transition: all 0.3s;
  border-radius: 8px;
  overflow: hidden;
}

.welcome-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.welcome-content {
  padding: 16px;
}

.welcome-text {
  text-align: center;
}

.welcome-text h2 {
  margin: 0 0 12px;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.welcome-text p {
  margin: 0;
  font-size: 14px;
  line-height: 1.5;
  color: #606266;
}

.feature-cards {
  margin-bottom: 16px;
}

.feature-card {
  height: 100%;
  transition: all 0.3s;
  border-radius: 8px;
  overflow: hidden;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.feature-content {
  padding: 20px;
  text-align: center;
}

.feature-icon {
  font-size: 36px;
  color: var(--el-color-primary);
  margin-bottom: 12px;
}

.feature-content h3 {
  margin: 0 0 8px;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.feature-content p {
  margin: 0;
  font-size: 14px;
  line-height: 1.5;
  color: #606266;
}

.guide-section {
  margin-bottom: 16px;
}

.guide-card {
  border-radius: 8px;
  overflow: hidden;
}

.card-header {
  padding: 12px 16px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.card-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

:deep(.el-steps) {
  padding: 20px;
}

:deep(.el-step__title) {
  font-size: 14px;
  font-weight: 500;
}

:deep(.el-step__description) {
  font-size: 12px;
  color: #606266;
}

@media screen and (max-width: 768px) {
  .welcome-content {
    flex-direction: column;
    gap: 16px;
  }
  
  .welcome-image {
    max-width: 100%;
    height: 160px;
  }
  
  .feature-cards .el-col {
    margin-bottom: 16px;
  }
}
</style> 