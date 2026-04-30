<template>
  <el-card class="knowledge-manage-card">
    <!-- 顶部工具栏 -->
    <div class="toolbar">
      <el-button type="primary" icon="Plus" @click="openDialog(activeTab)">
        新建{{ currentTabLabel }}
      </el-button>
    </div>

    <!-- 分类页签 -->
    <el-tabs v-model="activeTab" type="border-card" class="custom-tabs">
      <el-tab-pane
        v-for="tab in tabs"
        :key="tab.key"
        :label="tab.label"
        :name="tab.key"
      >
        <el-table
          :data="dataList[tab.key]"
          v-loading="loading[tab.key]"
          :empty-text="`暂无${tab.label}`"
          row-key="id"
          stripe
          border
        >
          <!-- 动态列 -->
          <el-table-column
            v-for="col in columnsMap[tab.key]"
            :key="col.prop"
            :prop="col.prop"
            :label="col.label"
            :width="col.width"
            :show-overflow-tooltip="!col.noEllipsis"
          />
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button link type="primary" size="small" @click="openDialog(tab.key, row)">编辑</el-button>
              <el-button link type="danger" size="small" @click="handleDelete(tab.key, row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="720px"
      draggable
      class="edit-dialog"
      @close="resetDialog"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="110px"
        class="edit-form"
      >
        <el-row :gutter="20">
          <el-col v-for="field in currentFields" :key="field.prop" :span="24">
            <el-form-item :label="field.label" :prop="field.prop">
              <component
                :is="field.component"
                v-model="formData[field.prop]"
                v-bind="field.attrs"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  listChronic, createChronic, updateChronic, deleteChronic,
  listNutrition, createNutrition, updateNutrition, deleteNutrition,
  listResearch, createResearch, updateResearch, deleteResearch,
  listHabit, createHabit, updateHabit, deleteHabit
} from '../../api/Knowledge'

/* ---------- 1. 选项卡配置 ---------- */
const tabs = [
  { key: 'chronic', label: '糖尿病知识' },
  { key: 'nutrition', label: '健康生活指南' },
  { key: 'research', label: '最新研究资讯' },
  { key: 'habits', label: '健康爱好' }
]
const activeTab = ref('chronic')
const currentTabLabel = computed(() => tabs.find(t => t.key === activeTab.value)?.label)

/* ---------- 2. 表格列配置 ---------- */
const columnsMap = {
  chronic: [
    { prop: 'name', label: '名称', width: 160 },
    { prop: 'description', label: '描述' },
    { prop: 'symptoms', label: '症状', noEllipsis: true },
    { prop: 'causes', label: '病因', noEllipsis: true },
    { prop: 'prevention', label: '预防措施', noEllipsis: true },
    { prop: 'treatment', label: '治疗方式', noEllipsis: true },
    { prop: 'order', label: '排序', width: 80 }
  ],
  nutrition: [
    { prop: 'title', label: '标题', width: 200 },
    { prop: 'category', label: '分类', width: 120 },
    { prop: 'content', label: '内容' },
    { prop: 'order', label: '排序', width: 80 }
  ],
  research: [
    { prop: 'title', label: '标题', width: 220 },
    { prop: 'source', label: '来源', width: 120 },
    { prop: 'publishDate', label: '发布日期', width: 140 },
    { prop: 'content', label: '内容', noEllipsis: true }
  ],
  habits: [
    { prop: 'title', label: '标题', width: 200 },
    { prop: 'category', label: '分类', width: 200 },
    { prop: 'content', label: '提示内容' },
    { prop: 'order', label: '排序', width: 80 }
  ]
}

/* ---------- 3. 数据与加载 ---------- */
const dataList = reactive({ chronic: [], nutrition: [], research: [], habits: [] })
const loading = reactive({ chronic: false, nutrition: false, research: false, habits: false })
async function loadList(key) {
  loading[key] = true
  try {
    const api = { chronic: listChronic, nutrition: listNutrition, research: listResearch, habits: listHabit }[key]
    dataList[key] = await api()
  } catch (e) {
    ElMessage.error('加载失败')
  } finally { loading[key] = false }
}
onMounted(() => loadList(activeTab.value))
watch(activeTab, (tab) => loadList(tab))

