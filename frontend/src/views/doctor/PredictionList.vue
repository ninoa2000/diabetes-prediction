<template>
  <el-container>
    <el-main>
      <el-card>
        <template #header>
          <div class="card-header">
            <span>Patient Prediction Records</span>
            <div class="header-actions">
              <el-input
                v-model="search"
                placeholder="Search patients..."
                style="width: 300px"
                class="mr-3"
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
              <el-select
                v-model="selectedDisease"
                placeholder="Disease Type"
                clearable
                style="width: 180px"
                class="mr-3"
              >
                <el-option
                  v-for="disease in diseaseTypes"
                  :key="disease"
                  :label="disease"
                  :value="disease"
                ></el-option>
              </el-select>
              <el-select
                v-model="riskLevel"
                placeholder="Risk Level"
                clearable
                style="width: 150px"
                class="mr-3"
              >
                <el-option
                  v-for="level in riskLevels"
                  :key="level.value"
                  :label="level.label"
                  :value="level.value"
                ></el-option>
              </el-select>
            </div>
          </div>
        </template>
        
        <el-table
          :data="filteredPredictions"
          style="width: 100%"
          border
          :default-sort="{ prop: 'date', order: 'descending' }"
        >
          <el-table-column prop="patientName" label="Patient Name" width="120" sortable></el-table-column>
          <el-table-column prop="diseaseType" label="Disease Type" width="120"></el-table-column>
          <el-table-column prop="date" label="Prediction Date" width="180" sortable>
            <template #default="scope">
              {{ formatDate(scope.row.date) }}
            </template>
          </el-table-column>
          <el-table-column prop="riskScore" label="Risk Score" width="100" sortable>
            <template #default="scope">
              <el-progress
                :percentage="scope.row.riskScore"
                :color="getRiskColor(scope.row.riskScore)"
                :stroke-width="18"
              ></el-progress>
            </template>
          </el-table-column>
          <el-table-column prop="riskLevel" label="Risk Level" width="120">
            <template #default="scope">
              <el-tag :type="getRiskTagType(scope.row.riskLevel)">
                {{ scope.row.riskLevel }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="Status" width="120">
            <template #default="scope">
              <el-tag :type="getStatusTagType(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="Actions" width="250">
            <template #default="scope">
              <el-button
                size="small"
                @click="viewDetails(scope.row)"
                type="primary"
                plain
                class="mr-2"
              >
                Details
              </el-button>
              <el-button
                size="small"
                @click="provideFeedback(scope.row)"
                type="success"
                plain
                :disabled="scope.row.status === 'Completed'"
                class="mr-2"
              >
                Feedback
              </el-button>
              <el-button
                size="small"
                @click="contactPatient(scope.row)"
                type="info"
                plain
              >
                Contact
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="filteredPredictions.length"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
      
      <!-- Details Dialog -->
      <el-dialog
        v-model="detailsDialog"
        title="Prediction Details"
        width="800px"
      >
        <template v-if="selectedPrediction">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="Patient Name" span="1">{{ selectedPrediction.patientName }}</el-descriptions-item>
            <el-descriptions-item label="Age" span="1">{{ selectedPrediction.patientAge }}</el-descriptions-item>
            <el-descriptions-item label="Gender" span="1">{{ selectedPrediction.patientGender }}</el-descriptions-item>
            <el-descriptions-item label="Contact Info" span="1">{{ selectedPrediction.patientContact }}</el-descriptions-item>
            <el-descriptions-item label="Disease Type" span="1">{{ selectedPrediction.diseaseType }}</el-descriptions-item>
            <el-descriptions-item label="Prediction Date" span="1">{{ formatDate(selectedPrediction.date) }}</el-descriptions-item>
            <el-descriptions-item label="Risk Score" span="1">
              <el-progress 
                :percentage="selectedPrediction.riskScore" 
                :color="getRiskColor(selectedPrediction.riskScore)"
                :stroke-width="18"
              ></el-progress>
            </el-descriptions-item>
            <el-descriptions-item label="Risk Level" span="1">
              <el-tag :type="getRiskTagType(selectedPrediction.riskLevel)">
                {{ selectedPrediction.riskLevel }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="Status" span="1">
              <el-tag :type="getStatusTagType(selectedPrediction.status)">
                {{ selectedPrediction.status }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="Key Indicators" span="2">
              <el-table :data="selectedPrediction.indicators" style="width: 100%">
                <el-table-column prop="name" label="Indicator" width="180"></el-table-column>
                <el-table-column prop="value" label="Value" width="120"></el-table-column>
                <el-table-column prop="normalRange" label="Normal Range" width="180"></el-table-column>
                <el-table-column prop="status" label="Status">
                  <template #default="scope">
                    <el-tag :type="scope.row.status === 'Normal' ? 'success' : 'danger'">
                      {{ scope.row.status }}
                    </el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </el-descriptions-item>
            <el-descriptions-item label="Risk Factors" span="2">{{ selectedPrediction.riskFactors }}</el-descriptions-item>
            <el-descriptions-item label="Advice" span="2">{{ selectedPrediction.recommendations }}</el-descriptions-item>
            <el-descriptions-item 
              v-if="selectedPrediction.doctorFeedback" 
              label="Doctor Feedback" 
              span="2"
            >
              {{ selectedPrediction.doctorFeedback }}
            </el-descriptions-item>
          </el-descriptions>
        </template>
      </el-dialog>
      
      <!-- Feedback Dialog -->
      <el-dialog
        v-model="feedbackDialog"
        title="Doctor Feedback"
        width="600px"
      >
        <el-form :model="feedbackForm" ref="feedbackFormRef" :rules="feedbackRules" label-width="120px">
          <el-form-item label="Accuracy" prop="accuracy">
            <el-rate
              v-model="feedbackForm.accuracy"
              :colors="rateColors"
              show-score
            ></el-rate>
          </el-form-item>
          <el-form-item label="Diagnosis" prop="diagnosis">
            <el-input v-model="feedbackForm.diagnosis" type="textarea" :rows="2" placeholder="Enter diagnosis..."></el-input>
          </el-form-item>
          <el-form-item label="Treatment Plan" prop="treatmentPlan">
            <el-input v-model="feedbackForm.treatmentPlan" type="textarea" :rows="3" placeholder="Enter treatment plan..."></el-input>
          </el-form-item>
          <el-form-item label="Lifestyle Advice" prop="lifestyleAdvice">
            <el-input v-model="feedbackForm.lifestyleAdvice" type="textarea" :rows="3" placeholder="Enter lifestyle advice..."></el-input>
          </el-form-item>
          <el-form-item label="Follow-up" prop="followUpPlan">
            <el-date-picker
              v-model="feedbackForm.followUpDate"
              type="date"
              placeholder="Select follow-up date"
              style="width: 100%"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="Notes" prop="notes">
            <el-input v-model="feedbackForm.notes" type="textarea" :rows="2" placeholder="Enter additional notes..."></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="feedbackDialog = false">Cancel</el-button>
            <el-button type="primary" @click="submitFeedback">Submit Feedback</el-button>
          </div>
        </template>
      </el-dialog>
    </el-main>
  </el-container>
</template>

<script>
import { ref, computed, reactive } from 'vue';
import { Search } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';

export default {
  name: 'PredictionList',
  components: {
    Search
  },
  setup() {
    const search = ref('');
    const selectedDisease = ref('');
    const riskLevel = ref('');
    const currentPage = ref(1);
    const pageSize = ref(10);
    const detailsDialog = ref(false);
    const feedbackDialog = ref(false);
    const selectedPrediction = ref(null);
    const feedbackFormRef = ref(null);
    
    const feedbackForm = reactive({
      accuracy: 3,
      diagnosis: '',
      treatmentPlan: '',
      lifestyleAdvice: '',
      followUpDate: '',
      notes: ''
    });
    
    const feedbackRules = {
      diagnosis: [
        { required: true, message: 'Please enter diagnosis', trigger: 'blur' }
      ],
      treatmentPlan: [
        { required: true, message: 'Please enter treatment plan', trigger: 'blur' }
      ]
    };
    
    const rateColors = ['#F56C6C', '#E6A23C', '#6F7AD3', '#5CB87A', '#1F9F85'];
    
    const diseaseTypes = [
      'Hypertension', 'Diabetes', 'Coronary Heart Disease', 'Stroke', 'Chronic Kidney Disease', 'COPD'
    ];
    
    const riskLevels = [
      { value: 'Low Risk', label: 'Low Risk' },
      { value: 'Medium Risk', label: 'Medium Risk' },
      { value: 'High Risk', label: 'High Risk' },
      { value: 'Very High Risk', label: 'Very High Risk' }
    ];
    
    // Mock Data
    const predictions = ref([
      {
        id: 1,
        patientId: 'P001',
        patientName: 'John Doe',
        patientAge: 58,
        patientGender: 'Male',
        patientContact: '13800138001',
        diseaseType: 'Hypertension',
        date: '2023-05-10T08:30:00',
        riskScore: 75,
        riskLevel: 'High Risk',
        status: 'Pending',
        indicators: [
          { name: 'Systolic BP', value: '150mmHg', normalRange: '90-120mmHg', status: 'Abnormal' },
          { name: 'Diastolic BP', value: '95mmHg', normalRange: '60-80mmHg', status: 'Abnormal' },
          { name: 'Heart Rate', value: '78bpm', normalRange: '60-100bpm', status: 'Normal' },
          { name: 'BMI', value: '28.5', normalRange: '18.5-24.9', status: 'Abnormal' }
        ],
        riskFactors: 'Age, Family History, Obesity, High Salt Diet, Lack of Exercise',
        recommendations: 'Control diet, reduce salt intake, increase aerobic exercise, control weight, regularly monitor blood pressure'
      },
      {
        id: 2,
        patientId: 'P002',
        patientName: 'Jane Smith',
        patientAge: 62,
        patientGender: 'Female',
        patientContact: '13800138002',
        diseaseType: 'Diabetes',
        date: '2023-05-09T10:15:00',
        riskScore: 85,
        riskLevel: 'Very High Risk',
        status: 'Completed',
        indicators: [
          { name: 'Fasting Glucose', value: '8.2mmol/L', normalRange: '3.9-6.1mmol/L', status: 'Abnormal' },
          { name: 'HbA1c', value: '7.8%', normalRange: '4.0-6.0%', status: 'Abnormal' },
          { name: 'Microalbuminuria', value: '35mg/24h', normalRange: '<30mg/24h', status: 'Abnormal' }
        ],
        riskFactors: 'Age, Family History, Obesity, High Carb Diet, Sedentary Lifestyle',
        recommendations: 'Control diet, reduce carbohydrate and sugar intake, increase exercise, regularly monitor blood glucose',
        doctorFeedback: 'Case consistent with Type 2 Diabetes diagnosis. Advised immediate lifestyle intervention and considering oral hypoglycemic medications.'
      },
      {
        id: 3,
        patientId: 'P003',
        patientName: 'Robert Wilson',
        patientAge: 45,
        patientGender: 'Male',
        patientContact: '13800138003',
        diseaseType: 'Hypertension',
        date: '2023-05-11T14:20:00',
        riskScore: 35,
        riskLevel: 'Low Risk',
        status: 'Pending',
        indicators: [
          { name: 'Systolic BP', value: '125mmHg', normalRange: '90-120mmHg', status: 'Abnormal' },
          { name: 'Diastolic BP', value: '82mmHg', normalRange: '60-80mmHg', status: 'Abnormal' },
          { name: 'Heart Rate', value: '72bpm', normalRange: '60-100bpm', status: 'Normal' },
          { name: 'BMI', value: '23.5', normalRange: '18.5-24.9', status: 'Normal' }
        ],
        riskFactors: 'High stress, slight obesity',
        recommendations: 'Maintain healthy lifestyle, monitor blood pressure, reduce work stress'
      },
      {
        id: 4,
        patientId: 'P004',
        patientName: 'Emma Brown',
        patientAge: 70,
        patientGender: 'Female',
        patientContact: '13800138004',
        diseaseType: 'Heart Disease',
        date: '2023-05-08T09:45:00',
        riskScore: 65,
        riskLevel: 'Medium Risk',
        status: 'Completed',
        indicators: [
          { name: 'Total Cholesterol', value: '5.8mmol/L', normalRange: '<5.2mmol/L', status: 'Abnormal' },
          { name: 'LDL-C', value: '3.5mmol/L', normalRange: '<3.4mmol/L', status: 'Abnormal' },
          { name: 'HDL-C', value: '1.0mmol/L', normalRange: '>1.0mmol/L', status: 'Normal' },
          { name: 'Blood Pressure', value: '145/90mmHg', normalRange: '<140/90mmHg', status: 'Abnormal' }
        ],
        riskFactors: 'Age, Hypertension, High Cholesterol, Smoking History',
        recommendations: 'Quit smoking, low-fat diet, regular exercise, control BP, statin therapy',
        doctorFeedback: 'Significant risk factors for CHD. Prescribed statins and advised aerobic exercise 3x/week.'
      },
      {
        id: 5,
        patientId: 'P005',
        patientName: 'William Jones',
        patientAge: 55,
        patientGender: 'Male',
        patientContact: '13800138005',
        diseaseType: 'COPD',
        date: '2023-05-07T15:30:00',
        riskScore: 55,
        riskLevel: 'Medium Risk',
        status: 'Pending',
        indicators: [
          { name: 'FEV1', value: '65% Predicted', normalRange: '>80% Predicted', status: 'Abnormal' },
          { name: 'FEV1/FVC', value: '0.65', normalRange: '>0.7', status: 'Abnormal' },
          { name: 'SpO2', value: '94%', normalRange: '95-100%', status: 'Abnormal' }
        ],
        riskFactors: 'Long-term smoking, occupational exposure, recurrent infections',
        recommendations: 'Immediate smoking cessation, avoid pollution, vaccinations, consider bronchodilators'
      }
    ]);
    
    const filteredPredictions = computed(() => {
      let result = predictions.value;
      
      if (search.value) {
        const searchLower = search.value.toLowerCase();
        result = result.filter(p => 
          p.patientName.toLowerCase().includes(searchLower) ||
          p.patientId.toLowerCase().includes(searchLower)
        );
      }
      
      if (selectedDisease.value) {
        result = result.filter(p => p.diseaseType === selectedDisease.value);
      }
      
      if (riskLevel.value) {
        result = result.filter(p => p.riskLevel === riskLevel.value);
      }
      
      return result;
    });
    
    const paginatedPredictions = computed(() => {
      const start = (currentPage.value - 1) * pageSize.value;
      const end = start + pageSize.value;
      return filteredPredictions.value.slice(start, end);
    });
    
    const formatDate = (dateString) => {
      const date = new Date(dateString);
      return date.toLocaleString();
    };
    
    const getRiskColor = (score) => {
      if (score < 30) return '#67C23A';
      if (score < 60) return '#E6A23C';
      if (score < 80) return '#F56C6C';
      return '#F56C6C';
    };
    
    const getRiskTagType = (level) => {
      switch (level) {
        case 'Low Risk': return 'success';
        case 'Medium Risk': return 'warning';
        case 'High Risk': return 'danger';
        case 'Very High Risk': return 'danger';
        default: return '';
      }
    };
    
    const getStatusTagType = (status) => {
      switch (status) {
        case 'Pending': return 'info';
        case 'Completed': return 'success';
        default: return '';
      }
    };
    
    const viewDetails = (prediction) => {
      selectedPrediction.value = prediction;
      detailsDialog.value = true;
    };
    
    const provideFeedback = (prediction) => {
      selectedPrediction.value = prediction;
      // Reset form
      Object.assign(feedbackForm, {
        accuracy: 3,
        diagnosis: '',
        treatmentPlan: '',
        lifestyleAdvice: '',
        followUpDate: new Date(new Date().setDate(new Date().getDate() + 30)), // Default 30 days
        notes: ''
      });
      feedbackDialog.value = true;
    };
    
    const submitFeedback = () => {
      feedbackFormRef.value.validate((valid) => {
        if (valid) {
          const index = predictions.value.findIndex(p => p.id === selectedPrediction.value.id);
          if (index !== -1) {
            predictions.value[index].status = 'Completed';
            predictions.value[index].doctorFeedback = `Diagnosis: ${feedbackForm.diagnosis}\nTreatment: ${feedbackForm.treatmentPlan}\nAdvice: ${feedbackForm.lifestyleAdvice}\nFollow-up: ${feedbackForm.followUpDate ? formatDate(feedbackForm.followUpDate) : 'Not set'}\nNotes: ${feedbackForm.notes}`;
            
            ElMessage.success('Feedback submitted successfully');
            feedbackDialog.value = false;
          }
        }
      });
    };
    
    const contactPatient = (prediction) => {
      ElMessageBox.confirm(
        `Do you want to contact patient ${prediction.patientName}? Phone: ${prediction.patientContact}`,
        'Contact Patient',
        {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'info'
        }
      ).then(() => {
        ElMessage.success(`Initiating call with ${prediction.patientName}...`);
      }).catch(() => {});
    };
    
    const handleSizeChange = (size) => {
      pageSize.value = size;
      currentPage.value = 1;
    };
    
    const handleCurrentChange = (page) => {
      currentPage.value = page;
    };
    
    return {
      search,
      selectedDisease,
      riskLevel,
      currentPage,
      pageSize,
      detailsDialog,
      feedbackDialog,
      selectedPrediction,
      feedbackForm,
      feedbackFormRef,
      feedbackRules,
      rateColors,
      diseaseTypes,
      riskLevels,
      predictions,
      filteredPredictions,
      paginatedPredictions,
      formatDate,
      getRiskColor,
      getRiskTagType,
      getStatusTagType,
      viewDetails,
      provideFeedback,
      submitFeedback,
      contactPatient,
      handleSizeChange,
      handleCurrentChange
    };
  }
};
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  align-items: center;
}

.mr-2 {
  margin-right: 8px;
}

.mr-3 {
  margin-right: 12px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style> 