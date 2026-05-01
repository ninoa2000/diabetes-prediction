<template>
  <div class="change-password-page">
    <el-card class="box-card">
      <template #header>
        <span>Change Password</span>
      </template>

      <el-form
        :model="form"
        :rules="rules"
        ref="formRef"
        label-width="100px"
        class="change-password-form"
      >
        <el-form-item label="Old Password" prop="oldPassword">
          <el-input
            v-model="form.oldPassword"
            type="password"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item label="New Password" prop="newPassword">
          <el-input
            v-model="form.newPassword"
            type="password"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item label="Confirm Password" prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            @click="onSubmit"
            >Submit</el-button>
          <el-button @click="onReset">Reset</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/store/user';
import { userService } from '@/api/user';

const router = useRouter();
const userStore = useUserStore();
const formRef = ref(null);
const loading = ref(false);
const form = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' });
const rules = {
  oldPassword: [
    { required: true, message: 'Please enter old password', trigger: 'blur' }
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
      },
      trigger: 'blur'
    }
  ]
};

function onSubmit() {
  console.log('onSubmit invoked');
  
   const payload = {
      oldPassword: form.oldPassword,
      newPassword: form.newPassword,
      confirmPassword: form.confirmPassword,
      userName: userStore.username
    };
    console.log('sending payload', payload);
    userService.changePassword(payload)
      .then(() => {
        ElMessage.success('Password changed successfully, please login again');
        userStore.logout();
        router.push('/login');
      })
      .catch(err => {
        console.log('request error', err);
        ElMessage.error(err.message || 'Failed to change password');
      })
}

function onReset() {
  formRef.value.resetFields();
}
</script>

<style scoped>
.change-password-page {
  padding: 20px;
}
.change-password-form {
  max-width: 400px;
}
</style>
