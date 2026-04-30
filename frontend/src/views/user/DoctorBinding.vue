<template>
  <div class="doctor-binding-container">
    <el-card class="binding-card">
      <template #header>
        <div class="card-header">
          <h2>医生绑定</h2>
        </div>
      </template>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>
      
      <template v-else>
        <div v-if="boundDoctor" class="bound-doctor-info">
          <div class="section-title">当前绑定医生</div>
          
          <el-descriptions :column="1" border>
            <el-descriptions-item label="姓名">{{ boundDoctor.name }}</el-descriptions-item>
            <el-descriptions-item label="科室">{{ boundDoctor.department }}</el-descriptions-item>
            <el-descriptions-item label="专长">{{ boundDoctor.specialty }}</el-descriptions-item>
          </el-descriptions>
          
          <div class="action-buttons">
            <el-button type="danger" @click="confirmUnbind">解除绑定</el-button>
            <el-button type="primary" @click="goToMessage">发送留言</el-button>
          </div>
        </div>
        
        <div v-else class="doctor-selection">
          <div class="section-title">可选医生列表</div>
          <div class="section-description">请选择一位医生进行绑定，绑定后可以向医生发送留言。</div>
          
          <el-table
            :data="pagedDoctors"
            style="width: 100%"
            @row-click="handleRowClick"
            highlight-current-row
          >
            <el-table-column prop="name" label="姓名" />
            <el-table-column prop="department" label="科室" />
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button
                  type="primary"
                  size="small"
                  @click.stop="bindDoctor(scope.row)"
                >
                  绑定
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <el-pagination
            v-if="totalPages > 1"
            class="pagination"
            background
            layout="prev, pager, next"
            v-model:current-page="currentPage"
            :page-size="pageSize"
            :total="availableDoctors.length"
            @current-change="handlePageChange"
          />
        </div>
      </template>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessageBox, ElMessage } from 'element-plus';
import { doctorService } from '@/api/doctor';

const router = useRouter();
const loading = ref(false);
const availableDoctors = ref([]);
const boundDoctor = ref(null);

// 分页相关
const currentPage = ref(1);
const pageSize = 10; // 每页显示数量
const totalPages = computed(() => Math.ceil(availableDoctors.value.length / pageSize));
const pagedDoctors = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  return availableDoctors.value.slice(start, start + pageSize);
});

// 加载医生数据
const loadDoctors = async () => {
  try {
    loading.value = true;
    const boundResponse = await doctorService.getBoundDoctor();
    boundDoctor.value = boundResponse.data;
    if (!boundDoctor.value) {
      const doctorsResponse = await doctorService.getAllDoctors();
      availableDoctors.value = doctorsResponse.data;
    }
  } catch (error) {
    ElMessage.error('加载医生数据失败');
    console.error('Failed to load doctors:', error);
  } finally {
    loading.value = false;
  }
};

const handleRowClick = (row) => {
  ElMessageBox.confirm(
    `确定要绑定医生 ${row.name} 吗？`,
    '绑定确认',
    { confirmButtonText: '确定', cancelButtonText: '取消', type: 'info' }
  ).then(() => bindDoctor(row)).catch(() => {});
};

const bindDoctor = async (doctor) => {
  try {
    loading.value = true;
    await doctorService.bindDoctor(doctor.id);
    ElMessage.success(`成功绑定医生：${doctor.name}`);
    boundDoctor.value = doctor;
  } catch (error) {
    ElMessage.error(error.message || '绑定医生失败');
    console.error('Failed to bind doctor:', error);
  } finally {
    loading.value = false;
  }
};

const confirmUnbind = () => {
  if (!boundDoctor.value) return;
  ElMessageBox.confirm(
    `确定要解除与医生 ${boundDoctor.value.name} 的绑定吗？`,
    '解除绑定确认',
    { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
  ).then(() => unbindDoctor()).catch(() => {});
};

const unbindDoctor = async () => {
  try {
    loading.value = true;
    await doctorService.unbindDoctor();
    ElMessage.success('已解除医生绑定');
    boundDoctor.value = null;
    const doctorsResponse = await doctorService.getAllDoctors();
    availableDoctors.value = doctorsResponse.data;
    currentPage.value = 1;
  } catch (error) {
    ElMessage.error('解除绑定失败');
    console.error('Failed to unbind doctor:', error);
  } finally {
    loading.value = false;
  }
};

const goToMessage = () => {
  router.push('/message');
};

const handlePageChange = (page) => {
  currentPage.value = page;
};

onMounted(() => {
  loadDoctors();
});
</script>

<style scoped>
.doctor-binding-container { max-width: 800px; margin: 0 auto; padding: 20px; }
.binding-card { width: 100%; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.card-header h2 { margin: 0; font-size: 20px; font-weight: 600; }
.loading-container { padding: 40px 0; text-align: center; }
.section-title { font-size: 18px; font-weight: 600; margin-bottom: 15px; color: #303133; }
.section-description { color: #606266; margin-bottom: 20px; }
.bound-doctor-info { padding: 20px 0; }
.action-buttons { margin-top: 20px; display: flex; gap: 10px; }
.doctor-selection { padding: 20px 0; }
.pagination { text-align: right; margin-top: 10px; }
</style>
