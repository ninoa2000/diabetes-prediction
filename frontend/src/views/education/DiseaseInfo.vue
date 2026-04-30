<template>
  <el-container>
    <el-main class="content-wrapper">
      <h1 class="main-title">慢性疾病知识</h1>

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

            <!-- 概述 -->
            <el-divider content-position="left">概述</el-divider>
            <p class="section-text">{{ item.description }}</p>

            <!-- 症状 -->
            <el-divider content-position="left">症状</el-divider>
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

            <!-- 病因 -->
            <el-divider content-position="left">病因</el-divider>
            <p class="section-text">{{ item.causes }}</p>

            <!-- 预防措施 -->
            <el-divider content-position="left">预防措施</el-divider>
            <p class="section-text">{{ item.prevention }}</p>

            <!-- 治疗方式 -->
            <el-divider content-position="left">治疗方式</el-divider>
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
    ElMessage.error('加载疾病数据失败');
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
