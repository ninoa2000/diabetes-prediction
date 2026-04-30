<template>
  <div class="cases-container">
    <el-card class="cases-card">
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-button @click="goBack" icon="el-icon-back" size="small">返回</el-button>
            <h2>患者病历列表</h2>
          </div>
          <div class="patient-info" v-if="patientInfo">
            <span>患者：{{ patientInfo.name }}</span>
          </div>
        </div>
      </template>

      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>

      <template v-else>
        <div v-if="!cases.length" class="empty-container">
          <el-empty description="暂无病历记录" />
        </div>

        <el-table v-else :data="cases" style="width: 100%">
          <el-table-column label="上传时间" width="180">
            <template #default="scope">
              {{ formatDate(scope.row.createdAt) }}
            </template>
          </el-table-column>
          <el-table-column label="预测结果" prop="disease" />
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-button
                type="primary"
                size="small"
                @click="viewDetails(scope.row)"
              >
                查看详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </template>
    </el-card>

    <!-- 病历详情对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="病历详情"
      width="50%"
      :before-close="handleDialogClose"
    >
      <div v-if="selectedCase" class="case-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="上传时间">{{ formatDate(selectedCase.createdAt) }}</el-descriptions-item>
          <el-descriptions-item label="预测结果">{{ selectedCase.disease || '暂无' }}</el-descriptions-item>
        </el-descriptions>

        <div class="health-data-section">
          <h3>健康数据</h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item 
              v-for="(value, key) in selectedCase.healthData" 
              :key="key" 
              :label="key"
            >
              {{ value }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { doctorService } from '@/api/doctor';

const router = useRouter();
const route = useRoute();
const loading = ref(false);
const cases = ref([]);
const patientInfo = ref(null);
const dialogVisible = ref(false);
const selectedCase = ref(null);

// 加载病历列表
const loadCases = async () => {
  try {
    loading.value = true;
    const patientId = route.params.patientId;
    console.log('Loading cases for patientId:', patientId);
    
    // 获取患者信息
    const patientResponse = await doctorService.getPatientDetail(patientId);
    console.log('Patient detail response:', patientResponse);
    patientInfo.value = patientResponse.data;
    
    // 获取病历列表
    const response = await doctorService.getPatientCases(patientId);
    console.log('Patient cases response:', response);
    
    // 确保数据是数组
    let casesData = [];
    if (response && Array.isArray(response)) {
      casesData = response;
    } else if (response && response.data) {
      casesData = Array.isArray(response.data) ? response.data : [response.data];
    }
    
    console.log('Cases data to set:', casesData);
    
    // 使用解构赋值来确保响应式更新
    cases.value = [...casesData];
    console.log('Cases value after setting:', cases.value);
  } catch (error) {
    console.error('Failed to load cases:', error);
    ElMessage.error('加载病历列表失败');
    cases.value = [];
  } finally {
    loading.value = false;
  }
};

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return '暂无';
  try {
    const date = new Date(dateStr);
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  } catch (error) {
    console.error('Date formatting error:', error);
    return '暂无';
  }
};

// 查看病历详情
const viewDetails = (caseData) => {
  console.log('Viewing case details:', caseData);
  selectedCase.value = caseData;
  dialogVisible.value = true;
};

// 关闭对话框
const handleDialogClose = () => {
  dialogVisible.value = false;
  selectedCase.value = null;
};

// 返回患者列表
const goBack = () => {
  router.push('/doctor/patients');
};

onMounted(() => {
  loadCases();
});
</script>

<style scoped>
.cases-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.cases-card {
  width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.header-left h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
}

.patient-info {
  font-size: 16px;
  color: #606266;
}

.loading-container {
  padding: 40px 0;
  text-align: center;
}

.empty-container {
  padding: 40px 0;
  text-align: center;
}

.case-detail {
  padding: 20px 0;
}

.health-data-section {
  margin-top: 20px;
}

.health-data-section h3 {
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}
</style> 