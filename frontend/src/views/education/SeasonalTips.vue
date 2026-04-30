<template>
  <el-container>
    <el-main>
      <h1 class="text-h4 mb-6">健康爱好</h1>

      <el-row gutter="20">
        <el-col :xs="24" :md="16">
          <p class="text-body-1 mb-4">
            健康爱好是促进身心健康的重要经济和行为支持。通过培养一些有益健康的爱好模式，不仅能有效维持身体活力，还能减轻压力，改善情绪，增强耐力。该段内容根据不同类型的健康行为提供实用的指导
          </p>
        </el-col>
        <el-col :xs="24" :md="8">
          <el-image
            src="images/diseases/seasonal.jpg"
            style="height: 200px; width: 100%"
            fit="cover"
            class="rounded-lg"
          />
        </el-col>
      </el-row>

      <el-divider class="my-6" />

      <el-card shadow="hover">
        <el-table
          :data="habitsList"
          v-loading="loading"
          border
          stripe
          style="width: 100%"
        >
          <el-table-column prop="title" label="标题" />
          <el-table-column prop="category" label="分类" width="120" />
          <el-table-column prop="content" label="提示内容" />
          
        </el-table>
      </el-card>

      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="habitsList.length"
        layout="prev, pager, next"
        class="mt-4 text-center"
      />
    </el-main>
  </el-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { listHabit as fetchHabits } from '../../api/Knowledge';
import { ElMessage } from 'element-plus';

const habitsRaw = ref([]);
const loading = ref(false);

const currentPage = ref(1);
const pageSize = 5;

async function loadHabits() {
  loading.value = true;
  try {
    const res = await fetchHabits();
    habitsRaw.value = Array.isArray(res) ? res : [];
  } catch (e) {
    ElMessage.error('加载健康提示失败');
  } finally {
    loading.value = false;
  }
}

onMounted(loadHabits);

const habitsList = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  return habitsRaw.value.slice(start, start + pageSize);
});
</script>

<style scoped>
.text-center { text-align: center; }
.text-h4 { font-size: 2.125rem; font-weight: 500; margin-bottom: 24px; }
.text-body-1 { font-size: 1rem; line-height: 1.5; margin-bottom: 16px; }
.mb-4 { margin-bottom: 16px; }
.mb-6 { margin: 24px 0; }
.mt-4 { margin-top: 16px; }
.rounded-lg { border-radius: 8px; overflow: hidden; }
</style>
