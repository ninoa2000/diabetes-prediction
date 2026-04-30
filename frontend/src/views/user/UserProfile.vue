<template>
  <div class="profile-container">
    <el-card class="profile-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <h2>个人信息</h2>
        </div>
      </template>
      
      <div class="profile-content">
        <!-- 医生表单 -->
        <el-form v-if="isDoctor" :model="profileForm" label-width="100px">
          <el-form-item label="用户名">
            <el-input v-model="profileForm.username" disabled />
          </el-form-item>
          
          <el-form-item label="姓名">
            <el-input v-model="profileForm.name" />
          </el-form-item>
          
         
          
          <el-form-item label="手机号">
            <el-input v-model="profileForm.phone" />
          </el-form-item>
          
          <el-form-item label="邮箱">
            <el-input v-model="profileForm.email" />
          </el-form-item>
          
          <el-form-item label="科室">
            <el-select v-model="profileForm.department" placeholder="请选择科室" style="width: 100%">
              <el-option label="内科" value="内科" />
              <el-option label="外科" value="外科" />
              <el-option label="妇产科" value="妇产科" />
              <el-option label="内分泌科" value="内分泌科" />
              <el-option label="眼科" value="眼科" />
              <el-option label="耳鼻喉科" value="耳鼻喉科" />
              <el-option label="口腔科" value="口腔科" />
              <el-option label="皮肤科" value="皮肤科" />
              <el-option label="精神科" value="精神科" />
              <el-option label="传染科" value="传染科" />
              <el-option label="康复科" value="康复科" />
              <el-option label="中医科" value="中医科" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="职称">
            <el-select v-model="profileForm.title" placeholder="请选择职称" style="width: 100%">
              <el-option label="主任医师" value="主任医师" />
              <el-option label="副主任医师" value="副主任医师" />
              <el-option label="主治医师" value="主治医师" />
              <el-option label="住院医师" value="住院医师" />
            </el-select>
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="handleSave">保存</el-button>
          </el-form-item>
        </el-form>

        <!-- 普通用户表单 -->
        <el-form v-else :model="profileForm" label-width="100px">
          <el-form-item label="用户名">
            <el-input v-model="profileForm.username" disabled />
          </el-form-item>
          
          <el-form-item label="姓名">
            <el-input v-model="profileForm.name" />
          </el-form-item>
          
          
          
          <el-form-item label="手机号">
            <el-input v-model="profileForm.phone" />
          </el-form-item>
          
          <el-form-item label="邮箱">
            <el-input v-model="profileForm.email" />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="handleSave">保存</el-button>
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

// 加载个人信息
const loadProfile = async () => {
  try {
    // 获取用户信息
    const userStr = localStorage.getItem('user');
    const userRole = localStorage.getItem('userRole');
    if (userStr) {
      const user = JSON.parse(userStr);
      isDoctor.value = userRole === 'ROLE_DOCTOR';
      
      if (isDoctor.value) {
        // 医生加载医生信息
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
        // 普通用户加载用户信息
        const response = await userService.getUserInfo();
        console.log('User info response:', response); // 添加日志
        if (response) {
          const userInfo = response;
          profileForm.value = {
            username: userInfo.username || '',
            name: userInfo.fullName || '',
            
            phone: userInfo.phone || '',
            email: userInfo.email || ''
          };
          console.log('Updated profile form:', profileForm.value); // 添加日志
        }
      }
    } else {
      ElMessage.warning('未找到用户信息');
    }
  } catch (error) {
    console.error('加载个人信息失败:', error);
    ElMessage.error('加载个人信息失败');
  }
};

// 保存个人信息
const handleSave = async () => {
  try {
    if (isDoctor.value) {
      // 更新医生信息
      const doctorData = {
        name: profileForm.value.name,
        
        phone: profileForm.value.phone,
        email: profileForm.value.email,
        department: profileForm.value.department,
        title: profileForm.value.title
      };
      await doctorService.updateProfile(doctorData);
      ElMessage.success('医生信息更新成功');
    } else {
      // 更新用户信息
      const userData = {
        name: profileForm.value.name,
        
        phone: profileForm.value.phone,
        email: profileForm.value.email
      };
      await userService.updateUser(userData);
      ElMessage.success('个人信息更新成功');
    }
    await loadProfile();
  } catch (error) {
    console.error('保存失败:', error);
    ElMessage.error('保存失败: ' + (error.response?.data?.message || error.message));
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