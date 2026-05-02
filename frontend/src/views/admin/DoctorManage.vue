<template>
  <el-container>
    <el-main>
      <el-card>
        <template #header>
          <div class="card-header">
            <span>Doctor Management</span>
            <div class="search-box">
              <el-input
                v-model="search"
                placeholder="Search doctors..."
                style="width: 300px"
                class="mr-3"
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
              <el-button type="primary" @click="addDoctor">
                <el-icon class="mr-1"><Plus /></el-icon>Add Doctor
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
          <el-table-column prop="username" label="Username" min-width="120" show-overflow-tooltip></el-table-column>
          <el-table-column prop="name" label="Name" min-width="150" show-overflow-tooltip></el-table-column>
          <el-table-column prop="title" label="Title" min-width="150" show-overflow-tooltip></el-table-column>
          <el-table-column prop="department" label="Department" min-width="150" show-overflow-tooltip></el-table-column>
          <el-table-column prop="phone" label="Phone" min-width="150" show-overflow-tooltip></el-table-column>
          <el-table-column prop="email" label="Email" min-width="200" show-overflow-tooltip></el-table-column>
          <el-table-column label="Actions" width="300" fixed="right">
            <template #default="scope">
              <div class="operation-buttons">
                <el-button 
                  type="primary" 
                  size="small" 
                  @click="editDoctor(scope.row)"
                >
                  Edit
                </el-button>
                <el-button 
                  type="warning" 
                  size="small" 
                  @click="resetPassword(scope.row)"
                >
                  Reset Password
                </el-button>
                <el-button 
                  type="danger" 
                  size="small" 
                  @click="confirmDelete(scope.row)"
                >
                  Delete
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
    prev-text="Previous"
    next-text="Next"
    page-size-suffix="records/page"
    background
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
  >
    <!-- Custom total display slot -->
    <template #total="{ total }">
      Total {{ total }}
    </template>
    <!-- Custom jumper slot -->
    <template #jumper>
      Go to
      <el-input
        v-model="currentPage"
        size="small"
        style="width: 50px"
        @keyup.enter="handlePageJump"
      />
      page
    </template>
  </el-pagination>
          
        </div>
      </el-card>
      
      <!-- Doctor Form Dialog -->
      <el-dialog
        v-model="doctorDialog"
        :title="isEditing ? 'Edit Doctor Information' : 'Add Doctor'"
        width="700px"
      >
        <el-form
          ref="doctorFormRef"
          :model="doctorForm"
          :rules="rules"
          label-width="120px"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Username" prop="username">
                <el-input 
                  v-model="doctorForm.username"
                  :disabled="isEditing"
                  placeholder="Please enter username"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Name" prop="name">
                <el-input v-model="doctorForm.name"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Title" prop="title">
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
              <el-form-item label="Department" prop="department">
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
              <el-form-item label="Phone" prop="phone">
                <el-input v-model="doctorForm.phone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Email" prop="email">
                <el-input v-model="doctorForm.email"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="Biography" prop="bio">
            <el-input
              v-model="doctorForm.bio"
              type="textarea"
              :rows="4"
              placeholder="Please enter doctor biography"
            ></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="doctorDialog = false">Cancel</el-button>
            <el-button type="primary" @click="submitDoctorForm">Save</el-button>
          </div>
        </template>
      </el-dialog>
      
      <!-- Delete Confirmation Dialog -->
      <el-dialog
        v-model="deleteDialog"
        title="Delete Doctor"
        width="400px"
      >
        <p>Are you sure you want to delete doctor "{{ selectedDoctor ? selectedDoctor.name : '' }}"? This action cannot be undone.</p>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="deleteDialog = false">Cancel</el-button>
            <el-button type="danger" @click="deleteDoctor">Delete</el-button>
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
    
    // Form data
    const doctorForm = reactive({
      username: '',
      name: '',
      title: '',
      department: '',
      phone: '',
      email: '',
      bio: ''
    });
    
    // Validation rules
    const rules = {
      username: [
        { required: true, message: 'Please enter username', trigger: 'blur' },
        { min: 3, max: 20, message: 'Length should be 3 to 20 characters', trigger: 'blur' }
      ],
      name: [
        { required: true, message: 'Please enter name', trigger: 'blur' }
      ],
      department: [
        { required: true, message: 'Please select department', trigger: 'change' }
      ],
      specialty: [
        { required: true, message: 'Please enter specialty', trigger: 'blur' }
      ],
      title: [
        { required: true, message: 'Please select title', trigger: 'change' }
      ],
      phone: [
        { required: true, message: 'Please enter phone number', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: 'Please enter a valid phone number', trigger: 'blur' }
      ],
      email: [
        { required: true, message: 'Please enter email', trigger: 'blur' },
        { type: 'email', message: 'Please enter a valid email address', trigger: 'blur' }
      ]
    };
    
    // Options data
    const titleOptions = [
      'Resident Physician', 'Attending Physician', 'Associate Chief Physician', 'Chief Physician', 'Senior Consultant'
    ];
    
    const departmentOptions = [
      'Internal Medicine', 'Surgery', 'Endocrinology', 'Obstetrics and Gynecology', 'Ophthalmology', 'Otolaryngology', 'Stomatology',
      'Dermatology', 'Traditional Chinese Medicine', 'Rehabilitation', 'Emergency', 'Anesthesiology', 'Radiology', 'General Medicine'
    ];
    
    // Filter doctor list
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
    
    // Pagination data
    const paginatedDoctors = computed(() => {
      const start = (currentPage.value - 1) * pageSize.value;
      const end = start + pageSize.value;
      return filteredDoctors.value.slice(start, end);
    });
    
    // Get all doctors
    const fetchDoctors = async () => {
      try {
        loading.value = true;
        const response = await doctorService.getAllDoctors();
        doctors.value = response.data;
      } catch (error) {
        ElMessage.error(error.response?.data?.message || 'Failed to fetch doctor list');
        console.error('Fetch doctors error:', error);
      } finally {
        loading.value = false;
      }
    };
    
    // Reset form
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
    
    // Add doctor
    const addDoctor = () => {
      resetDoctorForm();
      isEditing.value = false;
      doctorDialog.value = true;
    };
    
    // Edit doctor
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
    
    // Submit doctor form
    const submitDoctorForm = async () => {
      if (!doctorFormRef.value) return;
      
      await doctorFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            if (isEditing.value) {
              // Update doctor info
              await doctorService.updateDoctor(doctorForm.id, doctorForm);
              ElMessage.success('Doctor information updated successfully');
            } else {
              // Create new doctor
              await doctorService.createDoctor(doctorForm);
              ElMessage.success('Doctor created successfully');
            }
            doctorDialog.value = false;
            fetchDoctors();
          } catch (error) {
            ElMessage.error(error.response?.data?.message || 'Operation failed');
          }
        }
      });
    };
    
    // Reset password
    const resetPassword = (doctor) => {
      console.log(doctor.id, doctor.userId)
      ElMessageBox.confirm(
        `Are you sure you want to reset the password for doctor "${doctor.name}"?`,
        'Reset Password',
        {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }
      ).then(async () => {
        try {
          await doctorService.resetPassword(doctor.id);
          ElMessage.success('Password reset successful. New password: doctor123');
        } catch (error) {
          ElMessage.error(error.response?.data?.message || 'Failed to reset password');
          console.error('Reset password error:', error);
        }
      }).catch(() => {});
    };
    
    // Confirm delete
    const confirmDelete = (doctor) => {
      selectedDoctor.value = doctor;
      deleteDialog.value = true;
    };
    
    // Delete doctor
    const deleteDoctor = async () => {
      try {
        console.log('Deleting doctor with ID:', selectedDoctor.value?.id);
        console.log('Selected doctor:', selectedDoctor.value);
        await doctorService.deleteDoctor(selectedDoctor.value.id);
        ElMessage.success('Doctor deleted successfully');
        deleteDialog.value = false;
        fetchDoctors(); // Reload doctor list
      } catch (error) {
        console.error('Delete doctor error:', error);
        ElMessage.error(error.response?.data?.message || 'Failed to delete doctor');
      }
    };
    
    // Pagination event handling
    const handleSizeChange = (size) => {
      pageSize.value = size;
      currentPage.value = 1;
    };
    
    const handleCurrentChange = (page) => {
      currentPage.value = page;
    };
    
    // Fetch doctor list on mount
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