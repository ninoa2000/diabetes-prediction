<template>
  <div class="patient-list-container">
    <el-card class="patient-card">
      <template #header>
        <div class="card-header">
          <h2>我的患者</h2>
        </div>
      </template>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>
      
      <template v-else>
        <div v-if="patients.length === 0" class="empty-patients">
          <el-empty description="暂无患者" :image-size="200">
            <template #description>
              <p>当患者绑定您后，将会显示在这里</p>
            </template>
          </el-empty>
        </div>
        
        <div v-else class="patients-list">
          <el-table :data="patients" style="width: 100%">
            <el-table-column prop="name" label="患者姓名" />
           
            <el-table-column prop="phone" label="联系电话" />
            <el-table-column label="操作" width="250">
              <template #default="{ row }">
                <el-button type="primary" link @click="viewPatientDetail(row)">
                  查看详情
                </el-button>
                <el-button type="success" link @click="viewPatientCases(row)">
                  查看病例
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </template>
    </el-card>

    <!-- 患者详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="患者详情"
      width="500px"
    >
      <div v-if="selectedPatient" class="patient-detail">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="姓名">{{ selectedPatient.name }}</el-descriptions-item>
          
          <el-descriptions-item label="联系电话">{{ selectedPatient.phone }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ selectedPatient.email }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { doctorService } from '@/api/doctor';

const router = useRouter();
const loading = ref(false);
const patients = ref([]);
const detailDialogVisible = ref(false);
const selectedPatient = ref(null);

// View patient details
const viewPatientDetail = (patient) => {
  selectedPatient.value = patient;
  detailDialogVisible.value = true;
};

// View patient cases
const viewPatientCases = (patient) => {
  router.push(`/doctor/patients/${patient.id}/cases`);
};

// Load patients
const loadPatients = async () => {
  try {
    loading.value = true;
    const response = await doctorService.getPatients();
    if (response && Array.isArray(response)) {
      patients.value = response;
    } else {
      patients.value = [];
      ElMessage.warning('暂无患者数据');
    }
  } catch (error) {
    console.error('加载患者列表失败:', error);
    ElMessage.error(error.response?.data?.message || '加载患者列表失败');
  } finally {
    loading.value = false;
  }
};

// On component mount
onMounted(() => {
  loadPatients();
});
</script>

<style scoped>
.patient-list-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.patient-card {
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

.loading-container, .empty-patients {
  padding: 40px 0;
  text-align: center;
}

.patients-list {
  margin-top: 20px;
}

.patient-detail {
  padding: 20px 0;
}
</style> 