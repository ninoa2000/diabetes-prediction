<template>
  <div class="home-container">
    <!-- 欢迎卡片 -->
    <el-row :gutter="16">
      <el-col :span="24">
        <el-card class="welcome-card" shadow="hover">
          <div class="welcome-content">
            <div class="welcome-text">
              <h2>Welcome to the Diabetes Prediction System</h2>
              <p>Based on advanced machine learning algorithms, we predict the risk of diabetes by analyzing your health inspection data.</p>
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
            <h3>Fast Prediction</h3>
            <p>Just upload the inspection report to get prediction results quickly</p>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="feature-card" shadow="hover">
          <div class="feature-content">
            <el-icon class="feature-icon"><Document /></el-icon>
            <h3>Professional Suggestions</h3>
            <p>Provide professional health suggestions based on the prediction results</p>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="feature-card" shadow="hover">
          <div class="feature-content">
            <el-icon class="feature-icon"><Lock /></el-icon>
            <h3>Data Security</h3>
            <p>Strictly protect your personal health data</p>
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
              <h3>User Guide</h3>
            </div>
          </template>
          <el-steps :active="4" finish-status="success" align-center>
            <el-step title="Prepare Data" description="Prepare your health inspection report (Excel format)" />
            <el-step title="Upload Report" description="Click 'Prediction' menu to upload report" />
            <el-step title="View Results" description="System automatically analyzes and generates results" />
            <el-step title="Get Suggestions" description="Get professional health suggestions based on results" />
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