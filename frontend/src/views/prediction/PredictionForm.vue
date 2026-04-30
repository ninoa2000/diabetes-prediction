<template>
  <div class="prediction-form">
    <el-card class="box-card custom-card">
      <template #header>
        <div class="card-header">
          <div class="header-title">
            <el-icon class="header-icon"><Monitor /></el-icon>
            <span>Health Prediction</span>
          </div>

          <!-- Model dropdown -->
          <div class="model-select">
            <span class="model-label">Model:</span>
            <el-select
              v-model="modelType"
              size="default"
              style="width: 180px"
              @change="saveModelType"
              class="custom-select"
            >
              <el-option label="SVM" value="svm" />
              <el-option label="XGBoost" value="xgboost" />
              <el-option label="Random Forest" value="random_forest" />
              <el-option label="Deep Neural Network" value="mlp" />
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
          <div class="preview-title-group">
            <h3>Patient Data Overview</h3>
            <el-tag type="primary" size="default" effect="light" class="file-tag" v-if="uploadedFileName">
              <el-icon><Document /></el-icon> {{ uploadedFileName }}
            </el-tag>
          </div>
          <el-button plain type="primary" @click="clearPreview" class="reupload-btn">
            <el-icon><Refresh /></el-icon>
            Upload Different File
          </el-button>
        </div>

        <div class="data-preview">
          <div v-for="(value, key) in previewData[0]" :key="key" class="data-item">
            <div class="data-label">{{ key }}</div>
            <el-input
              v-model="previewData[0][key]"
              size="default"
              class="data-value"
            />
          </div>
        </div>

        <div class="preview-actions">
          <el-button
            type="info"
            plain
            size="large"
            @click="compareAlgorithms"
            class="action-btn"
          >
            <el-icon><DataAnalysis /></el-icon> Compare Algorithms
          </el-button>
          <el-button
            v-if="!selectedCase?.disease"
            type="primary"
            plain
            size="large"
            @click="submitData"
            class="action-btn"
          >
            <el-icon><Collection /></el-icon> Save Record
          </el-button>
          <el-button
            type="success"
            size="large"
            @click="predictDirectly"
            class="action-btn predict-btn"
          >
            <el-icon><MagicStick /></el-icon> Run Prediction
          </el-button>
        </div>
      </div>

      <!-- Case List -->
      <div v-if="!previewData.length && cases.length > 0" class="cases-section">
        <h3>Saved Medical Records</h3>
        <el-table :data="cases" style="width: 100%" v-loading="loading" border stripe>
          <el-table-column prop="createdAt" label="Upload Time" width="180">
            <template #default="scope">
              {{ formatDate(scope.row.createdAt) }}
            </template>
          </el-table-column>
          <el-table-column prop="fileName" label="File Name" min-width="150">
            <template #default="scope">
              <span v-if="scope.row.fileName">{{ scope.row.fileName }}</span>
              <span v-else style="color: #999;">Manual Input</span>
            </template>
          </el-table-column>
          <el-table-column prop="algorithm" label="Algorithm" width="150">
            <template #default="scope">
              <el-tag v-if="scope.row.algorithm" type="info">{{ scope.row.algorithm.toUpperCase().replace('_', ' ') }}</el-tag>
              <span v-else style="color: #999;">None</span>
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
          <div class="metadata-section" style="margin-bottom: 20px;">
            <el-descriptions border :column="2">
              <el-descriptions-item label="File Name">{{ selectedCase.fileName || 'Manual Input' }}</el-descriptions-item>
              <el-descriptions-item label="Algorithm">{{ selectedCase.algorithm ? selectedCase.algorithm.toUpperCase().replace('_', ' ') : 'N/A' }}</el-descriptions-item>
            </el-descriptions>
          </div>
          
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
import { UploadFilled, Refresh, Document, Monitor, DataAnalysis, Collection, MagicStick } from '@element-plus/icons-vue'
import * as XLSX from 'xlsx'
import * as echarts from 'echarts'
import { predictionService } from '@/api/prediction'

