<template>
  <div class="history-container">
    <el-card class="history-card">
      <template #header>
        <div class="card-header">
          <h2>历史预测记录</h2>
          <div>
            <el-button 
              type="primary" 
              plain 
              size="small" 
              @click="goToPrediction"
              :disabled="loading"
            >
              新建预测
            </el-button>
            <el-button 
              type="danger" 
              plain 
              size="small" 
              @click="confirmClearHistory"
              :disabled="loading || historyRecords.length === 0"
            >
              清空记录
            </el-button>
          </div>
        </div>
      </template>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>
      
      <div v-else-if="historyRecords.length === 0" class="empty-container">
        <el-empty description="暂无历史记录" :image-size="200">
          <el-button type="primary" @click="goToPrediction">开始预测</el-button>
        </el-empty>
      </div>
      
      <div v-else class="history-list">
        <el-timeline>
          <el-timeline-item
            v-for="record in historyRecords"
            :key="record.id"
            :timestamp="formatDate(record.date)"
            placement="top"
            :type="getTimelineItemType(record.result.riskLevel)"
          >
            <el-card class="history-item-card">
              <div class="history-item-header">
                <div class="history-item-title">
                  糖尿病风险评估结果
                </div>
                <div class="history-item-actions">
                  <el-tag :type="getRiskLevelType(record.result.riskLevel)">
                    {{ record.result.riskLevel }} 风险
                  </el-tag>
                  <span class="history-risk-percentage">{{ record.result.riskPercentage }}%</span>
                </div>
              </div>
              
              <div class="history-item-content">
                <div class="history-item-summary">
                  <el-descriptions :column="3" border size="small">
                    <el-descriptions-item label="年龄">{{ record.healthData.age }} 岁</el-descriptions-item>
                    <el-descriptions-item label="性别">{{ record.healthData.gender === 'male' ? '男' : '女' }}</el-descriptions-item>
                    <el-descriptions-item label="BMI">{{ record.healthData.bmi }}</el-descriptions-item>
                    
                    <el-descriptions-item label="血压">
                      {{ record.healthData.bloodPressure.systolic }}/{{ record.healthData.bloodPressure.diastolic }} mmHg
                    </el-descriptions-item>
                    <el-descriptions-item label="血糖">{{ record.healthData.bloodSugar }} mg/dL</el-descriptions-item>
                    <el-descriptions-item label="胆固醇">{{ record.healthData.cholesterol }} mg/dL</el-descriptions-item>
                  </el-descriptions>
                </div>
                
                <div class="history-item-footer">
                  <el-collapse>
                    <el-collapse-item title="健康建议">
                      <div class="recommendations">
                        <ul>
                          <li v-for="(recommendation, index) in record.result.recommendations" :key="index">
                            {{ recommendation }}
                          </li>
                        </ul>
                      </div>
                    </el-collapse-item>
                  </el-collapse>
                  
                  <div class="history-item-actions">
                    <el-button 
                      type="primary" 
                      link 
                      @click="viewDetails(record)"
                    >
                      查看详情
                    </el-button>
                    <el-button 
                      type="danger" 
                      link 
                      @click="confirmDeleteRecord(record.id)"
                    >
                      删除记录
                    </el-button>
                  </div>
                </div>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessageBox, ElMessage } from 'element-plus';
import { predictionService } from '@/api/prediction';

const router = useRouter();
const loading = ref(false);
const historyRecords = ref([]);

// Format date for display
const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
};

// Get risk level tag type
const getRiskLevelType = (level) => {
  switch (level) {
    case 'Low': return 'success';
    case 'Moderate': return 'warning';
    case 'High': return 'danger';
    case 'Very High': return 'danger';
    default: return 'info';
  }
};

// Get timeline item type based on risk level
const getTimelineItemType = (level) => {
  switch (level) {
    case 'Low': return 'success';
    case 'Moderate': return 'warning';
    case 'High': return 'danger';
    case 'Very High': return 'danger';
    default: return 'primary';
  }
};

// Go to prediction page
const goToPrediction = () => {
  router.push('/prediction');
};

// View prediction details
const viewDetails = (record) => {
  // Store the record data for the result page
  localStorage.setItem('current_prediction', JSON.stringify({
    healthData: record.healthData,
    result: record.result
  }));
  
  // Navigate to result page
  router.push('/prediction/result');
};

// Confirm delete record
const confirmDeleteRecord = (recordId) => {
  ElMessageBox.confirm(
    '确定要删除这条预测记录吗？',
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    deleteRecord(recordId);
  }).catch(() => {
    // User cancelled
  });
};

// Delete a record
const deleteRecord = (recordId) => {
  // Filter out the record to delete
  historyRecords.value = historyRecords.value.filter(record => record.id !== recordId);
  
  // Update localStorage
  localStorage.setItem('prediction_history', JSON.stringify(historyRecords.value));
  
  ElMessage.success('记录已删除');
};

// Confirm clear all history
const confirmClearHistory = () => {
  ElMessageBox.confirm(
    '确定要清空所有预测记录吗？此操作不可恢复。',
    '清空确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    clearHistory();
  }).catch(() => {
    // User cancelled
  });
};

// Clear all history
const clearHistory = () => {
  historyRecords.value = [];
  localStorage.removeItem('prediction_history');
  ElMessage.success('所有记录已清空');
};

// Load history records
const loadHistory = async () => {
  try {
    loading.value = true;
    const response = await predictionService.getPredictionHistory();
    historyRecords.value = response.data;
  } catch (error) {
    ElMessage.error('加载历史记录失败');
    console.error('Failed to load history:', error);
  } finally {
    loading.value = false;
  }
};

// On component mount
onMounted(() => {
  loadHistory();
});
</script>

<style scoped>
.history-container {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.history-card {
  width: 100%;
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

.loading-container, .empty-container {
  padding: 40px 0;
  text-align: center;
}

.history-list {
  margin-top: 20px;
}

.history-item-card {
  margin-bottom: 10px;
}

.history-item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.history-item-title {
  font-weight: 600;
  font-size: 16px;
}

.history-item-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.history-risk-percentage {
  font-weight: bold;
  font-size: 18px;
}

.history-item-content {
  margin-top: 10px;
}

.history-item-footer {
  margin-top: 15px;
  display: flex;
  flex-direction: column;
}

.recommendations {
  margin-top: 10px;
}

.recommendations ul {
  padding-left: 20px;
  margin: 0;
}

.recommendations li {
  margin-bottom: 5px;
}
</style> 