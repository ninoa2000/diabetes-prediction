<template>
  <div class="disease-container">
    <!-- Hero Section -->
    <div class="hero-section">
      <div class="hero-content">
        <h1>Chronic Disease Encyclopedia</h1>
        <p>Comprehensive knowledge to help you understand, prevent, and manage major health conditions.</p>
      </div>
      <div class="hero-bg-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="main-content" v-if="diseases.length > 0">
      <el-row :gutter="40">
        <!-- Sidebar Navigation -->
        <el-col :xs="24" :md="7" :lg="6">
          <div class="sidebar">
            <h3 class="sidebar-title">Medical Conditions</h3>
            <ul class="disease-menu">
              <li 
                v-for="(item, index) in diseases" 
                :key="item.id"
                :class="['menu-item', { active: activeIndex === index }]"
                @click="activeIndex = index"
              >
                <div class="menu-item-content">
                  <span class="menu-item-text">{{ item.name }}</span>
                  <el-icon class="menu-item-icon"><ArrowRight /></el-icon>
                </div>
              </li>
            </ul>
          </div>
        </el-col>

        <!-- Details Section -->
        <el-col :xs="24" :md="17" :lg="18">
          <transition name="fade-slide" mode="out-in">
            <div class="details-section" :key="activeIndex" v-if="activeDisease">
              
              <div class="details-header">
                <h2 class="disease-name">{{ activeDisease.name }}</h2>
                <div class="header-line"></div>
              </div>

              <!-- Overview Block -->
              <div class="info-card overview-card">
                <div class="card-icon-wrapper bg-primary-light">
                  <el-icon class="text-primary"><InfoFilled /></el-icon>
                </div>
                <div class="card-content">
                  <h3>Overview</h3>
                  <p>{{ activeDisease.description }}</p>
                </div>
              </div>

              <el-row :gutter="24">
                <!-- Symptoms Block -->
                <el-col :xs="24" :lg="12">
                  <div class="info-card h-100">
                    <div class="card-icon-wrapper bg-warning-light">
                      <el-icon class="text-warning"><WarningFilled /></el-icon>
                    </div>
                    <div class="card-content">
                      <h3>Common Symptoms</h3>
                      <div class="tag-cloud">
                        <el-tag 
                          v-for="(sym, idx) in formatList(activeDisease.symptoms)" 
                          :key="idx"
                          effect="light"
                          type="warning"
                          class="symptom-tag"
                          round
                        >
                          {{ sym }}
                        </el-tag>
                      </div>
                    </div>
                  </div>
                </el-col>

                <!-- Causes Block -->
                <el-col :xs="24" :lg="12">
                  <div class="info-card h-100">
                    <div class="card-icon-wrapper bg-danger-light">
                      <el-icon class="text-danger"><Aim /></el-icon>
                    </div>
                    <div class="card-content">
                      <h3>Underlying Causes</h3>
                      <p>{{ activeDisease.causes }}</p>
                    </div>
                  </div>
                </el-col>
              </el-row>

              <el-row :gutter="24" class="mt-24">
                <!-- Prevention Block -->
                <el-col :xs="24" :lg="12">
                  <div class="info-card h-100 prevention-card">
                    <div class="card-icon-wrapper bg-success-light">
                      <el-icon class="text-success"><CircleCheckFilled /></el-icon>
                    </div>
                    <div class="card-content">
                      <h3>Prevention Strategies</h3>
                      <p>{{ activeDisease.prevention }}</p>
                    </div>
                  </div>
                </el-col>

                <!-- Treatment Block -->
                <el-col :xs="24" :lg="12">
                  <div class="info-card h-100 treatment-card">
                    <div class="card-icon-wrapper bg-info-light">
                      <el-icon class="text-info"><FirstAidKit /></el-icon>
                    </div>
                    <div class="card-content">
                      <h3>Treatment Options</h3>
                      <p>{{ activeDisease.treatment }}</p>
                    </div>
                  </div>
                </el-col>
              </el-row>

            </div>
          </transition>
        </el-col>
      </el-row>
    </div>
    
    <div v-else class="loading-state">
      <el-skeleton :rows="15" animated />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import { 
  ArrowRight, 
  InfoFilled, 
  WarningFilled, 
  Aim, 
  CircleCheckFilled, 
  FirstAidKit 
} from '@element-plus/icons-vue';

const diseases = ref([]);
const activeIndex = ref(0);

const activeDisease = computed(() => {
  if (diseases.value.length === 0) return null;
  return diseases.value[activeIndex.value];
});

// Convert comma/semicolon separated strings to arrays
function formatList(val) {
  if (!val) return [];
  return Array.isArray(val)
    ? val
    : String(val)
        .split(/[，、,；;]+/)
        .filter((s) => s.trim());
}

async function loadDiseases() {
  try {
    const res = await axios.get('/api/chronic');
    diseases.value = Array.isArray(res.data) ? res.data : [];
  } catch (e) {
    console.error(e);
    ElMessage.error('Failed to load disease data');
  }
}

