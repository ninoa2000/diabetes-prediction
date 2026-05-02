<template>
  <el-card class="knowledge-manage-card">
    <!-- Top Toolbar -->
    <div class="toolbar">
      <el-button type="primary" icon="Plus" @click="openDialog(activeTab)">
        New {{ currentTabLabel }}
      </el-button>
    </div>

    <!-- Category Tabs -->
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
          :empty-text="`No ${tab.label} available`"
          row-key="id"
          stripe
          border
        >
          <!-- Dynamic Columns -->
          <el-table-column
            v-for="col in columnsMap[tab.key]"
            :key="col.prop"
            :prop="col.prop"
            :label="col.label"
            :width="col.width"
            :show-overflow-tooltip="!col.noEllipsis"
          />
          <el-table-column label="Actions" width="200" fixed="right">
            <template #default="{ row }">
              <el-button link type="primary" size="small" @click="openDialog(tab.key, row)">Edit</el-button>
              <el-button link type="danger" size="small" @click="handleDelete(tab.key, row)">Delete</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- Dialog -->
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
        label-width="130px"
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
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitForm">Save</el-button>
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

/* ---------- 1. Tab Configuration ---------- */
const tabs = [
  { key: 'chronic', label: 'Diabetes Knowledge' },
  { key: 'nutrition', label: 'Health Guide' },
  { key: 'research', label: 'Latest Research' },
  { key: 'habits', label: 'Health Habits' }
]
const activeTab = ref('chronic')
const currentTabLabel = computed(() => tabs.find(t => t.key === activeTab.value)?.label)

/* ---------- 2. Table Column Configuration ---------- */
const columnsMap = {
  chronic: [
    { prop: 'name', label: 'Name', width: 160 },
    { prop: 'description', label: 'Description' },
    { prop: 'symptoms', label: 'Symptoms', noEllipsis: true },
    { prop: 'causes', label: 'Causes', noEllipsis: true },
    { prop: 'prevention', label: 'Prevention', noEllipsis: true },
    { prop: 'treatment', label: 'Treatment', noEllipsis: true },
    { prop: 'order', label: 'Order', width: 80 }
  ],
  nutrition: [
    { prop: 'title', label: 'Title', width: 200 },
    { prop: 'category', label: 'Category', width: 120 },
    { prop: 'content', label: 'Content' },
    { prop: 'order', label: 'Order', width: 80 }
  ],
  research: [
    { prop: 'title', label: 'Title', width: 220 },
    { prop: 'source', label: 'Source', width: 120 },
    { prop: 'publishDate', label: 'Publish Date', width: 140 },
    { prop: 'content', label: 'Content', noEllipsis: true }
  ],
  habits: [
    { prop: 'title', label: 'Title', width: 200 },
    { prop: 'category', label: 'Category', width: 200 },
    { prop: 'content', label: 'Tips Content' },
    { prop: 'order', label: 'Order', width: 80 }
  ]
}

/* ---------- 3. Data & Loading ---------- */
const dataList = reactive({ chronic: [], nutrition: [], research: [], habits: [] })
const loading = reactive({ chronic: false, nutrition: false, research: false, habits: false })
async function loadList(key) {
  loading[key] = true
  try {
    const api = { chronic: listChronic, nutrition: listNutrition, research: listResearch, habits: listHabit }[key]
    dataList[key] = await api()
  } catch (e) {
    ElMessage.error('Failed to load')
  } finally { loading[key] = false }
}
onMounted(() => loadList(activeTab.value))
watch(activeTab, (tab) => loadList(tab))

/* ---------- 4. Form Field Configuration ---------- */
const formData = reactive({})
const dialogVisible = ref(false)
const dialogMode = ref('create')
const dialogTitle = computed(() => `${dialogMode.value === 'create' ? 'New' : 'Edit'} ${currentTabLabel.value}`)
const formRef = ref()

const genField = (prop, label, component = 'el-input', attrs = {}) => ({ prop, label, component, attrs })
const fieldConfigs = {
  chronic: [
    genField('name', 'Name'),
    genField('description', 'Description', 'el-input', { type: 'textarea', autosize: { minRows: 3, maxRows: 6 } }),
    genField('symptoms', 'Symptoms', 'el-input', { type: 'textarea', autosize: { minRows: 2, maxRows: 4 } }),
    genField('causes', 'Causes', 'el-input', { type: 'textarea', autosize: { minRows: 2, maxRows: 4 } }),
    genField('prevention', 'Prevention', 'el-input', { type: 'textarea', autosize: { minRows: 2, maxRows: 4 } }),
    genField('treatment', 'Treatment', 'el-input', { type: 'textarea', autosize: { minRows: 2, maxRows: 4 } }),
    genField('order', 'Order', 'el-input-number', { min: 1 })
  ],
  nutrition: [
    genField('title', 'Title'),
    genField('content', 'Content', 'el-input', { type: 'textarea', autosize: { minRows: 4, maxRows: 8 } }),
    genField('category', 'Category'),
    genField('order', 'Order', 'el-input-number', { min: 1 })
  ],
  research: [
    genField('title', 'Title'),
    genField('content', 'Content', 'el-input', { type: 'textarea', autosize: { minRows: 4, maxRows: 8 } }),
    genField('source', 'Source'),
    genField('publishDate', 'Publish Date', 'el-date-picker', { type: 'date', 'value-format': 'YYYY-MM-DD' }),
    genField('order', 'Order', 'el-input-number', { min: 1 })
  ],
  habits: [
    genField('title', 'Title'),
    genField('category', 'Category'),
    genField('content', 'Tips Content', 'el-input', { type: 'textarea', autosize: { minRows: 3, maxRows: 6 } }),
    genField('order', 'Order', 'el-input-number', { min: 1 })
  ]
}
const currentFields = computed(() => fieldConfigs[activeTab.value])

const formRules = {
  name: [{ required: true, message: 'Name cannot be empty', trigger: 'blur' }],
  description: [{ required: true, message: 'Description cannot be empty', trigger: 'blur' }]
}

/* ---------- 5. Open / Submit Form ---------- */
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
    ElMessage.success('Saved successfully')
    dialogVisible.value = false
    await loadList(activeTab.value)
  } catch (_) {
    ElMessage.error('Failed to save')
  }
}

/* ---------- 6. Delete ---------- */
async function handleDelete(key, row) {
  try {
    await ElMessageBox.confirm('Are you sure you want to delete this item?', 'Notice', { type: 'warning' })
    await { chronic: deleteChronic, nutrition: deleteNutrition, research: deleteResearch, habits: deleteHabit }[key](row.id)
    ElMessage.success('Deleted successfully')
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


<style scoped>
.knowledge-manage-card { background: #fff; padding: 20px; border-radius: 8px; }
.toolbar { margin-bottom: 16px; }
.custom-tabs ::v-deep .el-tabs__header { background: #f9f9f9; border-radius: 4px; }
.edit-dialog ::v-deep .el-dialog__body { max-height: 60vh; overflow-y: auto; padding: 16px; }
.edit-form { margin-top: 8px; }
</style>
