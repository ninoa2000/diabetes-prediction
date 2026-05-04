<template>
  <div class="profile-page-wrapper">
    <el-card class="premium-profile-card" :body-style="{ padding: '0px' }">
      <div class="profile-header-banner">
        <div class="header-overlay">
          <el-avatar :size="80" :src="avatarUrl" class="profile-avatar-large" />
          <div class="header-text">
            <h2>{{ profileForm.name || 'User Profile' }}</h2>
            <p>{{ isDoctor ? 'Healthcare Professional' : 'Patient Account' }}</p>
          </div>
        </div>
      </div>

      <div class="profile-tabs-container">
        <el-tabs v-model="activeTab" class="custom-premium-tabs">
          <el-tab-pane name="profile">
            <template #label>
              <span class="tab-label">
                <el-icon><UserIcon /></el-icon>
                <span>Profile Info</span>
              </span>
            </template>
            
            <div class="tab-content-area">
              <el-form :model="profileForm" label-position="top" class="premium-form">
                <el-row :gutter="32">
                  <el-col :md="12" :sm="24">
                    <el-form-item label="Username">
                      <el-input v-model="profileForm.username" disabled :prefix-icon="UserIcon" />
                    </el-form-item>
                  </el-col>
                  <el-col :md="12" :sm="24">
                    <el-form-item label="Full Name">
                      <el-input v-model="profileForm.name" placeholder="Enter your full name" :prefix-icon="EditPen" />
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="32">
                  <el-col :md="12" :sm="24">
                    <el-form-item label="Phone Number">
                      <el-input v-model="profileForm.phone" placeholder="Contact number" :prefix-icon="Phone" />
                    </el-form-item>
                  </el-col>
                  <el-col :md="12" :sm="24">
                    <el-form-item label="Email Address">
                      <el-input v-model="profileForm.email" placeholder="Email for notifications" :prefix-icon="MessageIcon" />
                    </el-form-item>
                  </el-col>
                </el-row>

                <template v-if="isDoctor">
                  <div class="section-divider">
                    <span class="divider-text">Professional Information</span>
                  </div>
                  <el-row :gutter="32">
                    <el-col :md="12" :sm="24">
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
                    </el-col>
                    <el-col :md="12" :sm="24">
                      <el-form-item label="Job Title">
                        <el-select v-model="profileForm.title" placeholder="Select Job Title" style="width: 100%">
                          <el-option label="Chief Physician" value="Chief Physician" />
                          <el-option label="Associate Chief Physician" value="Associate Chief Physician" />
                          <el-option label="Attending Physician" value="Attending Physician" />
                          <el-option label="Resident Physician" value="Resident Physician" />
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </template>

                <div class="form-footer-actions">
                  <el-button type="primary" size="large" @click="handleSave" class="save-profile-btn">
                    Update Profile Details
                  </el-button>
                </div>
              </el-form>
            </div>
          </el-tab-pane>

          <el-tab-pane name="password">
            <template #label>
              <span class="tab-label">
                <el-icon><Lock /></el-icon>
                <span>Security</span>
              </span>
            </template>
            <div class="tab-content-area">
              <change-password />
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { doctorService } from '@/api/doctor';
import { userService } from '@/api/user';
import ChangePassword from '@/views/user/ChangePassword.vue';
import { 
  User as UserIcon, 
  Lock, 
  EditPen, 
  Phone, 
  Message as MessageIcon 
} from '@element-plus/icons-vue';

const activeTab = ref('profile');
const isDoctor = ref(false);
const avatarUrl = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png');
const profileForm = ref({
  username: '',
  name: '',
  phone: '',
  email: '',
  department: '',
  title: ''
});

const loadProfile = async () => {
  try {
    const userStr = localStorage.getItem('user');
    const userRole = localStorage.getItem('userRole');
    if (userStr) {
      isDoctor.value = userRole === 'ROLE_DOCTOR';
      if (isDoctor.value) {
        const response = await doctorService.getDoctorInfo();
        if (response) {
          profileForm.value = {
            username: response.username || '',
            name: response.name || '',
            phone: response.phone || '',
            email: response.email || '',
            department: response.department || '',
            title: response.title || ''
          };
        }
      } else {
        const response = await userService.getUserInfo();
        if (response) {
          profileForm.value = {
            username: response.username || '',
            name: response.fullName || '',
            phone: response.phone || '',
            email: response.email || ''
          };
        }
      }
    }
  } catch (error) {
    console.error('Failed to load profile:', error);
    ElMessage.error('Failed to load profile details');
  }
};

