<template>
  <div class="profile-container">
    <el-card class="profile-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <h2>Profile Information</h2>
        </div>
      </template>
      
      <div class="profile-content">
        <!-- Doctor Form -->
        <el-form v-if="isDoctor" :model="profileForm" label-width="130px">
          <el-form-item label="Username">
            <el-input v-model="profileForm.username" disabled />
          </el-form-item>
          
          <el-form-item label="Full Name">
            <el-input v-model="profileForm.name" />
          </el-form-item>
          
         
          
          <el-form-item label="Phone Number">
            <el-input v-model="profileForm.phone" />
          </el-form-item>
          
          <el-form-item label="Email">
            <el-input v-model="profileForm.email" />
          </el-form-item>
          
          <el-form-item label="Department">
            <el-select v-model="profileForm.department" placeholder="Select Department" style="width: 100%">
              <el-option label="Internal Medicine" value="Internal Medicine" />
              <el-option label="Surgery" value="Surgery" />
              <el-option label="Obstetrics and Gynecology" value="Obstetrics and Gynecology" />
              <el-option label="Endocrinology" value="Endocrinology" />
              <el-option label="Ophthalmology" value="Ophthalmology" />
              <el-option label="ENT" value="ENT" />
              <el-option label="Stomatology" value="Stomatology" />
              <el-option label="Dermatology" value="Dermatology" />
              <el-option label="Psychiatry" value="Psychiatry" />
              <el-option label="Infectious Diseases" value="Infectious Diseases" />
              <el-option label="Rehabilitation" value="Rehabilitation" />
              <el-option label="Traditional Chinese Medicine" value="Traditional Chinese Medicine" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="Job Title">
            <el-select v-model="profileForm.title" placeholder="Select Job Title" style="width: 100%">
              <el-option label="Chief Physician" value="Chief Physician" />
              <el-option label="Associate Chief Physician" value="Associate Chief Physician" />
              <el-option label="Attending Physician" value="Attending Physician" />
              <el-option label="Resident Physician" value="Resident Physician" />
            </el-select>
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="handleSave">Save</el-button>
          </el-form-item>
        </el-form>

        <!-- User Form -->
        <el-form v-else :model="profileForm" label-width="130px">
          <el-form-item label="Username">
            <el-input v-model="profileForm.username" disabled />
          </el-form-item>
          
          <el-form-item label="Full Name">
            <el-input v-model="profileForm.name" />
          </el-form-item>
          
          
          
          <el-form-item label="Phone Number">
            <el-input v-model="profileForm.phone" />
          </el-form-item>
          
          <el-form-item label="Email">
            <el-input v-model="profileForm.email" />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="handleSave">Save</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { doctorService } from '@/api/doctor';
import { userService } from '@/api/user';

const isDoctor = ref(false);
const profileForm = ref({
  username: '',
  name: '',
  gender: '',
  phone: '',
  email: '',
  department: '',
  title: ''
});

// Load profile information
const loadProfile = async () => {
  try {
    // Get user info from localStorage
    const userStr = localStorage.getItem('user');
    const userRole = localStorage.getItem('userRole');
    if (userStr) {
      const user = JSON.parse(userStr);
      isDoctor.value = userRole === 'ROLE_DOCTOR';
      
      if (isDoctor.value) {
        // Doctor loads doctor info
        const response = await doctorService.getDoctorInfo();
        if (response) {
          const doctorInfo = response;
          profileForm.value = {
            username: doctorInfo.username || '',
            name: doctorInfo.name || '',
            
            phone: doctorInfo.phone || '',
            email: doctorInfo.email || '',
            department: doctorInfo.department || '',
            title: doctorInfo.title || ''
          };
        }
      } else {
        // User loads user info
        const response = await userService.getUserInfo();
        console.log('User info response:', response); 
        if (response) {
          const userInfo = response;
          profileForm.value = {
            username: userInfo.username || '',
            name: userInfo.fullName || '',
            
            phone: userInfo.phone || '',
            email: userInfo.email || ''
          };
          console.log('Updated profile form:', profileForm.value); 
        }
      }
    } else {
      ElMessage.warning('User information not found');
    }
  } catch (error) {
    console.error('Failed to load profile:', error);
    ElMessage.error('Failed to load profile');
  }
};

// Save profile information
const handleSave = async () => {
  try {
    if (isDoctor.value) {
      // Update doctor info
      const doctorData = {
        name: profileForm.value.name,
        
        phone: profileForm.value.phone,
        email: profileForm.value.email,
        department: profileForm.value.department,
        title: profileForm.value.title
      };
      await doctorService.updateProfile(doctorData);
      ElMessage.success('Doctor profile updated successfully');
    } else {
      // Update user info
      const userData = {
        name: profileForm.value.name,
        
        phone: profileForm.value.phone,
        email: profileForm.value.email
      };
      await userService.updateUser(userData);
      ElMessage.success('Profile updated successfully');
    }
    await loadProfile();
  } catch (error) {
    console.error('Failed to save:', error);
    ElMessage.error('Failed to save: ' + (error.response?.data?.message || error.message));
  }
};

onMounted(() => {
  loadProfile();
});
</script>

<style scoped>
.profile-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.profile-card {
  transition: all 0.3s;
}

.profile-card:hover {
  transform: translateY(-5px);
}

.card-header {
  text-align: center;
  padding: 10px 0;
}

.card-header h2 {
  margin: 0;
  color: #303133;
  font-size: 22px;
  font-weight: 600;
}

.profile-content {
  padding: 20px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}

:deep(.el-input-number) {
  width: 100%;
}
</style> 