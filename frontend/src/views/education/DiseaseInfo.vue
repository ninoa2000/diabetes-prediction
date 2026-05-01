<template>
  <el-container>
    <el-main class="content-wrapper">
      <h1 class="main-title">Chronic Disease Information</h1>

      <el-card
        v-for="item in diseases"
        :key="item.id"
        class="disease-card"
        shadow="hover"
      >
        <el-collapse v-model="activeNames" accordion>
          <el-collapse-item :name="item.id">
            <!-- 标题插槽 -->
            <template #title>
              <span class="collapse-title">{{ item.name }}</span>
            </template>

            <!-- Overview -->
            <el-divider content-position="left">Overview</el-divider>
            <p class="section-text">{{ item.description }}</p>

            <!-- Symptoms -->
            <el-divider content-position="left">Symptoms</el-divider>
            <div class="tag-group">
              <el-tag
                v-for="(sym, idx) in formatList(item.symptoms)"
                :key="idx"
                effect="plain"
                style="background-color: #FFD54F; color: #333; border: none;"
              >
                {{ sym }}
              </el-tag>
            </div>

            <!-- Causes -->
            <el-divider content-position="left">Causes</el-divider>
            <p class="section-text">{{ item.causes }}</p>

            <!-- Prevention Measures -->
            <el-divider content-position="left">Prevention Measures</el-divider>
            <p class="section-text">{{ item.prevention }}</p>

            <!-- Treatment Methods -->
            <el-divider content-position="left">Treatment Methods</el-divider>
            <p class="section-text">{{ item.treatment }}</p>
          </el-collapse-item>
        </el-collapse>
      </el-card>
    </el-main>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const diseases = ref([]);
const activeNames = ref([]);

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
  } catch (e) {
    console.error(e);
    ElMessage.error('Failed to load disease data');
  }
}

onMounted(loadDiseases);
</script>

<style scoped>
.content-wrapper {
  padding: 32px 16px;
  max-width: 960px;
  margin: 0 auto;
}
.main-title {
  font-size: 2rem;
  font-weight: 700;
  text-align: center;
  margin-bottom: 24px;
}
.disease-card {
  margin-bottom: 24px;
  border-radius: 8px;
}
.collapse-title {
  font-size: 1.25rem;
  font-weight: 600;
}
.section-text {
  line-height: 1.6;
  color: #595959;
  margin-bottom: 16px;
}
.tag-group {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
}
</style>
