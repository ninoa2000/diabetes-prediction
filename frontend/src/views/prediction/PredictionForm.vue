<template>
  <div class="prediction-form">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>Health Prediction</span>

          <!-- Model dropdown -->
          <div class="model-select">
            <span class="model-label">Model:</span>
            <el-select
              v-model="modelType"
              size="small"
              style="width: 180px"
              @change="saveModelType"
            >
              <el-option label="SVM" value="svm" />
              <el-option label="XGBoost" value="xgboost" />
              <el-option label="Random Forest" value="random_forest" />
              <el-option label="MLP" value="mlp" />
              <el-option label="Decision Tree" value="decision_tree" />
            </el-select>
          </div>
        </div>
      </template>

      <el-upload
        v-if="!previewData.length"
        class="upload-demo"
        drag
        action="#"
        :auto-upload="false"
        :on-change="handleFileChange"
        accept=".xlsx,.xls"
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">
          Drag the inspection report here, or <em>click Upload</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">
            Please upload the inspection report in Excel format, .xlsx and .xls formats are supported
          </div>
        </template>
      </el-upload>

      <div v-if="previewData.length > 0" class="preview-section">
        <div class="preview-header">
          <h3>Data Preview</h3>
          <el-button link type="primary" @click="clearPreview">
            <el-icon><refresh /></el-icon>
            Re-upload
          </el-button>
        </div>

        <div class="data-preview">
          <div class="data-row" v-for="(row, rowIndex) in chunkedData" :key="rowIndex">
            <div v-for="(value, key) in row" :key="key" class="data-item">
              <div class="data-label">{{ key }}：</div>
              <el-input
                v-model="previewData[0][key]"
                type="textarea"
                :autosize="{ minRows: 1, maxRows: 4 }"
                resize="none"
                class="data-value"
              />
            </div>
          </div>
        </div>

        <div class="preview-actions">
          <el-button
            type="warning"
            @click="compareAlgorithms"
          >
            Algorithm Comparison
          </el-button>
          <el-button
            type="success"
            @click="predictDirectly"
          >
            Direct Predict
          </el-button>
          <el-button
            v-if="!selectedCase?.disease"
            type="primary"
            @click="submitData"
          >
            Save medical record
          </el-button>
        </div>
      </div>

      <!-- Case List -->
      <div v-if="!previewData.length && cases.length > 0" class="cases-section">
        <h3>Saved Medical Records</h3>
        <el-table :data="cases" style="width: 100%" v-loading="loading">
          <el-table-column prop="createdAt" label="Upload Time" width="180">
            <template #default="scope">
              {{ formatDate(scope.row.createdAt) }}
            </template>
          </el-table-column>
          <el-table-column prop="disease" label="Prediction Result">
            <template #default="scope">
              <span v-if="scope.row.disease">
                {{ scope.row.disease }}
                ({{ (scope.row.probability * 100).toFixed(2) }}%)
              </span>
              <span v-else>No prediction</span>
            </template>
          </el-table-column>
          <el-table-column label="Actions" width="280">
            <template #default="scope">
              <el-button type="primary" link @click="viewCase(scope.row)">
                Details
              </el-button>
              <el-button
                v-if="!scope.row.disease"
                type="success"
                link
                @click="predictCase(scope.row)"
              >
                Predict
              </el-button>
              <el-button type="danger" link @click="deleteCase(scope.row)">
                Delete
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- Case Details Dialog -->
      <el-dialog v-model="caseDialogVisible" title="Medical record details" width="70%">
        <div v-if="selectedCase" class="case-details">
          <div class="data-row" v-for="(row, rowIndex) in chunkedCaseData" :key="rowIndex">
            <div v-for="(value, key) in row" :key="key" class="data-item">
              <div class="data-label">{{ key }}</div>
              <div class="data-value">{{ value }}</div>
            </div>
          </div>

          <div v-if="selectedCase.disease" class="prediction-result">
            <h4>Prediction Result</h4>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="Predicted Disease">
                {{ selectedCase.disease }}
              </el-descriptions-item>
              <el-descriptions-item label="Probability">
                {{ (selectedCase.probability * 100).toFixed(2) }}%
              </el-descriptions-item>
              <el-descriptions-item label="Suggestion">
                {{ selectedCase.suggestion }}
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </div>
      </el-dialog>

      <!-- Prediction Result Dialog -->
      <el-dialog
        v-model="predictionDialogVisible"
        title="Prediction Result"
        width="40%"
        :close-on-click-modal="false"
        :close-on-press-escape="false"
        :show-close="false"
      >
        <div class="prediction-result">
          <div class="progress-container">
            <div class="circle-progress">
              <el-progress
                type="dashboard"
                :percentage="predictionPercentage"
                :format="format"
                :color="customColors"
                :stroke-width="10"
                :show-text="true"
                :width="200"
              >
                <template #default="{ percentage }">
                  <div class="progress-content">
                    <div class="percentage">{{ percentage.toFixed(1) }}%</div>
                    <div class="risk-level">{{ riskLevel }}</div>
                  </div>
                </template>
              </el-progress>
            </div>
          </div>
          <div class="result-text">
            <div class="disease-info">
              <h3>Predicted Disease: {{ predictionDisease }}</h3>
              <p>Probability: {{ predictionPercentage.toFixed(1) }}%</p>
              <p>Risk Level: {{ riskLevel }}</p>
            </div>
            <div class="suggestion-box">
              <h3>Health Suggestion</h3>
              <div class="suggestion-content" v-html="formattedSuggestion"></div>
            </div>
          </div>
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="closePredictionDialog">Close</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- Algorithm Comparison Dialog -->
      <el-dialog
        v-model="comparisonDialogVisible"
        title="Algorithm Comparison Analysis"
        width="60%"
        @opened="initComparisonChart"
      >
        <div class="comparison-container">
          <div ref="comparisonChartRef" style="width: 100%; height: 400px;"></div>
          
          <div class="comparison-table">
            <el-table :data="comparisonTableData" stripe style="width: 100%">
              <el-table-column prop="algo" label="Algorithm" />
              <el-table-column prop="prob" label="Probability">
                <template #default="scope">
                  {{ (scope.row.prob * 100).toFixed(2) }}%
                </template>
              </el-table-column>
              <el-table-column prop="label" label="Prediction">
                <template #default="scope">
                  <el-tag :type="scope.row.prob >= 0.5 ? 'danger' : 'success'">
                    {{ scope.row.label }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { UploadFilled, Refresh } from '@element-plus/icons-vue'
import * as XLSX from 'xlsx'
import * as echarts from 'echarts'
import { predictionService } from '@/api/prediction'

const expectedColumns = 35
const previewData = ref([])
const cases = ref([])
const loading = ref(false)
const caseDialogVisible = ref(false)
const selectedCase = ref(null)
const predictionDialogVisible = ref(false)
const predictionDisease = ref('')
const predictionSuggestion = ref('')
const predictionPercentage = ref(0)
const predictionInterval = ref(null)

// Algorithm Comparison
const comparisonDialogVisible = ref(false)
const comparisonChartRef = ref(null)
const comparisonResults = ref({})
let comparisonChart = null

/**
 * ✅ Model selection state (saved in localStorage)
 */
const modelType = ref(localStorage.getItem('modelType') || 'svm')
const saveModelType = (val) => {
  localStorage.setItem('modelType', val)
}

// Data grouping for display
const chunkedData = computed(() => {
  if (!previewData.value.length) return []

  const items = Object.entries(previewData.value[0])
  const result = []
  for (let i = 0; i < items.length; i += 3) {
    const chunk = {}
    items.slice(i, i + 3).forEach(([key, value]) => {
      chunk[key] = value
    })
    result.push(chunk)
  }
  return result
})

// Case details grouping
const chunkedCaseData = computed(() => {
  if (!selectedCase.value?.healthData) return []

  const items = Object.entries(selectedCase.value.healthData)
  const result = []
  for (let i = 0; i < items.length; i += 3) {
    const chunk = {}
    items.slice(i, i + 3).forEach(([key, value]) => {
      chunk[key] = value
    })
    result.push(chunk)
  }
  return result
})

const handleFileChange = (file) => {
  const reader = new FileReader()
  reader.onload = (e) => {
    const wb = XLSX.read(e.target.result, { type: 'array' })
    const ws = wb.Sheets[wb.SheetNames[0]]

    const range = XLSX.utils.decode_range(ws['!ref'])
    const actualColumns = range.e.c - range.s.c + 1
    if (actualColumns !== expectedColumns) {
      ElMessage.error('Data mismatch, please check the file columns')
      return
    }

    const jsonData = XLSX.utils.sheet_to_json(ws, { defval: null })
    if (!jsonData.length) {
      ElMessage.error('Excel file is empty')
      return
    }
    previewData.value = [jsonData[0]]
  }
  reader.readAsArrayBuffer(file.raw)
}

const clearPreview = () => {
  previewData.value = []
  loadCases()
}

const submitData = async () => {
  if (previewData.value.length === 0) {
    ElMessage.warning('Please upload data first')
    return
  }

  try {
    loading.value = true
    const user = JSON.parse(localStorage.getItem('user'))
    if (!user || !user.id) {
      ElMessage.error('User not logged in')
      return
    }

    const requestData = {
      ...previewData.value[0],
      userId: user.id
    }

    await predictionService.saveHealthData(requestData)
    ElMessage.success('Medical record saved successfully')
    clearPreview()
  } catch (error) {
    console.error('Save failed:', error)
    ElMessage.error(error.message || 'Save failed')
  } finally {
    loading.value = false
  }
}

const loadCases = async () => {
  try {
    loading.value = true
    const response = await predictionService.getHealthCases()
    cases.value = response.data
  } catch (error) {
    console.error('Failed to load records:', error)
    ElMessage.error('Failed to load records')
  } finally {
    loading.value = false
  }
}

const viewCase = (caseData) => {
  selectedCase.value = caseData
  caseDialogVisible.value = true
}

// Compute risk level
const riskLevel = computed(() => {
  if (predictionPercentage.value < 30) return 'Low Risk'
  if (predictionPercentage.value < 70) return 'Medium Risk'
  return 'High Risk'
})

// Compute progress bar color
const customColors = computed(() => {
  if (predictionPercentage.value < 30) return '#67C23A'
  if (predictionPercentage.value < 70) return '#E6A23C'
  return '#F56C6C'
})

const format = (percentage) => percentage.toFixed(2) + '%'

const formattedSuggestion = computed(() => {
  return predictionSuggestion.value ? predictionSuggestion.value.replace(/\n/g, '<br>') : ''
})

const closePredictionDialog = () => {
  predictionDialogVisible.value = false
  predictionPercentage.value = 0
  predictionDisease.value = ''
  predictionSuggestion.value = ''
  if (predictionInterval.value) clearInterval(predictionInterval.value)
}

const predictCase = async (caseData) => {
  try {
    predictionDialogVisible.value = true
    predictionPercentage.value = 0
    predictionDisease.value = ''
    predictionSuggestion.value = ''

    predictionInterval.value = setInterval(() => {
      if (predictionPercentage.value < 90) predictionPercentage.value += 10
    }, 200)

    loading.value = true

    const selectedModel = localStorage.getItem('modelType') || 'svm'
    const response = await predictionService.predictWithFlask(caseData.healthData || caseData, selectedModel)

    clearInterval(predictionInterval.value)

    predictionPercentage.value = response.data.probability * 100
    predictionDisease.value = response.data.disease
    predictionSuggestion.value = response.data.suggestion

    try {
      await loadCases()
    } catch (e) {
      console.warn('Backend loadCases failed, but prediction succeeded:', e)
    }
  } catch (error) {
    console.error('Prediction failed:', error)
    ElMessage.error(error.message || 'Prediction failed')
    closePredictionDialog()
  } finally {
    loading.value = false
  }
}

const predictDirectly = async () => {
  if (previewData.value.length === 0) {
    ElMessage.warning('Please upload data first')
    return
  }
  
  try {
    const dataToPredict = previewData.value[0]
    await predictCase(dataToPredict)
  } catch (error) {
    console.error('Direct prediction failed:', error)
    ElMessage.error('Direct prediction failed')
  }
}

const compareAlgorithms = async () => {
  if (previewData.value.length === 0) {
    ElMessage.warning('Please upload data first')
    return
  }
  
  try {
    loading.value = true
    const response = await predictionService.predictAllWithFlask(previewData.value[0])
    comparisonResults.value = response.data.results
    comparisonDialogVisible.value = true
  } catch (error) {
    console.error('Algorithm comparison failed:', error)
    ElMessage.error('Failed to get comparison results')
  } finally {
    loading.value = false
  }
}

const comparisonTableData = computed(() => {
  return Object.entries(comparisonResults.value).map(([algo, data]) => ({
    algo: algo.toUpperCase().replace('_', ' '),
    prob: data.probability || 0,
    label: data.label || 'Unknown'
  }))
})

const initComparisonChart = () => {
  if (!comparisonChartRef.value) return
  
  if (comparisonChart) {
    comparisonChart.dispose()
  }
  
  comparisonChart = echarts.init(comparisonChartRef.value)
  
  const data = comparisonTableData.value
  const option = {
    title: { text: 'Comparison of Prediction Probabilities', left: 'center' },
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: data.map(i => i.algo)
    },
    yAxis: {
      type: 'value',
      name: 'Probability (%)',
      max: 100
    },
    series: [{
      data: data.map(i => (i.prob * 100).toFixed(2)),
      type: 'bar',
      showBackground: true,
      backgroundStyle: { color: 'rgba(180, 180, 180, 0.2)' },
      itemStyle: {
        color: (params) => {
          const val = params.data
          if (val < 30) return '#67C23A'
          if (val < 70) return '#E6A23C'
          return '#F56C6C'
        }
      },
      label: {
        show: true,
        position: 'top',
        formatter: '{c}%'
      }
    }]
  }
  
  comparisonChart.setOption(option)
}

