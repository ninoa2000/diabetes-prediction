<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="login-title">Diabetes Prediction System</h2>
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        label-position="top"
        class="login-form"
      >
        <el-form-item label="Username" prop="username">
          <el-input 
            v-model="loginForm.username"
            placeholder="Please enter username"
            prefix-icon="el-icon-user"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="Password" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="Please enter password"
            prefix-icon="el-icon-lock"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            class="login-button"
            @click="handleLogin"
          >
            Login
          </el-button>
        </el-form-item>
        
        <div class="login-footer">
          <span>Don't have an account? </span>
          <router-link to="/register">Register now</router-link>
        </div>

        <el-divider>Test Accounts</el-divider>
        <div class="test-accounts">
          <el-button size="small" @click="fillLogin('ninoa2000', 'admin123')">Patient</el-button>
          <el-button size="small" type="success" @click="fillLogin('doctor3', 'admin123')">Doctor</el-button>
          <el-button size="small" type="warning" @click="fillLogin('admin', 'admin123')">Admin</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { useUserStore } from '@/store/user';

// Import role constants
import { ROLE_ADMIN, ROLE_DOCTOR, ROLE_USER } from '@/constants/roles';

const router = useRouter();
const userStore = useUserStore();
const loginFormRef = ref(null);
const loading = ref(false);

// Login form data
const loginForm = reactive({
  username: '',
  password: '',
});

const fillLogin = (username, password) => {
  loginForm.username = username;
  loginForm.password = password;
};

// Form validation rules
const loginRules = reactive({
  username: [
    { required: true, message: 'Please enter username', trigger: 'blur' },
    { min: 3, max: 20, message: 'Length should be 3 to 20 characters', trigger: 'blur' },
  ],
  password: [
    { required: true, message: 'Please enter password', trigger: 'blur' },
    { min: 6, max: 20, message: 'Length should be 6 to 20 characters', trigger: 'blur' },
  ],
});

// Handle login form submission
const handleLogin = async () => {
  try {
    // Perform form validation first
    await loginFormRef.value.validate();
    
    loading.value = true;
    await userStore.login(loginForm);
    
    ElMessage.success('Login successful');
    
    // Redirect based on role
    const userRole = localStorage.getItem('userRole');
    if (userRole === ROLE_ADMIN) {
      router.push('/admin/doctors');
    } else if (userRole === ROLE_DOCTOR) {
      router.push('/doctor/home');
    } else {
      router.push('/home');
    }
  } catch (error) {
    console.error('Login failed:', error);
    ElMessage.error(error?.message || 'Login failed');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.login-box {
  width: 400px;
  padding: 30px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.login-title {
  text-align: center;
  margin-bottom: 30px;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.login-form {
  margin-bottom: 20px;
}

.login-button {
  width: 100%;
}

.login-footer {
  text-align: center;
  font-size: 14px;
  color: #606266;
}

.login-footer a {
  color: #409EFF;
  text-decoration: none;
}

.test-accounts {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 10px;
}

:deep(.el-divider__text) {
  background-color: #fff;
  font-size: 12px;
  color: #909399;
}
</style>