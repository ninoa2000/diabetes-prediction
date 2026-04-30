<template>
  <div class="change-password-page">
    <el-card class="box-card">
      <template #header>
        <span>修改密码</span>
      </template>

      <el-form
        :model="form"
        :rules="rules"
        ref="formRef"
        label-width="100px"
        class="change-password-form"
      >
        <el-form-item label="原密码" prop="oldPassword">
          <el-input
            v-model="form.oldPassword"
            type="password"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="form.newPassword"
            type="password"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
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
          >提交</el-button>
          <el-button @click="onReset">重置</el-button>
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
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 8, message: '密码至少 8 位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator(_, value) {
        if (value !== form.newPassword) {
          return new Error('两次输入密码不一致');
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
        ElMessage.success('密码修改成功，请重新登录');
        userStore.logout();
        router.push('/login');
      })
      .catch(err => {
        console.log('request error', err);
        ElMessage.error(err.message || '修改失败');
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
