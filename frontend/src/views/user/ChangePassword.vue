<template>
  <div class="password-form-container">
    <el-form
      :model="form"
      :rules="rules"
      ref="formRef"
      label-position="top"
      class="custom-password-form"
    >
      <el-form-item label="Current Password" prop="oldPassword">
        <el-input
          v-model="form.oldPassword"
          type="password"
          placeholder="Enter current password"
          show-password
          :prefix-icon="Lock"
        />
      </el-form-item>
      
      <el-form-item label="New Password" prop="newPassword">
        <el-input
          v-model="form.newPassword"
          type="password"
          placeholder="Enter at least 8 characters"
          show-password
          :prefix-icon="Key"
        />
      </el-form-item>
      
      <el-form-item label="Confirm New Password" prop="confirmPassword">
        <el-input
          v-model="form.confirmPassword"
          type="password"
          placeholder="Repeat new password"
          show-password
          :prefix-icon="CircleCheck"
        />
      </el-form-item>
      
      <div class="form-actions">
        <el-button
          type="primary"
          :loading="loading"
          @click="onSubmit"
          class="submit-btn"
        >Update Password</el-button>
        <el-button @click="onReset">Reset</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/store/user';
import { userService } from '@/api/user';
import { Lock, Key, CircleCheck } from '@element-plus/icons-vue';

const router = useRouter();
const userStore = useUserStore();
const formRef = ref(null);
const loading = ref(false);
const form = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' });

const rules = {
  oldPassword: [
    { required: true, message: 'Please enter current password', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: 'Please enter new password', trigger: 'blur' },
    { min: 8, message: 'Password must be at least 8 characters', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: 'Please re-enter new password', trigger: 'blur' },
    {
      validator(_, value) {
        if (value !== form.newPassword) {
          return new Error('The two passwords do not match');
        }
        return true;
      },
      trigger: 'blur'
    }
  ]
};

async function onSubmit() {
  if (!formRef.value) return;
  
  await formRef.value.validate((valid) => {
    if (valid) {
      loading.value = true;
      const payload = {
        oldPassword: form.oldPassword,
        newPassword: form.newPassword,
        confirmPassword: form.confirmPassword,
        userName: userStore.user?.username // Fixed access to username
      };
      
      userService.changePassword(payload)
        .then(() => {
          ElMessage.success('Password changed successfully, please login again');
          userStore.logout();
          router.push('/login');
        })
        .catch(err => {
          ElMessage.error(err.response?.data?.message || 'Failed to change password');
        })
        .finally(() => {
          loading.value = false;
        });
    }
  });
}

function onReset() {
  if (formRef.value) {
    formRef.value.resetFields();
  }
}
</script>

<style scoped>
.password-form-container {
  padding: 10px 0;
}

.custom-password-form {
  max-width: 460px;
}

.form-actions {
  margin-top: 30px;
  display: flex;
  gap: 12px;
}

.submit-btn {
  padding: 12px 24px;
  font-weight: 600;
  background-color: #3b82f6;
  border-color: #3b82f6;
}

.submit-btn:hover {
  background-color: #2563eb;
  border-color: #2563eb;
}

:deep(.el-form-item__label) {
  font-weight: 600;
  padding-bottom: 8px !important;
  color: #374151;
}

:deep(.el-input__wrapper) {
  padding: 8px 12px;
  border-radius: 8px;
  transition: all 0.2s;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #3b82f6 inset !important;
}
</style>
