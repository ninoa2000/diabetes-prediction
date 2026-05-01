<template>
  <div class="prediction-result-container fade-in">
    <template v-if="predictionData">
      <el-card class="result-card premium-card" shadow="never">
        <template #header>
          <div class="card-header">
            <div class="title-group">
              <el-icon class="header-main-icon"><Monitor /></el-icon>
              <h2>Clinical Risk Assessment</h2>
            </div>
            <div class="action-group">
              <el-button 
                type="info" 
                plain 
                @click="goBack"
                class="hover-lift"
              >
                New Analysis
              </el-button>
              <el-button 
                type="primary" 
                @click="printResult"
                class="hover-lift"
              >
                Export Report
              </el-button>
            </div>
          </div>
        </template>
        
        <div class="prediction-main">
          <!-- Left: Visualization -->
          <div class="risk-visualization">
            <div class="gauge-wrapper">
              <div ref="gaugeChartRef" class="chart-container"></div>
              <div class="gauge-overlay">
                <span class="percentage-display" :style="{ color: dynamicColor }">
                  {{ predictionData.result.riskPercentage }}%
                </span>
                <span class="label-display">Probability</span>
              </div>
            </div>
            <div class="risk-badge-container">
              <div class="status-pill-large" :style="{ backgroundColor: dynamicLightColor, color: dynamicColor, borderColor: dynamicColor }">
                {{ riskStatus }}
              </div>
            </div>
          </div>
          
          <!-- Right: Summary -->
          <div class="risk-summary">
            <div class="algorithm-badge-row">
              <span class="label">Analytical Model:</span>
              <el-tag effect="dark" class="algo-pill-solid">
                {{ currentAlgorithm }}
              </el-tag>
            </div>

            <div class="report-text">
              <h3>Diagnostic Summary</h3>
              <p>
                Integrated health analysis identifies a risk index of 
                <span class="dynamic-value" :style="{ color: dynamicColor }">{{ predictionData.result.riskPercentage }}%</span>.
                This indicates a <span class="dynamic-value" :style="{ color: dynamicColor }">{{ riskStatus }}</span> profile based on current metrics.
              </p>
            </div>
            
            <div class="recommendations-section">
              <h3>Personalized Protocols</h3>
              <div class="recommendation-list">
                <div 
                  v-for="(rec, index) in predictionData.result.recommendations" 
                  :key="index" 
                  class="rec-item"
                >
                  <div class="rec-bullet" :style="{ backgroundColor: dynamicColor }"></div>
                  <span>{{ rec }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="disclaimer-section">
          <el-alert
            title="Medical Disclaimer"
            type="warning"
            :closable="false"
            show-icon
            class="soft-alert"
          >
            <p>This report is a <strong>rough estimate</strong> generated for research/testing purposes. It is not a clinical diagnosis. Please consult a medical professional for guidance.</p>
          </el-alert>
        </div>

        <el-divider />

        <div class="health-data-summary">
          <div class="summary-header">
            <h3>Clinical Metrics</h3>
            <span class="timestamp">Tested on: {{ reportDate }}</span>
          </div>
          <div class="data-grid">
            <div class="data-cell" v-for="(val, label) in profileDisplay" :key="label">
              <span class="d-label">{{ label }}</span>
              <span class="d-value">{{ val }}</span>
            </div>
          </div>
        </div>
      </el-card>
    </template>
    
    <el-empty v-else description="No Data Available">
      <el-button type="primary" @click="goBack">Start Analysis</el-button>
    </el-empty>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import * as echarts from 'echarts';
import { Monitor } from '@element-plus/icons-vue';

const router = useRouter();
const gaugeChartRef = ref(null);
let gaugeChart = null;
const predictionData = ref(null);
const reportDate = ref(new Date().toLocaleDateString());

const getVar = (name) => getComputedStyle(document.documentElement).getPropertyValue(name).trim() || '#000000';

const riskStatus = computed(() => {
  if (!predictionData.value) return 'NORMAL';
  const prob = predictionData.value.result.riskPercentage;
  if (prob < 30) return 'LOW RISK';
  if (prob < 70) return 'MODERATE RISK';
  return 'HIGH RISK';
});

const dynamicColor = computed(() => {
  if (!predictionData.value) return '#94a3b8';
  const prob = predictionData.value.result.riskPercentage;
  if (prob < 30) return getVar('--color-success');
  if (prob < 70) return getVar('--color-warning');
  return getVar('--color-danger');
});

const dynamicLightColor = computed(() => {
  if (!predictionData.value) return '#f8fafc';
  const prob = predictionData.value.result.riskPercentage;
  if (prob < 30) return getVar('--color-success-light');
  if (prob < 70) return getVar('--color-warning-light');
  return getVar('--color-danger-light');
});

const currentAlgorithm = computed(() => {
  const model = localStorage.getItem('modelType') || 'svm';
  const names = { 'svm': 'SVM', 'xgboost': 'XGBoost', 'random_forest': 'Random Forest', 'mlp': 'MLP', 'decision_tree': 'Decision Tree' };
  return names[model] || 'Standard';
});

const profileDisplay = computed(() => {
  if (!predictionData.value) return {};
  const d = predictionData.value.healthData;
  return {
    'Age': `${d.age} yrs`,
    'Gender': d.gender,
    'BMI': d.bmi,
    'BP (Systolic)': d.bloodPressure.systolic,
    'BP (Diastolic)': d.bloodPressure.diastolic,
    'Blood Sugar': d.bloodSugar
  };
});

const initGaugeChart = () => {
  if (!predictionData.value || !gaugeChartRef.value) return;
  gaugeChart = echarts.init(gaugeChartRef.value);
  gaugeChart.setOption({
    series: [{
      type: 'gauge', startAngle: 180, endAngle: 0, radius: '100%', center: ['50%', '85%'],
      progress: { show: true, width: 12, itemStyle: { color: dynamicColor.value } },
      axisLine: { lineStyle: { width: 12, color: [[1, '#f1f5f9']] } },
      axisTick: { show: false }, splitLine: { show: false }, axisLabel: { show: false }, anchor: { show: false }, pointer: { show: false }, detail: { show: false },
      data: [{ value: predictionData.value.result.riskPercentage }]
    }]
  });
};

const goBack = () => router.push('/prediction');
const printResult = () => window.print();

onMounted(() => {
  const stored = localStorage.getItem('current_prediction');
  if (stored) {
    predictionData.value = JSON.parse(stored);
    setTimeout(() => initGaugeChart(), 200);
  }
});
</script>

<style scoped>
.prediction-result-container { max-width: 900px; margin: 0 auto; padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.title-group { display: flex; align-items: center; gap: 10px; }
.header-main-icon { color: var(--color-primary); font-size: 20px; }
.card-header h2 { margin: 0; font-size: 18px; font-weight: 700; color: #1e293b; }

.prediction-main { display: grid; grid-template-columns: 320px 1fr; gap: 30px; padding: 20px 0; }
.risk-visualization { display: flex; flex-direction: column; align-items: center; justify-content: center; }
.gauge-wrapper { width: 100%; height: 160px; position: relative; }
.chart-container { width: 100%; height: 180px; }
.gauge-overlay { position: absolute; bottom: 0; left: 0; right: 0; text-align: center; }
.percentage-display { font-size: 36px; font-weight: 800; }
.label-display { font-size: 12px; color: #94a3b8; text-transform: uppercase; letter-spacing: 1px; }

.status-pill-large { margin-top: 20px; padding: 6px 20px; border-radius: 30px; font-weight: 800; font-size: 14px; border: 1px solid transparent; }

.risk-summary { display: flex; flex-direction: column; gap: 20px; }
.algorithm-badge-row { display: flex; align-items: center; gap: 8px; font-size: 13px; color: #64748b; }
.algo-pill-solid { background-color: var(--color-algorithm) !important; color: var(--color-algorithm-text) !important; font-weight: 800; border: none; font-size: 12px; padding: 4px 12px; border-radius: 8px; }

.report-text h3, .recommendations-section h3 { font-size: 14px; font-weight: 700; color: #1e293b; margin-bottom: 8px; text-transform: uppercase; letter-spacing: 0.5px; }
.report-text p { font-size: 15px; color: #475569; line-height: 1.6; }
.dynamic-value { font-weight: 700; }

.recommendation-list { display: flex; flex-direction: column; gap: 8px; }
.rec-item { display: flex; align-items: center; gap: 10px; font-size: 14px; color: #475569; }
.rec-bullet { width: 6px; height: 6px; border-radius: 50%; flex-shrink: 0; }

.disclaimer-section { margin-top: 10px; }
.soft-alert { border: none; background-color: #fffbeb; border-radius: 12px; }

.health-data-summary { margin-top: 20px; }
.summary-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
.summary-header h3 { font-size: 14px; font-weight: 700; margin: 0; color: #1e293b; }
.timestamp { font-size: 12px; color: #94a3b8; }

.data-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(130px, 1fr)); gap: 12px; }
.data-cell { background: #f8fafc; padding: 12px; border-radius: 10px; border: 1px solid #f1f5f9; }
.d-label { font-size: 10px; color: #94a3b8; text-transform: uppercase; font-weight: 700; display: block; margin-bottom: 2px; }
.d-value { font-size: 15px; font-weight: 600; color: #1e293b; }

@media (max-width: 768px) { .prediction-main { grid-template-columns: 1fr; } }
</style>