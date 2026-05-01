<template>
  <div class="home-container fade-in">
    <!-- Hero Section -->
    <el-row :gutter="24">
      <el-col :span="24">
        <el-card class="hero-card" shadow="never">
          <div class="hero-content">
            <div class="hero-text">
              <span class="hero-badge">AI-Powered Healthcare</span>
              <h1>Diabetes Risk Prediction System</h1>
              <p>Leveraging advanced machine learning to provide accurate, data-driven insights into your metabolic health.</p>
              <div class="hero-actions">
                <el-button type="primary" size="large" @click="goTo('/prediction')" class="hover-lift">
                  Start Analysis <el-icon class="el-icon--right"><ArrowRight /></el-icon>
                </el-button>
                <el-button size="large" @click="goTo('/education/health-guide')" class="hover-lift">
                  Learn More
                </el-button>
              </div>
            </div>
            <div class="hero-visual">
              <div class="abstract-shape"></div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Core Features -->
    <el-row :gutter="24" class="feature-section">
      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="feature-card premium-hover" @click="goTo('/prediction')">
          <div class="feature-content">
            <div class="icon-wrapper clinical">
              <el-icon><Timer /></el-icon>
            </div>
            <h3>Instant Prediction</h3>
            <p>Upload your clinical metrics and receive a comprehensive risk analysis in seconds.</p>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="feature-card premium-hover" @click="goTo('/education/health-guide')">
          <div class="feature-content">
            <div class="icon-wrapper wellness">
              <el-icon><Document /></el-icon>
            </div>
            <h3>Health Intelligence</h3>
            <p>Access evidence-based protocols and personalized suggestions for diabetes prevention.</p>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="feature-card premium-hover" @click="goTo('/profile')">
          <div class="feature-content">
            <div class="icon-wrapper security">
              <el-icon><Lock /></el-icon>
            </div>
            <h3>Privacy First</h3>
            <p>Your health data is encrypted and handled according to clinical security standards.</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Operational Flow -->
    <el-row :gutter="24" class="guide-section">
      <el-col :span="24">
        <el-card class="guide-card premium-card" shadow="never">
          <template #header>
            <div class="section-header">
              <h3>Analytical Workflow</h3>
              <p>Four steps to a comprehensive health assessment</p>
            </div>
          </template>
          <div class="steps-wrapper">
            <el-steps :active="4" finish-status="success" align-center>
              <el-step title="Data Input" description="Enter metrics manually or via Excel" />
              <el-step title="Model Selection" description="Choose from 5 specialized AI algorithms" />
              <el-step title="AI Processing" description="Neural patterns identify risk correlations" />
              <el-step title="Clinical Report" description="Receive detailed diagnostic suggestions" />
            </el-steps>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { Timer, Document, Lock, ArrowRight } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const goTo = (path) => {
  router.push(path)
}
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.hero-card {
  border: none;
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-primary-dark) 100%);
  border-radius: 24px;
  color: white;
  margin-bottom: 40px;
  overflow: hidden;
  position: relative;
}

.hero-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  padding: 60px;
  align-items: center;
  position: relative;
  z-index: 2;
}

.hero-text h1 {
  font-size: 42px;
  font-weight: 800;
  margin: 16px 0;
  line-height: 1.2;
}

.hero-text p {
  font-size: 18px;
  opacity: 0.9;
  margin-bottom: 32px;
  max-width: 500px;
  line-height: 1.6;
}

.hero-badge {
  background: rgba(255, 255, 255, 0.15);
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.hero-actions {
  display: flex;
  gap: 16px;
}

.hero-visual {
  display: flex;
  justify-content: flex-end;
}

.abstract-shape {
  width: 300px;
  height: 300px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 30% 70% 70% 30% / 30% 30% 70% 70%;
  animation: morph 8s ease-in-out infinite;
}

@keyframes morph {
  0% { border-radius: 30% 70% 70% 30% / 30% 30% 70% 70%; }
  50% { border-radius: 50% 50% 20% 80% / 25% 80% 20% 75%; }
  100% { border-radius: 30% 70% 70% 30% / 30% 30% 70% 70%; }
}

.feature-section {
  margin-bottom: 40px;
}

.feature-card {
  height: 100%;
  border-radius: var(--radius-lg);
  border: 1px solid var(--color-border);
  cursor: pointer;
  padding: 10px;
}

.feature-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 20px;
}

.icon-wrapper {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  margin-bottom: 20px;
}

.icon-wrapper.clinical { background: var(--color-primary-light); color: var(--color-primary); }
.icon-wrapper.wellness { background: var(--color-success-light); color: var(--color-success); }
.icon-wrapper.security { background: var(--color-warning-light); color: var(--color-warning); }

.feature-content h3 {
  font-size: 20px;
  font-weight: 700;
  color: var(--color-text-main);
  margin-bottom: 12px;
}

.feature-content p {
  color: var(--color-text-muted);
  line-height: 1.6;
  font-size: 15px;
}

.section-header h3 {
  font-size: 20px;
  font-weight: 700;
  margin: 0 0 8px;
}

.section-header p {
  color: var(--color-text-light);
  margin: 0;
}

.steps-wrapper {
  padding: 40px 0;
}

.premium-hover {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.premium-hover:hover {
  transform: translateY(-8px);
  box-shadow: var(--shadow-lg);
  border-color: var(--color-primary);
}

.hover-lift {
  transition: transform 0.2s;
}

.hover-lift:hover {
  transform: translateY(-2px);
}

@media (max-width: 992px) {
  .hero-content { grid-template-columns: 1fr; padding: 40px; text-align: center; }
  .hero-text p { margin-left: auto; margin-right: auto; }
  .hero-actions { justify-content: center; }
  .hero-visual { display: none; }
}
</style>