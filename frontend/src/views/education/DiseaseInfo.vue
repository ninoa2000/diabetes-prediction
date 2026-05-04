<template>
  <el-container>
    <el-main>
      <h1 class="text-h4 mb-6">Diabetes Info</h1>

      <el-row :gutter="20">
        <el-col :xs="24" :md="16">
          <p class="text-body-1 mb-4">
            Comprehensive knowledge to help you understand, prevent, and manage major health conditions. Select a medical condition below to learn more about its overview, symptoms, causes, and recommended treatments.
          </p>
        </el-col>
        <el-col :xs="24" :md="8">
          <el-image
            src="https://images.unsplash.com/photo-1579154204601-01588f351e67?auto=format&fit=crop&w=800&q=80"
            style="height: 200px; width: 100%"
            fit="cover"
            class="rounded-lg shadow-sm"
          ></el-image>
        </el-col>
      </el-row>

      <el-divider class="my-6"></el-divider>

      <div v-if="diseases.length > 0">
        <el-tabs v-model="activeTab" type="card" class="text-center">
          <el-tab-pane 
            v-for="(item, index) in diseases" 
            :key="item.id" 
            :label="item.name" 
            :name="String(item.id)" 
          />
        </el-tabs>

        <div class="mt-5">
          <div v-for="disease in diseases" :key="disease.id">
            <div v-if="activeTab === String(disease.id)">
              <el-row :gutter="20">
                <el-col :span="24">
                  <h2 class="text-h5 mb-4">Overview</h2>
                  <p class="text-body-1 mb-6">{{ disease.description }}</p>
                  
                  <el-card>
                    <el-collapse v-model="activeCollapse">
                      <el-collapse-item name="1">
                        <template #title>
                          <div class="collapse-title">
                            <el-icon class="mr-2 text-warning"><WarningFilled /></el-icon>
                            Common Symptoms
                          </div>
                        </template>
                        <div class="tag-group pt-2 pb-2">
                          <el-tag
                            v-for="(sym, idx) in formatList(disease.symptoms)"
                            :key="idx"
                            effect="light"
                            type="warning"
                            class="symptom-tag mr-2 mb-2"
                            round
                          >
                            {{ sym }}
                          </el-tag>
                        </div>
                      </el-collapse-item>

                      <el-collapse-item name="2">
                        <template #title>
                          <div class="collapse-title">
                            <el-icon class="mr-2 text-danger"><Aim /></el-icon>
                            Underlying Causes
                          </div>
                        </template>
                        <p class="pt-2 pb-2 text-body-1">{{ disease.causes }}</p>
                      </el-collapse-item>

                      <el-collapse-item name="3">
                        <template #title>
                          <div class="collapse-title">
                            <el-icon class="mr-2 text-success"><SuccessFilled /></el-icon>
                            Prevention Strategies
                          </div>
                        </template>
                        <p class="pt-2 pb-2 text-body-1">{{ disease.prevention }}</p>
                      </el-collapse-item>

                      <el-collapse-item name="4">
                        <template #title>
                          <div class="collapse-title">
                            <el-icon class="mr-2 text-primary"><FirstAidKit /></el-icon>
                            Treatment Options
                          </div>
                        </template>
                        <p class="pt-2 pb-2 text-body-1">{{ disease.treatment }}</p>
                      </el-collapse-item>
                    </el-collapse>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </div>
        </div>
      </div>
      
      <div v-else class="text-center mt-5">
        <el-skeleton :rows="10" animated />
      </div>

    </el-main>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import { Aim, SuccessFilled, FirstAidKit, WarningFilled } from '@element-plus/icons-vue';

const diseases = ref([]);
const activeTab = ref('');
const activeCollapse = ref(['1']);

// 将逗号、顿号等分隔的字符串转数组
function formatList(val) {
  if (!val) return [];
  return Array.isArray(val)
    ? val
    : String(val)
        .split(/[，、,；;]+/)
        .filter((s) => s.trim());
}

async function loadDiseases() {
  try {
    const res = await axios.get('/api/chronic');
    diseases.value = Array.isArray(res.data) ? res.data : [];
    if (diseases.value.length > 0) {
      activeTab.value = String(diseases.value[0].id);
    }
  } catch (e) {
    console.error(e);
    ElMessage.error('Failed to load disease data');
  }
}

onMounted(loadDiseases);
</script>

<style scoped>
.text-center { text-align: center; }
.text-h4 { font-size: 2.125rem; font-weight: 500; margin-bottom: 24px; }
.text-h5 { font-size: 1.5rem; font-weight: 500; margin-bottom: 16px; }
.text-body-1 { font-size: 1rem; line-height: 1.6; color: #475569; }
.mb-6 { margin-bottom: 24px; }
.mb-4 { margin-bottom: 16px; }
.mb-2 { margin-bottom: 8px; }
.mt-5 { margin-top: 20px; }
.my-6 { margin: 24px 0; }
.mr-2 { margin-right: 8px; }
.pt-2 { padding-top: 8px; }
.pb-2 { padding-bottom: 8px; }

.rounded-lg { border-radius: 8px; overflow: hidden; }
.shadow-sm { box-shadow: 0 4px 6px rgba(0,0,0,0.05); }

.collapse-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #1e293b;
  display: flex;
  align-items: center;
}

.text-warning { color: #d97706; }
.text-danger { color: #dc2626; }
.text-success { color: #16a34a; }
.text-primary { color: #2563eb; }

.tag-group {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.symptom-tag {
  font-size: 0.95rem;
  padding: 6px 16px;
  height: auto;
}

/* Adjust element plus collapse style */
:deep(.el-collapse-item__header) {
  font-size: 1.05rem;
}
</style>
