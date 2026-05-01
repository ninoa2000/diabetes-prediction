<template>
  <el-container>
    <el-main>
      <h1 class="main-title mb-6">Chronic Disease Research Information</h1>

      <p class="description mb-8">
        This page brings together the latest research findings and medical progress on chronic disease prevention and management. 
        We update content regularly to provide you with the latest discoveries from authoritative medical journals and institutions.
      </p>

      <el-card class="mb-6">
        <template #header>
          <div class="card-header">Research Progress</div>
        </template>
        <el-timeline>
          <el-timeline-item
            v-for="item in pagedList"
            :key="item.id"
            :timestamp="formatDate(item.publishDate)"
            placement="top"
          >
            <div class="timeline-header">
              <el-tag size="small" type="info">{{ item.source }}</el-tag>
            </div>
            <el-card class="mb-2">
              <template #header>
                <div class="font-weight-bold">{{ item.title }}</div>
              </template>
              <p>{{ item.content }}</p>
              <div class="action-row">
                <el-tag size="small" type="info">{{ item.type || 'Research' }}</el-tag>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-card>

      <div class="text-center">
        <el-pagination
          :current-page="currentPage"
          :page-size="pageSize"
          :total="researchList.length"
          layout="prev, pager, next"
          @current-change="onPageChange"
        />
      </div>

    </el-main>
  </el-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { listResearch } from '../../api/Knowledge'; // 后端接口

const researchList = ref([]);
const currentPage = ref(1);
const pageSize = ref(5);

async function loadResearch() {
  try {
    researchList.value = await listResearch();
  } catch (e) {
    console.error('Failed to load research data', e);
  }
}

onMounted(loadResearch);

const pagedList = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return researchList.value.slice(start, start + pageSize.value);
});

function onPageChange(page) {
  currentPage.value = page;
}

function formatDate(date) {
  return date ? date.split('T')[0] : '';
}
</script>

<style scoped>
.main-title {
  font-size: 2.125rem;
  font-weight: 500;
}
.description {
  font-size: 1rem;
  line-height: 1.5;
  margin-bottom: 32px;
}
.card-header {
  background-color: var(--el-color-primary);
  color: white;
  padding: 12px 20px;
  font-weight: bold;
}
.timeline-header {
  margin-bottom: 8px;
}
.font-weight-bold {
  font-weight: bold;
}
.action-row {
  margin-top: 12px;
}
.mb-2 { margin-bottom: 8px; }
.mb-6 { margin-bottom: 24px; }
.text-center { text-align: center; margin-top: 16px; }
</style>