/* ---------- 4. 表单字段配置 ---------- */
const formData = reactive({})
const dialogVisible = ref(false)
const dialogMode = ref('create')
const dialogTitle = computed(() => `${dialogMode.value === 'create' ? '新建' : '编辑'} ${currentTabLabel.value}`)
const formRef = ref()

const genField = (prop, label, component = 'el-input', attrs = {}) => ({ prop, label, component, attrs })
const fieldConfigs = {
  chronic: [
    genField('name', '名称'),
    genField('description', '描述', 'el-input', { type: 'textarea', autosize: { minRows: 3, maxRows: 6 } }),
    genField('symptoms', '症状', 'el-input', { type: 'textarea', autosize: { minRows: 2, maxRows: 4 } }),
    genField('causes', '病因', 'el-input', { type: 'textarea', autosize: { minRows: 2, maxRows: 4 } }),
    genField('prevention', '预防措施', 'el-input', { type: 'textarea', autosize: { minRows: 2, maxRows: 4 } }),
    genField('treatment', '治疗方式', 'el-input', { type: 'textarea', autosize: { minRows: 2, maxRows: 4 } }),
    genField('order', '排序', 'el-input-number', { min: 1 })
  ],
  nutrition: [
    genField('title', '标题'),
    genField('content', '内容', 'el-input', { type: 'textarea', autosize: { minRows: 4, maxRows: 8 } }),
    genField('category', '分类'),
    genField('order', '排序', 'el-input-number', { min: 1 })
  ],
  research: [
    genField('title', '标题'),
    genField('content', '内容', 'el-input', { type: 'textarea', autosize: { minRows: 4, maxRows: 8 } }),
    genField('source', '来源'),
    genField('publishDate', '发布日期', 'el-date-picker', { type: 'date', 'value-format': 'YYYY-MM-DD' }),
    genField('order', '排序', 'el-input-number', { min: 1 })
  ],
  habits: [
    genField('title', '标题'),
    genField('category', '分类'),
    genField('content', '提示内容', 'el-input', { type: 'textarea', autosize: { minRows: 3, maxRows: 6 } }),
    genField('order', '排序', 'el-input-number', { min: 1 })
  ]
}
const currentFields = computed(() => fieldConfigs[activeTab.value])

const formRules = {
  name: [{ required: true, message: '名称不能为空', trigger: 'blur' }],
  description: [{ required: true, message: '描述不能为空', trigger: 'blur' }]
}

/* ---------- 5. 打开 / 提交表单 ---------- */
function openDialog(key, row) {
  activeTab.value = key
  dialogMode.value = row ? 'edit' : 'create'
  Object.keys(formData).forEach(k => delete formData[k])
  Object.assign(formData, row || {})
  dialogVisible.value = true
}
function resetDialog() {
  formRef.value?.resetFields()
}
async function submitForm() {
  await formRef.value.validate()
  try {
    if (dialogMode.value === 'create') {
      await { chronic: createChronic, nutrition: createNutrition, research: createResearch, habits: createHabit }[activeTab.value](formData)
    } else {
      await { chronic: updateChronic, nutrition: updateNutrition, research: updateResearch, habits: updateHabit }[activeTab.value](formData.id, formData)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    await loadList(activeTab.value)
  } catch (_) {
    ElMessage.error('保存失败')
  }
}

/* ---------- 6. 删除 ---------- */
async function handleDelete(key, row) {
  try {
    await ElMessageBox.confirm('确认删除该条目？', '提示', { type: 'warning' })
    await { chronic: deleteChronic, nutrition: deleteNutrition, research: deleteResearch, habits: deleteHabit }[key](row.id)
    ElMessage.success('删除成功')
    loadList(key)
  } catch (_) {}
}
</script>

<style scoped>
.knowledge-manage-card { background: #fff; padding: 20px; border-radius: 8px; }
.toolbar { margin-bottom: 16px; }
.custom-tabs ::v-deep .el-tabs__header { background: #f9f9f9; border-radius: 4px; }
.edit-dialog ::v-deep .el-dialog__body { max-height: 60vh; overflow-y: auto; padding: 16px; }
.edit-form { margin-top: 8px; }
</style>