const handleSave = async () => {
  try {
    if (isDoctor.value) {
      await doctorService.updateProfile({
        name: profileForm.value.name,
        phone: profileForm.value.phone,
        email: profileForm.value.email,
        department: profileForm.value.department,
        title: profileForm.value.title
      });
      ElMessage.success('Doctor profile updated successfully');
    } else {
      await userService.updateUser({
        name: profileForm.value.name,
        phone: profileForm.value.phone,
        email: profileForm.value.email
      });
      ElMessage.success('Profile updated successfully');
    }
    await loadProfile();
  } catch (error) {
    console.error('Failed to save profile:', error);
    ElMessage.error('Failed to save changes: ' + (error.response?.data?.message || error.message));
  }
};

onMounted(loadProfile);
</script>

<style scoped>
.profile-page-wrapper {
  max-width: 1000px;
  margin: 40px auto;
  padding: 0 20px;
}

.premium-profile-card {
  border-radius: 20px;
  overflow: hidden;
  border: none;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.08);
  background-color: #fff;
}

.profile-header-banner {
  height: 200px;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  position: relative;
}

.header-overlay {
  position: absolute;
  bottom: -40px;
  left: 50px;
  display: flex;
  align-items: flex-end;
  gap: 25px;
}

.profile-avatar-large {
  border: 5px solid #fff;
  box-shadow: 0 8px 20px rgba(0,0,0,0.12);
  background-color: #f3f4f6;
}

.header-text {
  padding-bottom: 50px;
}

.header-text h2 {
  margin: 0;
  color: #fff;
  font-size: 28px;
  font-weight: 800;
  letter-spacing: -0.5px;
  text-shadow: 0 2px 10px rgba(0,0,0,0.15);
}

.header-text p {
  margin: 6px 0 0;
  color: rgba(255, 255, 255, 0.95);
  font-size: 15px;
  font-weight: 500;
}

.profile-tabs-container {
  padding-top: 70px;
}

.tab-label {
  display: flex;
  align-items: center;
  gap: 10px;
  font-weight: 600;
  font-size: 16px;
  padding: 4px 8px;
}

.tab-content-area {
  padding: 40px 50px 50px;
}

.premium-form {
  margin-top: 15px;
}

.section-divider {
  margin: 45px 0 30px;
  height: 1px;
  background-color: #f1f5f9;
  position: relative;
}

.divider-text {
  position: absolute;
  top: 50%;
  left: 0;
  transform: translateY(-50%);
  background: #fff;
  padding-right: 20px;
  font-size: 13px;
  font-weight: 700;
  text-transform: uppercase;
  color: #94a3b8;
  letter-spacing: 1px;
}

.form-footer-actions {
  margin-top: 50px;
  display: flex;
  justify-content: flex-start;
}

.save-profile-btn {
  height: 52px;
  padding: 0 35px;
  border-radius: 12px;
  font-weight: 700;
  font-size: 16px;
  background: #3b82f6;
  border: none;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.save-profile-btn:hover {
  background: #2563eb;
  transform: translateY(-3px);
  box-shadow: 0 10px 20px rgba(59, 130, 246, 0.35);
}

:deep(.el-tabs__header) {
  margin-bottom: 0;
  padding: 0 50px;
  border-bottom: 2px solid #f1f5f9;
}

:deep(.el-tabs__nav-wrap::after) {
  display: none;
}

:deep(.el-tabs__item) {
  height: 70px;
  line-height: 70px;
  color: #94a3b8;
  transition: all 0.3s;
}

:deep(.el-tabs__item.is-active) {
  color: #3b82f6;
}

:deep(.el-tabs__active-bar) {
  background-color: #3b82f6;
  height: 4px;
  border-radius: 4px 4px 0 0;
}

:deep(.el-form-item__label) {
  font-weight: 700;
  color: #475569;
  padding-bottom: 10px !important;
  font-size: 14px;
}

:deep(.el-input__wrapper) {
  padding: 12px 18px;
  border-radius: 12px;
  background-color: #f8fafc;
  box-shadow: none !important;
  border: 2px solid #f1f5f9;
  transition: all 0.2s;
}

:deep(.el-input__wrapper.is-focus) {
  border-color: #3b82f6;
  background-color: #fff;
  box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.1) !important;
}

:deep(.el-select .el-input__wrapper) {
  width: 100%;
}

@media (max-width: 768px) {
  .header-overlay {
    left: 50%;
    transform: translateX(-50%);
    bottom: -60px;
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 15px;
  }
  
  .header-text {
    padding-bottom: 0;
  }
  
  .profile-tabs-container {
    padding-top: 120px;
  }
  
  .tab-content-area {
    padding: 30px 20px;
  }
  
  :deep(.el-tabs__header) {
    padding: 0 20px;
  }
}
</style>