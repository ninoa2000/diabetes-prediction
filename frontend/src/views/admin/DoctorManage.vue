<template>
  <el-container>
    <el-main>
      <el-card>
        <template #header>
          <div class="card-header">
            <span>医生管理</span>
            <div class="search-box">
              <el-input
                v-model="search"
                placeholder="搜索医生..."
                style="width: 300px"
                class="mr-3"
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
              <el-button type="primary" @click="addDoctor">
                <el-icon class="mr-1"><Plus /></el-icon>添加医生
              </el-button>
            </div>
          </div>
        </template>
        
        <el-table
          :data="paginatedDoctors"
          border
          style="width: 100%"
          v-loading="loading"
        >
          <el-table-column prop="username" label="用户名" min-width="100"></el-table-column>
          <el-table-column prop="name" label="姓名" min-width="120"></el-table-column>
          <el-table-column prop="title" label="职称" min-width="120"></el-table-column>
          <el-table-column prop="department" label="科室" min-width="120"></el-table-column>
          <el-table-column prop="phone" label="电话" min-width="150"></el-table-column>
          <el-table-column prop="email" label="邮箱" min-width="220"></el-table-column>
          <el-table-column label="操作" width="280" fixed="right">
            <template #default="scope">
              <div class="operation-buttons">
                <el-button 
                  type="primary" 
                  size="small" 
                  @click="editDoctor(scope.row)"
                >
                  编辑
                </el-button>
                <el-button 
                  type="warning" 
                  size="small" 
                  @click="resetPassword(scope.row)"
                >
                  重置密码
                </el-button>
                <el-button 
                  type="danger" 
                  size="small" 
                  @click="confirmDelete(scope.row)"
                >
                  删除
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="pagination-container">
          <el-pagination
          v-model:current-page="currentPage"
    v-model:page-size="pageSize"
    :page-sizes="[10, 20, 50, 100]"
    :total="filteredDoctors.length"
    layout="total, sizes, prev, pager, next, jumper"
    prev-text="上一页"
    next-text="下一页"
    page-size-suffix="条/页"
    background
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
  >
    <!-- 自定义总数显示插槽 -->
    <template #total="{ total }">
      共 {{ total }} 条
    </template>
    <!-- 自定义跳转插槽 -->
    <template #jumper>
      跳至
      <el-input
        v-model="currentPage"
        size="small"
        style="width: 50px"
        @keyup.enter="handlePageJump"
      />
      页
    </template>
  </el-pagination>
          
        </div>
      </el-card>
      
      <!-- 医生表单对话框 -->
      <el-dialog
        v-model="doctorDialog"
        :title="isEditing ? '编辑医生信息' : '添加医生'"
        width="700px"
      >
        <el-form
          ref="doctorFormRef"
          :model="doctorForm"
          :rules="rules"
          label-width="100px"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用户名" prop="username">
                <el-input 
                  v-model="doctorForm.username"
                  :disabled="isEditing"
                  placeholder="请输入用户登录名"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="姓名" prop="name">
                <el-input v-model="doctorForm.name"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="职称" prop="title">
                <el-select v-model="doctorForm.title" style="width: 100%">
                  <el-option
                    v-for="title in titleOptions"
                    :key="title"
                    :label="title"
                    :value="title"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="科室" prop="department">
                <el-select v-model="doctorForm.department" style="width: 100%">
                  <el-option
                    v-for="dept in departmentOptions"
                    :key="dept"
                    :label="dept"
                    :value="dept"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="电话" prop="phone">
                <el-input v-model="doctorForm.phone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="doctorForm.email"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="简介" prop="bio">
            <el-input
              v-model="doctorForm.bio"
              type="textarea"
              :rows="4"
              placeholder="请输入医生简介"
            ></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="doctorDialog = false">取消</el-button>
            <el-button type="primary" @click="submitDoctorForm">保存</el-button>
          </div>
        </template>
      </el-dialog>
      
      <!-- 删除确认对话框 -->
      <el-dialog
        v-model="deleteDialog"
        title="删除医生"
        width="400px"
      >
        <p>确定要删除医生 "{{ selectedDoctor ? selectedDoctor.name : '' }}" 吗？此操作不可恢复。</p>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="deleteDialog = false">取消</el-button>
            <el-button type="danger" @click="deleteDoctor">删除</el-button>
          </div>
        </template>
      </el-dialog>
    </el-main>
  </el-container>
</template>

<script>
import { ref, computed, reactive, onMounted } from 'vue';
import { Search, Plus } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { doctorService } from '@/api/doctor';

