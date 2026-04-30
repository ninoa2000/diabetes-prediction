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
          将检查报告拖到此处，或<em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">
            请上传 Excel 格式的检查报告，支持 .xlsx 和 .xls 格式
          </div>
        </template>
      </el-upload>

      <div v-if="previewData.length > 0" class="preview-section">
        <div class="preview-header">
          <h3>数据预览</h3>
          <el-button link type="primary" @click="clearPreview">
            <el-icon><refresh /></el-icon>
            重新上传
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
            v-if="!selectedCase?.disease"
            type="primary"
            @click="submitData"
          >
            保存病历
          </el-button>
          <el-button
            v-else
            type="success"
            disabled
          >
            已预测
          </el-button>
        </div>
      </div>

      <!-- 病例列表 -->
      <div v-if="!previewData.length && cases.length > 0" class="cases-section">
        <h3>已保存的病例</h3>
        <el-table :data="cases" style="width: 100%" v-loading="loading">
          <el-table-column prop="createdAt" label="上传时间" width="180">
            <template #default="scope">
              {{ formatDate(scope.row.createdAt) }}
            </template>
          </el-table-column>
          <el-table-column prop="disease" label="预测结果">
            <template #default="scope">
              <span v-if="scope.row.disease">
                {{ scope.row.disease }}
                ({{ (scope.row.probability * 100).toFixed(2) }}%)
              </span>
              <span v-else>未预测</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="280">
            <template #default="scope">
              <el-button type="primary" link @click="viewCase(scope.row)">
                查看详情
              </el-button>
              <el-button
                v-if="!scope.row.disease"
                type="success"
                link
                @click="predictCase(scope.row)"
              >
                开始预测
              </el-button>
              <el-button type="danger" link @click="deleteCase(scope.row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 病例详情对话框 -->
      <el-dialog v-model="caseDialogVisible" title="病例详情" width="70%">
        <div v-if="selectedCase" class="case-details">
          <div class="data-row" v-for="(row, rowIndex) in chunkedCaseData" :key="rowIndex">
            <div v-for="(value, key) in row" :key="key" class="data-item">
              <div class="data-label">{{ key }}</div>
              <div class="data-value">{{ value }}</div>
            </div>
          </div>

          <div v-if="selectedCase.disease" class="prediction-result">
            <h4>预测结果</h4>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="预测疾病">
                {{ selectedCase.disease }}
              </el-descriptions-item>
              <el-descriptions-item label="预测概率">
                {{ (selectedCase.probability * 100).toFixed(2) }}%
              </el-descriptions-item>
              <el-descriptions-item label="建议">
                {{ selectedCase.suggestion }}
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </div>
      </el-dialog>

      <!-- 预测结果弹窗 -->
      <el-dialog
        v-model="predictionDialogVisible"
        title="预测结果"
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
              <h3>预测疾病：{{ predictionDisease }}</h3>
              <p>预测概率：{{ predictionPercentage.toFixed(1) }}%</p>
              <p>风险等级：{{ riskLevel }}</p>
            </div>
            <div class="suggestion-box">
              <h3>健康建议</h3>
              <div class="suggestion-content" v-html="formattedSuggestion"></div>
            </div>
          </div>
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="closePredictionDialog">关闭</el-button>
          </span>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { UploadFilled, Refresh } from '@element-plus/icons-vue'
import * as XLSX from 'xlsx'
import { predictionService } from '@/api/prediction'

const expectedColumns = 35
const previewData = ref([])
const cases = ref([])
const loading = ref(false)
const caseDialogVisible = ref(false)
const selectedCase = ref(null)
const predictionDialogVisible = ref(false)
const predictionPercentage = ref(0)
const predictionInterval = ref(null)
const predictionDisease = ref('')
const predictionSuggestion = ref('')

/**
 * ✅ Model selection state (saved in localStorage)
 */
const modelType = ref(localStorage.getItem('modelType') || 'svm')
const saveModelType = (val) => {
  localStorage.setItem('modelType', val)
}

// 将数据分成每行3个的数组
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

// 病例详情数据
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

    // 1. 直接从工作表的 ref（范围）里解码出真实列数
    const range = XLSX.utils.decode_range(ws['!ref'])
    const actualColumns = range.e.c - range.s.c + 1
    if (actualColumns !== expectedColumns) {
      ElMessage.error('上传数据缺失，请重新检查后上传')
      return
    }

    // 2. 再转成 JSON
    const jsonData = XLSX.utils.sheet_to_json(ws, { defval: null })
    if (!jsonData.length) {
      ElMessage.error('Excel 文件为空')
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
    ElMessage.warning('请先上传数据')
    return
  }

  try {
    loading.value = true
    const user = JSON.parse(localStorage.getItem('user'))
    if (!user || !user.id) {
      ElMessage.error('用户未登录')
      return
    }

    const requestData = {
      ...previewData.value[0],
      userId: user.id
    }

    await predictionService.saveHealthData(requestData)
    ElMessage.success('病例保存成功')
    clearPreview()
  } catch (error) {
    console.error('保存病例失败:', error)
    ElMessage.error(error.message || '保存病例失败')
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
    console.error('加载病例失败:', error)
    ElMessage.error('加载病例失败')
  } finally {
    loading.value = false
  }
}

const viewCase = (caseData) => {
  selectedCase.value = caseData
  caseDialogVisible.value = true
}

// 计算风险等级
const riskLevel = computed(() => {
  if (predictionPercentage.value < 30) return '低风险'
  if (predictionPercentage.value < 70) return '中风险'
  return '高风险'
})

// 计算进度条颜色
const customColors = computed(() => {
  if (predictionPercentage.value < 30) return '#67C23A'
  if (predictionPercentage.value < 70) return '#E6A23C'
  return '#F56C6C'
})

const format = (percentage) => percentage.toFixed(2) + '%'

const formattedSuggestion = computed(() => {
  return predictionSuggestion.value.replace(/\n/g, '<br>')
})

const closePredictionDialog = () => {
  predictionDialogVisible.value = false
  predictionPercentage.value = 0
  predictionDisease.value = ''
  predictionSuggestion.value = ''
  if (predictionInterval.value) clearInterval(predictionInterval.value)
}

/**
 * ✅ Predict case (next step: send modelType to backend)
 * Right now, dropdown is saved in localStorage.
 * Next we will update prediction.js to pass it.
 */
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

    // NOTE: modelType is saved in localStorage for now.
    // Next step we will send it in API call.
    const selectedModel = localStorage.getItem('modelType') || 'svm'
    const response = await predictionService.predictWithFlask(caseData.healthData || caseData, selectedModel)


    clearInterval(predictionInterval.value)

    predictionPercentage.value = response.data.probability * 100
    predictionDisease.value = response.data.disease
    predictionSuggestion.value = response.data.suggestion

    await loadCases()
  } catch (error) {
    console.error('预测失败:', error)
    ElMessage.error(error.message || '预测失败')
    closePredictionDialog()
  } finally {
    loading.value = false
  }
}

const deleteCase = async (caseData) => {
  try {
    await ElMessageBox.confirm('确定要删除这个病例吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    loading.value = true
    await predictionService.deleteCase(caseData.id)
    await loadCases()
    ElMessage.success('删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除病例失败:', error)
      ElMessage.error(error.message || '删除病例失败')
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
</style>