const deleteCase = async (caseData) => {
  try {
    await ElMessageBox.confirm('Are you sure you want to delete this medical record?', 'Notice', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })

    loading.value = true
    await predictionService.deleteCase(caseData.id)
    await loadCases()
    ElMessage.success('Deleted successfully')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Delete failed:', error)
      ElMessage.error(error.message || 'Delete failed')
    }
  } finally {
    loading.value = false
  }
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString()
}

onMounted(() => {
  loadCases()
})

onBeforeUnmount(() => {
  if (predictionInterval.value) clearInterval(predictionInterval.value)
})
</script>

<style scoped>
.prediction-form {
  max-width: 800px;
  margin: 20px auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* ✅ model dropdown */
.model-select {
  display: flex;
  align-items: center;
  gap: 8px;
}

.model-label {
  color: #606266;
  font-size: 13px;
}

.upload-demo {
  width: 100%;
}

.preview-section, .cases-section {
  margin-top: 20px;
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.preview-header h3, .cases-section h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}

.data-preview {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 20px;
}

.data-row {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 20px;
}

.data-item {
  flex: 1;
  min-width: 200px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.data-label {
  width: 100px;
  text-align: right;
  color: #606266;
}

.data-value {
  flex: 1;
  color: #303133;
}

.preview-actions {
  display: flex;
  justify-content: flex-end;
}

.el-upload__tip {
  margin-top: 10px;
  color: #666;
}

:deep(.el-upload-dragger) {
  width: 100%;
}

.case-details {
  padding: 20px;
}

.prediction-result {
  text-align: center;
  padding: 20px;
}

.progress-container {
  margin: 20px 0;
  display: flex;
  justify-content: center;
}

.circle-progress {
  position: relative;
  width: 200px;
  height: 200px;
}

.progress-content {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
}

.percentage {
  font-size: 24px;
  font-weight: bold;
  color: var(--el-text-color-primary);
}

.risk-level {
  font-size: 14px;
  color: var(--el-text-color-secondary);
  margin-top: 5px;
}

.result-text {
  margin-top: 20px;
  font-size: 16px;
  animation: fadeIn 0.5s ease-in-out;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

:deep(.el-progress-bar__outer) {
  background-color: var(--el-border-color-lighter);
}

:deep(.el-progress-bar__inner) {
  transition: all 0.3s ease-in-out;
}

.disease-info {
  margin-bottom: 20px;
  padding: 15px;
  background-color: var(--el-bg-color-page);
  border-radius: 8px;
}

.suggestion-box {
  margin-top: 20px;
  padding: 15px;
  background-color: var(--el-bg-color-page);
  border-radius: 8px;
}

.suggestion-content {
  color: var(--el-text-color-regular);
  line-height: 1.6;
  text-align: left;
  font-size: 16px;
}

.comparison-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 10px;
}

.comparison-table {
  margin-top: 10px;
}
</style>