const expectedColumns = 35
const uploadedFileName = ref('')
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

// Data grouping for display is no longer needed because we use CSS Grid directly on the object.
// We removed chunkedData entirely.

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
  uploadedFileName.value = file.name
  const reader = new FileReader()
  reader.onload = (e) => {
    const wb = XLSX.read(e.target.result, { type: 'array' })
    const ws = wb.Sheets[wb.SheetNames[0]]

    const range = XLSX.utils.decode_range(ws['!ref'])
    const actualColumns = range.e.c - range.s.c + 1
    if (actualColumns !== expectedColumns) {
      ElMessage.error('Data mismatch, please check the file columns')
      uploadedFileName.value = ''
      return
    }

    const jsonData = XLSX.utils.sheet_to_json(ws, { defval: null })
    if (!jsonData.length) {
      ElMessage.error('Excel file is empty')
      uploadedFileName.value = ''
      return
    }
    previewData.value = [jsonData[0]]
  }
  reader.readAsArrayBuffer(file.raw)
}

const clearPreview = () => {
  previewData.value = []
  uploadedFileName.value = ''
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
      userId: user.id,
      fileName: uploadedFileName.value || null,
      algorithm: predictionDisease.value ? modelType.value : null,
      disease: predictionDisease.value || null,
      probability: predictionPercentage.value > 0 ? predictionPercentage.value / 100 : null,
      suggestion: predictionSuggestion.value || null
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
    loading.value = true
    await predictionService.deleteCase(caseData.id)
    await loadCases()
    ElMessage.success('Deleted successfully')
  } catch (error) {
    console.error('Delete failed:', error)
    ElMessage.error(error.message || 'Delete failed')
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
  width: 100%;
  margin: 20px auto;
}

.custom-card {
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(149, 157, 165, 0.1) !important;
  border: none;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 5px 0;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 20px;
  font-weight: 700;
  color: #1e293b;
}

.header-icon {
  color: var(--el-color-primary);
  font-size: 24px;
}

.model-select {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #f8fafc;
  padding: 6px 16px;
  border-radius: 20px;
  border: 1px solid #e2e8f0;
}

.model-label {
  color: #475569;
  font-size: 14px;
  font-weight: 600;
}

.custom-select :deep(.el-input__wrapper) {
  box-shadow: none !important;
  background: transparent;
}

.upload-demo {
  width: 100%;
}

:deep(.el-upload-dragger) {
  width: 100%;
  padding: 40px;
  border-radius: 12px;
  background-color: #f8fafc;
  border: 2px dashed #cbd5e1;
  transition: all 0.3s ease;
}

:deep(.el-upload-dragger:hover) {
  border-color: var(--el-color-primary);
  background-color: #f0f9ff;
}

.preview-section, .cases-section {
  margin-top: 25px;
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid #f1f5f9;
}

.preview-title-group {
  display: flex;
  align-items: center;
  gap: 15px;
}

.preview-title-group h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
  color: #0f172a;
}

.file-tag {
  font-size: 14px;
  padding: 4px 12px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.reupload-btn {
  border-radius: 20px;
}

.data-preview {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 16px;
  margin-bottom: 30px;
  background: #f8fafc;
  padding: 24px;
  border-radius: 16px;
  border: 1px solid #e2e8f0;
}

.data-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
  background: white;
  padding: 12px 16px;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.02);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  border: 1px solid #f1f5f9;
}

.data-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  border-color: #e2e8f0;
}

.data-label {
  font-size: 12px;
  color: #64748b;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.data-value {
  width: 100%;
}

.data-value :deep(.el-input__wrapper) {
  box-shadow: none;
  background: #f8fafc;
  border-radius: 6px;
}

.preview-actions {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  padding-top: 10px;
}

.action-btn {
  border-radius: 8px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 6px;
}

.predict-btn {
  padding: 0 24px;
  box-shadow: 0 4px 12px rgba(103, 194, 58, 0.3);
}

.el-upload__tip {
  margin-top: 15px;
  color: #64748b;
  font-size: 13px;
  text-align: center;
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