onMounted(loadDiseases);
</script>

<style scoped>
/* Main Container Layout */
.disease-container {
  min-height: 100vh;
  background-color: #f8fafc;
  padding-bottom: 60px;
}

/* Hero Section */
.hero-section {
  position: relative;
  background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%);
  padding: 80px 40px;
  color: white;
  text-align: center;
  overflow: hidden;
  border-bottom-left-radius: 40px;
  border-bottom-right-radius: 40px;
  box-shadow: 0 10px 30px rgba(59, 130, 246, 0.2);
  margin-bottom: 50px;
}

.hero-content {
  position: relative;
  z-index: 2;
  max-width: 800px;
  margin: 0 auto;
}

.hero-content h1 {
  font-size: 3rem;
  font-weight: 800;
  margin-bottom: 20px;
  letter-spacing: -0.5px;
  text-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.hero-content p {
  font-size: 1.2rem;
  font-weight: 300;
  line-height: 1.6;
  opacity: 0.9;
}

/* Abstract Background Shapes for Hero */
.hero-bg-shapes .shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(5px);
}

.shape-1 {
  width: 300px;
  height: 300px;
  top: -50px;
  left: -50px;
}

.shape-2 {
  width: 400px;
  height: 400px;
  bottom: -100px;
  right: -100px;
}

/* Content Layout */
.main-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 40px;
}

/* Sidebar Menu */
.sidebar {
  background: white;
  border-radius: 20px;
  padding: 30px 20px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.03);
  position: sticky;
  top: 40px;
}

.sidebar-title {
  font-size: 1.2rem;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: 1px;
  margin-bottom: 20px;
  padding-left: 15px;
  font-weight: 700;
}

.disease-menu {
  list-style: none;
  padding: 0;
  margin: 0;
}

.menu-item {
  margin-bottom: 10px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;
}

.menu-item-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  font-size: 1.1rem;
  font-weight: 500;
  color: #334155;
  background: transparent;
}

.menu-item:hover .menu-item-content {
  background: #f1f5f9;
  transform: translateX(5px);
}

.menu-item.active {
  background: linear-gradient(90deg, #eff6ff 0%, #dbeafe 100%);
  border-left: 4px solid #3b82f6;
}

.menu-item.active .menu-item-content {
  color: #1d4ed8;
  font-weight: 700;
}

.menu-item-icon {
  opacity: 0;
  transition: opacity 0.3s ease, transform 0.3s ease;
  transform: translateX(-10px);
}

.menu-item.active .menu-item-icon {
  opacity: 1;
  transform: translateX(0);
}

/* Details Section */
.details-section {
  padding-left: 20px;
}

.details-header {
  margin-bottom: 30px;
}

.disease-name {
  font-size: 2.5rem;
  font-weight: 800;
  color: #0f172a;
  margin-bottom: 15px;
}

.header-line {
  height: 4px;
  width: 60px;
  background: #3b82f6;
  border-radius: 2px;
}

/* Info Cards */
.info-card {
  background: white;
  border-radius: 20px;
  padding: 30px;
  margin-bottom: 24px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.04);
  display: flex;
  gap: 20px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  border: 1px solid rgba(0,0,0,0.02);
}

.info-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 35px rgba(0,0,0,0.08);
}

.h-100 {
  height: calc(100% - 24px); /* Account for margin */
}

.mt-24 {
  margin-top: 24px;
}

.card-icon-wrapper {
  flex-shrink: 0;
  width: 60px;
  height: 60px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
}

.card-content {
  flex: 1;
}

.card-content h3 {
  font-size: 1.4rem;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 15px;
  margin-top: 5px;
}

.card-content p {
  color: #475569;
  line-height: 1.8;
  font-size: 1.05rem;
  margin: 0;
}

/* Colors & Badges */
.bg-primary-light { background: #dbeafe; }
.text-primary { color: #2563eb; }

.bg-warning-light { background: #fef3c7; }
.text-warning { color: #d97706; }

.bg-danger-light { background: #fee2e2; }
.text-danger { color: #dc2626; }

.bg-success-light { background: #dcfce3; }
.text-success { color: #16a34a; }

.bg-info-light { background: #e0e7ff; }
.text-info { color: #4f46e5; }

.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.symptom-tag {
  padding: 8px 16px;
  font-size: 0.95rem;
  height: auto;
  border: none;
  font-weight: 500;
}

/* Animations */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateY(20px) scale(0.98);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-20px) scale(0.98);
}

.loading-state {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px;
  background: white;
  border-radius: 20px;
}

/* Responsive */
@media (max-width: 768px) {
  .hero-section {
    padding: 60px 20px;
    border-radius: 0 0 20px 20px;
  }
  .hero-content h1 {
    font-size: 2.2rem;
  }
  .main-content {
    padding: 0 15px;
  }
  .details-section {
    padding-left: 0;
    margin-top: 30px;
  }
  .info-card {
    flex-direction: column;
    padding: 20px;
  }
  .card-icon-wrapper {
    margin-bottom: 15px;
  }
}
</style>
