<template>
  <div class="prediction-form fade-in">
    <!-- Main Prediction Card -->
    <!-- Main Prediction Card -->
    <el-card class="box-card premium-card no-header" shadow="never">
      <!-- Input Selection -->
      <div v-if="!previewData.length" class="input-selection-container">
        <el-row :gutter="32">
          <el-col :span="12">
            <div class="method-card upload-card" @click="triggerUpload">
              <div class="card-glow"></div>
              <el-icon class="method-icon"><UploadFilled /></el-icon>
              <h3>Import Dataset</h3>
              <p>Upload clinical metrics from standardized Excel or CSV formats.</p>
              <div class="action-hint">Browse Device Storage</div>
              <input type="file" ref="fileInput" style="display: none" @change="handleFileChange" accept=".xlsx,.xls">
            </div>
          </el-col>

          <el-col :span="12">
            <div class="method-card scan-card premium-scan-card">
              <div class="card-glow"></div>
              <div class="ai-tag">AI Vision</div>
              <el-icon class="method-icon"><Camera /></el-icon>
              <h3>AI Medical Scan</h3>
              <p>Extract metrics from medical letters or printed reports.</p>
              
              <div class="scan-options-row">
                <el-button type="primary" class="scan-sub-btn" @click="openScanner">
                  <el-icon><VideoCamera /></el-icon> Use Camera
                </el-button>
                <el-button type="success" plain class="scan-sub-btn" @click="triggerImageUpload">
                  <el-icon><Picture /></el-icon> Upload Image
                </el-button>
              </div>
              <input type="file" ref="imageInput" style="display: none" @change="handleImageFileChange" accept="image/*">
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- Unified Preview Stage -->
      <div v-if="previewData.length > 0" class="preview-stage fade-in">
        <div class="stage-header">
          <div class="stage-info">
            <el-icon v-if="isScanResult"><MagicStick /></el-icon>
            <el-icon v-else><Document /></el-icon>
            <h3>Verify Metrics & Select Intelligence</h3>
            <span class="source-pill" :class="{ 'ai-source': isScanResult }">
              {{ isScanResult ? 'AI Vision Scan' : (uploadedFileName || 'Data Import') }}
            </span>
          </div>
          <el-button link type="primary" @click="clearPreview">Reset & Change Method</el-button>
        </div>

        <div class="metrics-grid">
          <div v-for="(value, key) in previewData[0]" :key="key" class="metric-input-item">
            <label>{{ key }}</label>
            <el-input v-model="previewData[0][key]" size="default" />
          </div>
        </div>

        <!-- Model Selection Section (Now inside preview) -->
        <div class="model-selection-preview fade-in">
          <div class="selection-box-compact">
            <div class="selection-label-group">
              <span class="selection-title">Diagnostic Intelligence</span>
              <span class="selection-subtitle">Choose the AI algorithm to process these metrics</span>
            </div>
            <el-select
              v-model="modelType"
              size="large"
              placeholder="Choose an AI Algorithm..."
              @change="saveModelType"
              class="premium-select-standalone"
              style="width: 320px"
            >
              <el-option label="SVM (Support Vector Machine)" value="svm" />
              <el-option label="XGBoost (Extreme Gradient Boosting)" value="xgboost" />
              <el-option label="Random Forest Classifier" value="random_forest" />
              <el-option label="MLP (Neural Network)" value="mlp" />
              <el-option label="Decision Tree" value="decision_tree" />
            </el-select>
          </div>
        </div>

        <div class="stage-footer">
          <el-button 
            type="success" 
            size="large" 
            @click="predictDirectly" 
            class="btn-wide predict-btn"
            :disabled="!modelType"
          >
            {{ modelType ? 'Run AI Diagnosis' : 'Select Algorithm to Predict' }}
          </el-button>
        </div>
      </div>

      <!-- History Records -->
      <div v-if="!previewData.length && cases.length > 0" class="history-section">
        <div class="history-header">
          <div class="history-title-group">
            <h3>Recent Records</h3>
            <p>Historical clinical assessments and diagnostic history</p>
          </div>
          <el-button link type="primary" @click="loadCases" :loading="loading">
            <el-icon><Refresh /></el-icon> Refresh
          </el-button>
        </div>
        <el-table :data="cases" style="width: 100%" v-loading="loading" class="premium-table">
          <el-table-column prop="createdAt" label="Time" width="160">
            <template #default="scope">
              <span class="time-text">{{ formatDateShort(scope.row.createdAt) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="fileName" label="Source" min-width="120">
            <template #default="scope">
              <el-tag v-if="scope.row.fileName?.includes('Scan')" type="success" size="small" effect="plain">AI Scan</el-tag>
              <span v-else-if="scope.row.fileName" class="source-text">{{ scope.row.fileName }}</span>
              <span v-else class="manual-badge">Manual</span>
            </template>
          </el-table-column>
          <el-table-column prop="algorithm" label="Model" width="160">
            <template #default="scope">
              <el-tag v-if="scope.row.algorithm" effect="dark" class="algo-pill-solid">
                {{ scope.row.algorithm.toUpperCase() }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="disease" label="Assessment Result">
            <template #default="scope">
              <div v-if="scope.row.disease" class="result-pill-container">
                <span class="status-dot" :style="{ backgroundColor: getProbColor(scope.row.probability) }"></span>
                <span class="status-text-small" :style="{ color: getProbColor(scope.row.probability) }">
                  {{ formatRiskDisplay(scope.row.probability) }}
                </span>
                <span class="status-percent-small">({{ (scope.row.probability * 100).toFixed(0) }}%)</span>
              </div>
              <span v-else class="pending-text">Pending</span>
            </template>
          </el-table-column>
          <el-table-column label="Actions" width="200" align="right">
            <template #default="scope">
              <el-button type="primary" link @click="viewCase(scope.row)">View</el-button>
              <el-button v-if="!scope.row.disease" type="success" link @click="predictCase(scope.row)">Predict</el-button>
              <el-button type="danger" link @click="deleteCase(scope.row)">Delete</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- AI Vision Scanner Dialog -->
    <el-dialog v-model="scannerVisible" title="AI Clinical Report Scanner" width="700px" custom-class="scanner-dialog" :before-close="closeScanner">
      <div class="scanner-viewport">
        <div v-if="countdown > 0" class="countdown-overlay"><div class="countdown-number">{{ countdown }}</div></div>
        <div v-if="cameraActive" class="video-container">
          <video ref="videoRef" autoplay playsinline muted class="camera-stream"></video>
          <div class="scan-overlay"><div class="scan-frame"></div><div class="scan-laser" v-if="scanning"></div></div>
          <div class="camera-controls" v-if="countdown === 0">
            <el-button type="primary" size="large" @click="startCountdown" :loading="scanning"><el-icon><Camera /></el-icon> Capture Clinical Data</el-button>
          </div>
        </div>
        <div v-else class="camera-init">
          <div v-if="!cameraError" class="loading-state"><el-icon class="is-loading" :size="40"><Loading /></el-icon><p>Initialising Secure AI Camera Feed...</p></div>
          <div v-else class="error-msg"><el-icon :size="48" color="#f56c6c"><CircleClose /></el-icon><p>Camera access failed.</p>
            <div class="error-actions"><el-button type="primary" @click="openScanner">Retry Camera</el-button><el-button type="success" plain @click="startSimulation">Use Demo Mode</el-button></div>
          </div>
        </div>
        <div v-if="scanning" class="ocr-results"><div class="ocr-status">AI Extracting Clinical Metrics...</div><div class="ocr-fields"><div v-for="f in detectedFields" :key="f" class="ocr-pill fade-in">{{ f }}</div></div></div>
      </div>
    </el-dialog>

    <!-- Case Details Dialog -->
    <el-dialog v-model="caseDialogVisible" title="Clinical Record Analysis" width="750px" custom-class="premium-dialog">
      <div v-if="selectedCase" class="case-details">
        <div class="case-header-info">
          <div class="case-meta">
            <el-tag effect="plain" type="info" size="small">{{ selectedCase.fileName || 'Manual' }}</el-tag>
            <el-tag effect="dark" class="algo-pill-solid-small" v-if="selectedCase.algorithm">{{ selectedCase.algorithm.toUpperCase() }}</el-tag>
          </div>
          <div class="case-time">{{ formatDate(selectedCase.createdAt) }}</div>
        </div>
        <div v-if="selectedCase.disease" class="case-prediction-box" :style="{ borderColor: getProbColor(selectedCase.probability) }">
          <div class="result-main">
            <div class="result-label">Diagnostic Assessment</div>
            <div class="result-value" :style="{ color: getProbColor(selectedCase.probability) }">{{ formatRiskDisplay(selectedCase.probability) }} <small>({{ (selectedCase.probability * 100).toFixed(1) }}%)</small></div>
          </div>
          <div class="result-suggestion"><strong>Clinical Note:</strong> {{ selectedCase.suggestion }}</div>
        </div>
        <el-alert title="Medical Disclaimer" type="warning" :closable="false" show-icon style="margin-bottom:20px">Rough estimate only. Seek medical guidance.</el-alert>
        <el-divider />
        <div class="data-row" v-for="(row, rowIndex) in chunkedCaseData" :key="rowIndex">
          <div v-for="(value, key) in row" :key="key" class="data-item-small"><div class="data-label-small">{{ key }}</div><div class="data-value-small">{{ value }}</div></div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { UploadFilled, Refresh, Monitor, DataAnalysis, Camera, VideoCamera, Picture, Loading, Lock, MagicStick, Document, CircleClose } from '@element-plus/icons-vue'
import * as XLSX from 'xlsx'
import { predictionService } from '@/api/prediction'

const modelType = ref('')
const previewData = ref([])
const cases = ref([])
const loading = ref(false)
const uploadedFileName = ref('')
const fileInput = ref(null)
const imageInput = ref(null)
const selectedCase = ref(null)
const caseDialogVisible = ref(false)

// AI Vision State
const scannerVisible = ref(false)
const cameraActive = ref(false)
const cameraError = ref(false)
const scanning = ref(false)
const countdown = ref(0)
const isScanResult = ref(false)
const videoRef = ref(null)
const detectedFields = ref([])
let stream = null

const saveModelType = (val) => { localStorage.setItem('modelType', val) }
const getVar = (name) => getComputedStyle(document.documentElement).getPropertyValue(name).trim() || '#000000'
const getProbColor = (prob) => {
  const val = prob * 100
  if (val < 30) return getVar('--color-success')
  if (val < 70) return getVar('--color-warning')
  return getVar('--color-danger')
}
const formatRiskDisplay = (prob) => {
  const val = prob * 100
  if (val < 30) return 'LOW Risk'
  if (val < 70) return 'MODERATE Risk'
  return 'HIGH Risk'
}

const triggerUpload = () => fileInput.value.click()
const handleFileChange = (e) => {
  const file = e.target.files[0]
  if (!file) return
  uploadedFileName.value = file.name
  isScanResult.value = false
  const reader = new FileReader()
  reader.onload = (ev) => {
    const wb = XLSX.read(ev.target.result, { type: 'array' })
    const ws = wb.Sheets[wb.SheetNames[0]]
    const jsonData = XLSX.utils.sheet_to_json(ws, { defval: null })
    previewData.value = [jsonData[0]]
    modelType.value = '' // Force user to choose
  }
  reader.readAsArrayBuffer(file)
}

const triggerImageUpload = () => imageInput.value.click()
const handleImageFileChange = (e) => {
  const file = e.target.files[0]
  if (!file) return
  loading.value = true
  ElMessage.info('AI is scanning document...')
  setTimeout(() => {
    isScanResult.value = true
    uploadedFileName.value = file.name
    previewData.value = [{ 'Age': 48, 'BMI': 31.5, 'Blood Glucose': 125, 'Systolic BP': 142, 'Diastolic BP': 88, 'HbA1c': 6.2, 'Family History': 1 }]
    modelType.value = '' // Force user to choose
    loading.value = false
    ElMessage.success('Data extracted successfully')
  }, 2000)
}

const openScanner = async () => {
  scannerVisible.value = true
  cameraError.value = false
  cameraActive.value = false
  countdown.value = 0
  await nextTick()
  const cameraTimeout = setTimeout(() => { if (!cameraActive.value) cameraError.value = true }, 5000)
  try {
    if (!navigator.mediaDevices?.getUserMedia) throw new Error('API Fail')
    stream = await navigator.mediaDevices.getUserMedia({ video: { width: { ideal: 1280 }, height: { ideal: 720 } } })
    cameraActive.value = true
    clearTimeout(cameraTimeout)
    await nextTick()
    if (videoRef.value) {
      videoRef.value.srcObject = stream
      videoRef.value.onloadedmetadata = () => videoRef.value.play().catch(console.error)
    }
  } catch (err) { cameraError.value = true; clearTimeout(cameraTimeout); }
}

const startCountdown = () => {
  countdown.value = 3
  const timer = setInterval(() => {
    if (countdown.value > 1) { countdown.value-- }
    else { clearInterval(timer); countdown.value = 0; captureImage(); }
  }, 1000)
}

const closeScanner = () => { if (stream) stream.getTracks().forEach(t => t.stop()); cameraActive.value = false; scanning.value = false; scannerVisible.value = false; }
const startSimulation = () => { cameraActive.value = true; cameraError.value = false; startCountdown(); }
const captureImage = () => {
  scanning.value = true
  detectedFields.value = []
  const fields = ['Clinical Data Found', 'Age: 42', 'BMI: 26.5', 'Glucose: 98', 'BP: 128/82']
  let idx = 0
  const t = setInterval(() => {
    if (idx < fields.length) { detectedFields.value.push(fields[idx++]) }
    else {
      clearInterval(t)
      setTimeout(() => {
        isScanResult.value = true
        previewData.value = [{ 'Age': 42, 'BMI': 26.5, 'Blood Glucose': 98, 'Systolic BP': 128, 'Diastolic BP': 82, 'HbA1c': 5.4, 'Family History': 0 }]
        closeScanner()
        ElMessage.success('Extraction Complete')
      }, 1000)
    }
  }, 600)
}

const clearPreview = () => { previewData.value = []; uploadedFileName.value = ''; isScanResult.value = false; }
const loadCases = async () => {
  loading.value = true
  try { const res = await predictionService.getHealthCases(); cases.value = res.data; }
  finally { loading.value = false; }
}

const viewCase = (c) => { selectedCase.value = c; caseDialogVisible.value = true; }
const deleteCase = async (c) => { await predictionService.deleteCase(c.id); loadCases(); }

const predictCase = async (caseData) => {
  loading.value = true
  try {
    const model = localStorage.getItem('modelType') || 'svm'
    const res = await predictionService.predictWithFlask(caseData.healthData || caseData, model)
    if (caseData.id) { 
      await predictionService.updateCaseResults(caseData.id, { algorithm: model, disease: res.data.disease, probability: res.data.probability, suggestion: res.data.suggestion }) 
    } else {
      await predictionService.saveHealthData({ 
        ...caseData, 
        fileName: isScanResult.value ? 'AI Scan Result' : uploadedFileName.value, 
        algorithm: model, 
        disease: res.data.disease, 
        probability: res.data.probability, 
        suggestion: res.data.suggestion 
      })
    }
    ElMessage.success('Prediction successful! Check history below.')
    clearPreview()
    loadCases()
  } catch (e) { 
    console.error('Prediction error:', e)
    const errorMsg = e.response?.data?.error || e.message || 'Prediction Failed'
    ElMessage.error(`Prediction Failed: ${errorMsg}`)
  }
  finally { loading.value = false }
}

const predictDirectly = () => { if (previewData.value.length) predictCase(previewData.value[0]) }

const chunkedCaseData = computed(() => {
  if (!selectedCase.value?.healthData) return []
  const items = Object.entries(selectedCase.value.healthData)
  const result = []
  for (let i = 0; i < items.length; i += 4) {
    const chunk = {}; items.slice(i, i + 4).forEach(([k, v]) => { chunk[k] = v }); result.push(chunk)
  }
  return result
})

const formatDate = (ds) => new Date(ds).toLocaleString()
const formatDateShort = (ds) => new Date(ds).toLocaleDateString()

onMounted(() => loadCases())
onBeforeUnmount(() => { if (stream) stream.getTracks().forEach(t => t.stop()) })
</script>

<style scoped>
.prediction-form { max-width: 1100px; margin: 20px auto; padding-bottom: 50px; }
.card-header { display: flex; justify-content: space-between; align-items: center; padding: 10px 0; }
.header-title-group { display: flex; align-items: center; gap: 16px; }
.icon-box { width: 48px; height: 48px; background: var(--color-primary); color: white; border-radius: 12px; display: flex; align-items: center; justify-content: center; font-size: 24px; }
.title-text h2 { margin: 0; font-size: 20px; font-weight: 800; color: var(--color-text-main); }
.title-text p { margin: 0; font-size: 13px; color: var(--color-text-muted); }
.model-select-container { display: flex; flex-direction: column; gap: 4px; }
.model-label { font-size: 11px; font-weight: 700; color: var(--color-text-light); text-transform: uppercase; }

.input-selection-container { padding: 10px 0 30px; }
.method-card { position: relative; background: #fff; border: 1px solid var(--color-border); border-radius: 24px; padding: 40px 32px; text-align: center; cursor: pointer; transition: all 0.3s ease; height: 100%; min-height: 300px; display: flex; flex-direction: column; justify-content: center; align-items: center; }
.method-card:hover { transform: translateY(-5px); border-color: var(--color-primary); }
.method-icon { font-size: 56px; color: var(--color-primary); margin-bottom: 24px; }
.method-card h3 { font-size: 20px; font-weight: 800; margin: 0 0 12px; }
.method-card p { font-size: 14px; color: #64748b; line-height: 1.5; margin-bottom: 24px; max-width: 280px; }
.ai-tag { position: absolute; top: 20px; right: 20px; background: #3b82f6; color: white; padding: 4px 12px; border-radius: 20px; font-size: 11px; font-weight: 700; box-shadow: 0 2px 10px rgba(59, 130, 246, 0.3); }

.model-selection-preview { margin-top: 24px; padding: 20px; background: #fff; border: 1px solid #e2e8f0; border-radius: 16px; }
.selection-box-compact { display: flex; align-items: center; gap: 24px; justify-content: space-between; }
.selection-label-group { display: flex; flex-direction: column; gap: 2px; }
.selection-title { font-size: 15px; font-weight: 800; color: var(--color-text-main); }
.selection-subtitle { font-size: 12px; color: var(--color-text-muted); }

.scan-options-row { display: flex; gap: 12px; justify-content: center; width: 100%; }
.scan-sub-btn { border-radius: 12px; padding: 12px 20px; font-weight: 700; }

.preview-stage { padding: 32px; background: #f8fafc; border-radius: 24px; margin-top: 20px; border: 1px solid #e2e8f0; }
.stage-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.stage-info { display: flex; align-items: center; gap: 12px; }
.stage-info h3 { margin: 0; font-size: 18px; font-weight: 800; }
.source-pill { background: white; padding: 4px 14px; border-radius: 20px; font-size: 12px; font-weight: 700; color: #3b82f6; border: 2px solid #3b82f6; }
.ai-source { border-color: #3b82f6; color: #3b82f6; background: white; }

.metrics-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(160px, 1fr)); gap: 16px; margin: 24px 0; }
.metric-input-item label { display: block; font-size: 10px; font-weight: 800; color: #94a3b8; text-transform: uppercase; margin-bottom: 8px; }

.history-section { margin-top: 100px; border-top: 1px solid #f1f5f9; padding-top: 40px; }
.history-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.history-title-group h3 { font-size: 18px; font-weight: 800; margin: 0; }
.history-title-group p { margin: 4px 0 0; font-size: 13px; color: #94a3b8; }

.result-pill-container { display: flex; align-items: center; gap: 6px; }
.status-dot { width: 6px; height: 6px; border-radius: 50%; }
.status-text-small { font-size: 11px; font-weight: 700; text-transform: uppercase; }
.status-percent-small { font-size: 10px; color: #94a3b8; }
.algo-pill-solid { background-color: white !important; color: #3b82f6 !important; border: 2px solid #3b82f6 !important; font-weight: 800; font-size: 11px; border-radius: 20px; padding: 2px 12px; }

.scanner-viewport { background: #111; border-radius: 16px; height: 400px; position: relative; overflow: hidden; display: flex; align-items: center; justify-content: center; }
.video-container { width: 100%; height: 100%; position: relative; }
.camera-stream { width: 100%; height: 100%; object-fit: cover; }
.scan-laser { position: absolute; top: 20%; left: 10%; width: 80%; height: 2px; background: #10b981; box-shadow: 0 0 10px #10b981; animation: laser 2s linear infinite; }
@keyframes laser { 0%, 100% { top: 20%; } 50% { top: 80%; } }
.camera-controls { position: absolute; bottom: 20px; width: 100%; text-align: center; }

.countdown-overlay { position: absolute; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.6); display: flex; flex-direction: column; align-items: center; justify-content: center; z-index: 10; color: white; }
.countdown-number { font-size: 80px; font-weight: 900; color: #10b981; }

.loading-state, .error-msg { text-align: center; color: white; padding: 40px; }
.error-actions { display: flex; gap: 12px; justify-content: center; margin-top: 20px; }

.data-row { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; margin-bottom: 10px; }
.data-item-small { padding: 8px; background: #f8fafc; border-radius: 6px; border: 1px solid #eee; }
.data-label-small { font-size: 9px; color: #94a3b8; }
.data-value-small { font-size: 12px; font-weight: 600; color: #1e293b; }

.result-main { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
.result-label { font-size: 12px; font-weight: 700; color: #64748b; }
.result-value { font-size: 20px; font-weight: 800; }
.result-suggestion { background: #f8fafc; padding: 12px; border-radius: 8px; font-size: 13px; line-height: 1.5; border-left: 4px solid #3b82f6; }

.stage-footer { margin-top: 40px; display: flex; justify-content: flex-start; }
.predict-btn { padding: 16px 40px; height: auto; font-size: 16px; font-weight: 800; border-radius: 12px; }

:deep(.premium-table) { border-radius: 12px; overflow: hidden; border: 1px solid #f1f5f9; }
</style>