export default {
  name: 'DoctorManage',
  components: {
    Search,
    Plus
  },
  setup() {
    const search = ref('');
    const currentPage = ref(1);
    const pageSize = ref(10);
    const doctorDialog = ref(false);
    const deleteDialog = ref(false);
    const isEditing = ref(false);
    const selectedDoctor = ref(null);
    const doctorFormRef = ref(null);
    const loading = ref(false);
    const doctors = ref([]);
    
    // 表单数据
    const doctorForm = reactive({
      username: '',
      name: '',
      title: '',
      department: '',
      phone: '',
      email: '',
      bio: ''
    });
    
    // 表单验证规则
    const rules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
      ],
      name: [
        { required: true, message: '请输入姓名', trigger: 'blur' }
      ],
      department: [
        { required: true, message: '请选择科室', trigger: 'change' }
      ],
      specialty: [
        { required: true, message: '请输入专长', trigger: 'blur' }
      ],
      title: [
        { required: true, message: '请选择职称', trigger: 'change' }
      ],
      phone: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
      ],
      email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
      ]
    };
    
    // 选项数据
    const titleOptions = [
      '住院医师', '主治医师', '副主任医师', '主任医师'
    ];
    
    const departmentOptions = [
      '内科', '外科', '内分泌科', '妇产科', '眼科', '耳鼻喉科', '口腔科',
      '皮肤科', '中医科', '康复科', '急诊科', '麻醉科', '放射科'
    ];
    
    // 过滤医生列表
    const filteredDoctors = computed(() => {
      let result = doctors.value;
      
      if (search.value) {
        const searchLower = search.value.toLowerCase();
        result = result.filter(doctor => 
          doctor.name.toLowerCase().includes(searchLower) ||
          (doctor.email && doctor.email.toLowerCase().includes(searchLower)) ||
          (doctor.phone && doctor.phone.includes(search.value)) ||
          (doctor.bio && doctor.bio.toLowerCase().includes(searchLower))
        );
      }
      
      return result;
    });
    
    // 分页数据
    const paginatedDoctors = computed(() => {
      const start = (currentPage.value - 1) * pageSize.value;
      const end = start + pageSize.value;
      return filteredDoctors.value.slice(start, end);
    });
    
    // 获取所有医生
    const fetchDoctors = async () => {
      try {
        loading.value = true;
        const response = await doctorService.getAllDoctors();
        doctors.value = response.data;
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '获取医生列表失败');
        console.error('获取医生列表错误:', error);
      } finally {
        loading.value = false;
      }
    };
    
    // 重置表单
    const resetDoctorForm = () => {
      if (doctorFormRef.value) {
        doctorFormRef.value.resetFields();
      }
      Object.assign(doctorForm, {
        username: '',
        name: '',
        title: '',
        department: '',
        phone: '',
        email: '',
        bio: ''
      });
    };
    
    // 添加医生
    const addDoctor = () => {
      resetDoctorForm();
      isEditing.value = false;
      doctorDialog.value = true;
    };
    
    // 编辑医生
    const editDoctor = (doctor) => {
      isEditing.value = true;
      Object.assign(doctorForm, {
        id: doctor.id,
        username: doctor.username,
        name: doctor.name,
        title: doctor.title,
        department: doctor.department,
        phone: doctor.phone,
        email: doctor.email,
        bio: doctor.bio || ''
      });
      doctorDialog.value = true;
    };
    
    // 提交医生表单
    const submitDoctorForm = async () => {
      if (!doctorFormRef.value) return;
      
      await doctorFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            if (isEditing.value) {
              // 更新医生信息
              await doctorService.updateDoctor(doctorForm.id, doctorForm);
              ElMessage.success('医生信息更新成功');
            } else {
              // 创建新医生
              await doctorService.createDoctor(doctorForm);
              ElMessage.success('医生创建成功');
            }
            doctorDialog.value = false;
            fetchDoctors();
          } catch (error) {
            ElMessage.error(error.response?.data?.message || '操作失败');
          }
        }
      });
    };
    
    // 重置密码
    const resetPassword = (doctor) => {
      console.log(doctor.id, doctor.userId)
      ElMessageBox.confirm(
        `确定要重置医生 "${doctor.name}" 的密码吗？`,
        '重置密码',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(async () => {
        try {
          await doctorService.resetPassword(doctor.id);
          ElMessage.success('密码重置成功，新密码为: doctor123');
        } catch (error) {
          ElMessage.error(error.response?.data?.message || '重置密码失败');
          console.error('重置密码错误:', error);
        }
      }).catch(() => {});
    };
    
    // 确认删除
    const confirmDelete = (doctor) => {
      selectedDoctor.value = doctor;
      deleteDialog.value = true;
    };
    
    // 删除医生
    const deleteDoctor = async () => {
      try {
        console.log('Deleting doctor with ID:', selectedDoctor.value?.id);
        console.log('Selected doctor:', selectedDoctor.value);
        await doctorService.deleteDoctor(selectedDoctor.value.id);
        ElMessage.success('医生删除成功');
        deleteDialog.value = false;
        fetchDoctors(); // 重新加载医生列表
      } catch (error) {
        console.error('Delete doctor error:', error);
        ElMessage.error(error.response?.data?.message || '删除医生失败');
      }
    };
    
    // 分页事件处理
    const handleSizeChange = (size) => {
      pageSize.value = size;
      currentPage.value = 1;
    };
    
    const handleCurrentChange = (page) => {
      currentPage.value = page;
    };
    
    // 组件挂载时获取医生列表
    onMounted(() => {
      fetchDoctors();
    });
    
    return {
      search,
      currentPage,
      pageSize,
      doctorDialog,
      deleteDialog,
      isEditing,
      selectedDoctor,
      doctorForm,
      doctorFormRef,
      rules,
      titleOptions,
      departmentOptions,
      doctors,
      filteredDoctors,
      paginatedDoctors,
      loading,
      addDoctor,
      editDoctor,
      submitDoctorForm,
      resetPassword,
      confirmDelete,
      deleteDoctor,
      handleSizeChange,
      handleCurrentChange
    };
  }
};
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-box {
  display: flex;
  align-items: center;
}

.mr-1 {
  margin-right: 4px;
}

.mr-2 {
  margin-right: 8px;
}

.mr-3 {
  margin-right: 12px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}

.operation-buttons {
  display: flex;
  gap: 8px;
  white-space: nowrap;
}
</style> 