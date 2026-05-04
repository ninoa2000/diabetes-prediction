<template>
  <div class="patient-list-container">
    <el-card class="patient-card">
      <template #header>
        <div class="card-header">
          <h2>My Patients</h2>
        </div>
      </template>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>
      
      <template v-else>
        <div v-if="patients.length === 0" class="empty-patients">
          <el-empty description="No patients yet" :image-size="200">
            <template #description>
              <p>When patients bind to you, they will appear here.</p>
            </template>
          </el-empty>
        </div>
        
        <div v-else class="patients-list">
          <el-table :data="patients" style="width: 100%">
            <el-table-column prop="name" label="Patient Name" />
           
            <el-table-column prop="phone" label="Phone Number" />
            <el-table-column label="Actions" width="250">
              <template #default="{ row }">
                <el-button type="primary" link @click="viewPatientDetail(row)">
                  View Details
                </el-button>
                <el-button type="success" link @click="viewPatientCases(row)">
                  View Records
                </el-button>
                <el-button type="warning" link @click="handleMessage(row)">
                  Message
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </template>
    </el-card>

    <!-- Patient Details Dialog -->
    <el-dialog
      v-model="detailDialogVisible"
      title="Patient Details"
      width="500px"
    >
      <div v-if="selectedPatient" class="patient-detail">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="Name">{{ selectedPatient.name }}</el-descriptions-item>
          
          <el-descriptions-item label="Phone Number">{{ selectedPatient.phone }}</el-descriptions-item>
          <el-descriptions-item label="Email">{{ selectedPatient.email }}</el-descriptions-item>
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

// Message patient
const handleMessage = (patient) => {
  // Navigate to message list with active patient
  router.push({
    path: '/doctor/messages',
    query: { patientId: patient.id, patientName: patient.name }
  });
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
      ElMessage.warning('No patient data available');
    }
  } catch (error) {
    console.error('Failed to load patient list:', error);
    ElMessage.error(error.response?.data?.message || 'Failed to load patient list');
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