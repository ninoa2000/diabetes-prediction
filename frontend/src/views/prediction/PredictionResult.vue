<template>
  <div class="prediction-result-container">
    <template v-if="predictionData">
      <el-card class="result-card">
        <template #header>
          <div class="card-header">
            <h2>预测结果</h2>
            <div>
              <el-button 
                type="primary" 
                plain 
                size="small" 
                @click="goBack"
              >
                返回
              </el-button>
              <el-button 
                type="success" 
                plain 
                size="small" 
                @click="printResult"
              >
                打印结果
              </el-button>
            </div>
          </div>
        </template>
        
        <div class="prediction-main">
          <div class="risk-gauge">
            <div ref="gaugeChartRef" class="chart-container"></div>
            <div class="risk-level">
              <span>风险等级:</span>
              <el-tag :type="riskLevelType">{{ predictionData.result.riskLevel }}</el-tag>
            </div>
          </div>
          
          <div class="risk-details">
            <h3>风险分析</h3>
            <p>
              根据您提供的健康数据，您患糖尿病的风险指数为 <strong>{{ predictionData.result.riskPercentage }}%</strong>，
              属于<strong>{{ predictionData.result.riskLevel }}</strong>风险水平。
            </p>
            
            <h3>健康建议</h3>
            <el-alert
              v-for="(recommendation, index) in predictionData.result.recommendations"
              :key="index"
              type="info"
              :closable="false"
              show-icon
            >
              {{ recommendation }}
            </el-alert>
            
            <div class="contributing-factors" v-if="contributingFactors.length > 0">
              <h3>主要风险因素</h3>
              <el-tag 
                v-for="factor in contributingFactors" 
                :key="factor.name"
                :type="factor.type"
                effect="dark"
                class="factor-tag"
              >
                {{ factor.label }}
              </el-tag>
            </div>
          </div>
        </div>
        
        <div class="health-data-summary">
          <h3>健康数据摘要</h3>
          <el-descriptions border :column="3">
            <el-descriptions-item label="年龄">{{ predictionData.healthData.age }} 岁</el-descriptions-item>
            <el-descriptions-item label="性别">{{ predictionData.healthData.gender === 'male' ? '男' : '女' }}</el-descriptions-item>
            <el-descriptions-item label="BMI">{{ predictionData.healthData.bmi }}</el-descriptions-item>
            <el-descriptions-item label="血压">
              {{ predictionData.healthData.bloodPressure.systolic }}/{{ predictionData.healthData.bloodPressure.diastolic }} mmHg
            </el-descriptions-item>
            <el-descriptions-item label="血糖">{{ predictionData.healthData.bloodSugar }} mg/dL</el-descriptions-item>
            <el-descriptions-item label="胆固醇">{{ predictionData.healthData.cholesterol }} mg/dL</el-descriptions-item>
            <el-descriptions-item label="家族病史">{{ predictionData.healthData.familyHistory ? '有' : '无' }}</el-descriptions-item>
            <el-descriptions-item label="吸烟">{{ predictionData.healthData.smoking ? '是' : '否' }}</el-descriptions-item>
            <el-descriptions-item label="饮酒">{{ predictionData.healthData.alcohol ? '是' : '否' }}</el-descriptions-item>
            <el-descriptions-item label="规律运动">{{ predictionData.healthData.exercise ? '是' : '否' }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </el-card>
    </template>
    
    <el-empty 
      v-else 
      description="没有预测数据" 
      :image-size="200"
    >
      <el-button type="primary" @click="goToPrediction">开始预测</el-button>
    </el-empty>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import * as echarts from 'echarts';

const router = useRouter();
const gaugeChartRef = ref(null);
let gaugeChart = null;

// Get prediction data from localStorage
const predictionData = ref(null);

// Determine risk level tag type
const riskLevelType = computed(() => {
  if (!predictionData.value) return '';
  
  const level = predictionData.value.result.riskLevel;
  switch (level) {
    case 'Low': return 'success';
    case 'Moderate': return 'warning';
    case 'High': return 'danger';
    case 'Very High': return 'danger';
    default: return 'info';
  }
});

// Determine contributing factors based on health data
const contributingFactors = computed(() => {
  if (!predictionData.value) return [];
  
  const factors = [];
  const data = predictionData.value.healthData;
  
  // Check individual factors and add to list if they contribute to risk
  if (data.age > 50) {
    factors.push({ label: '年龄偏高', type: 'warning', name: 'age' });
  }
  
  if (data.bloodPressure.systolic > 130 || data.bloodPressure.diastolic > 80) {
    factors.push({ label: '血压偏高', type: 'danger', name: 'blood_pressure' });
  }
  
  if (data.bloodSugar > 100) {
    factors.push({ label: '血糖偏高', type: 'danger', name: 'blood_sugar' });
  }
  
  if (data.cholesterol > 200) {
    factors.push({ label: '胆固醇偏高', type: 'danger', name: 'cholesterol' });
  }
  
  if (data.bmi > 25) {
    factors.push({ label: 'BMI偏高', type: 'warning', name: 'bmi' });
  }
  
  if (data.familyHistory) {
    factors.push({ label: '家族病史', type: 'warning', name: 'family_history' });
  }
  
  if (data.smoking) {
    factors.push({ label: '吸烟', type: 'danger', name: 'smoking' });
  }
  
  if (data.alcohol) {
    factors.push({ label: '饮酒', type: 'warning', name: 'alcohol' });
  }
  
  if (!data.exercise) {
    factors.push({ label: '缺乏运动', type: 'warning', name: 'exercise' });
  }
  
  return factors;
});

// Initialize gauge chart
const initGaugeChart = () => {
  if (!predictionData.value || !gaugeChartRef.value) return;
  
  const riskPercentage = predictionData.value.result.riskPercentage;
  
  // Set up chart options
  const option = {
    series: [
      {
        type: 'gauge',
        progress: {
          show: true,
          width: 18
        },
        axisLine: {
          lineStyle: {
            width: 18,
            color: [
              [0.2, '#67C23A'],  // Low risk - green
              [0.5, '#E6A23C'],  // Moderate risk - yellow
              [0.8, '#F56C6C'],  // High risk - red
              [1, '#8B0000']     // Very high risk - dark red
            ]
          }
        },
        axisTick: { show: false },
        splitLine: { show: false },
        axisLabel: { show: false },
        anchor: { show: false },
        pointer: { show: false },
        title: {
          show: true,
          offsetCenter: [0, '30%'],
          fontSize: 20,
          fontWeight: 'bold',
          color: '#303133',
          formatter: riskPercentage + '%'
        },
        detail: {
          valueAnimation: true,
          fontSize: 16,
          color: '#303133',
          offsetCenter: [0, '60%'],
          formatter: '糖尿病风险指数'
        },
        data: [{ value: riskPercentage }]
      }
    ]
  };
  
  // Initialize chart
  gaugeChart = echarts.init(gaugeChartRef.value);
  gaugeChart.setOption(option);
  
  // Handle window resize
  window.addEventListener('resize', handleResize);
};

// Handle window resize for chart
const handleResize = () => {
  if (gaugeChart) {
    gaugeChart.resize();
  }
};

// Go back to prediction form
const goBack = () => {
  router.push('/prediction');
};

// Go to prediction if no data
const goToPrediction = () => {
  router.push('/prediction');
};

// Print result
const printResult = () => {
  window.print();
};

// On component mount
onMounted(() => {
  // Get prediction data from localStorage
  const storedData = localStorage.getItem('current_prediction');
  if (storedData) {
    predictionData.value = JSON.parse(storedData);
    
    // Initialize chart after DOM update
    setTimeout(() => {
      initGaugeChart();
    }, 100);
  }
});

// Clean up when component is unmounted
const onUnmounted = () => {
  if (gaugeChart) {
    gaugeChart.dispose();
    window.removeEventListener('resize', handleResize);
  }
};
</script>

<style scoped>
.prediction-result-container {
  max-width: 900px;
  margin: 0 auto;
}

.result-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
}

.prediction-main {
  display: flex;
  margin-bottom: 30px;
}

.risk-gauge {
  flex: 0 0 40%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.chart-container {
  width: 100%;
  height: 250px;
}

.risk-level {
  margin-top: 10px;
  font-size: 16px;
}

.risk-details {
  flex: 0 0 60%;
  padding-left: 20px;
}

.risk-details h3 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.risk-details p {
  line-height: 1.6;
  margin-bottom: 20px;
}

.contributing-factors {
  margin-top: 20px;
}

.factor-tag {
  margin-right: 10px;
  margin-bottom: 10px;
}

.health-data-summary {
  margin-top: 20px;
  border-top: 1px solid #ebeef5;
  padding-top: 20px;
}

.health-data-summary h3 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

@media print {
  .el-button {
    display: none;
  }
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .prediction-main {
    flex-direction: column;
  }
  
  .risk-gauge, .risk-details {
    flex: 1;
    width: 100%;
  }
  
  .risk-details {
    padding-left: 0;
    margin-top: 20px;
  }
}
</style> 