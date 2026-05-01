<template>
  <div class="register-container">
    <div class="register-box">
      <h2 class="register-title">User Registration</h2>
      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        label-position="top"
        class="register-form"
      >
        <el-form-item label="Username" prop="username">
          <el-input 
            v-model="registerForm.username"
            placeholder="Please enter username"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="Name" prop="name">
          <el-input 
            v-model="registerForm.name"
            placeholder="Please enter name"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="Phone Number" prop="phone">
          <el-input 
            v-model="registerForm.phone"
            placeholder="Please enter phone number"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="Password" prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="Please enter password"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="Confirm Password" prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="Please re-enter password"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            class="register-button"
            @click="handleRegister"
          >
            Register
          </el-button>
        </el-form-item>
        
        <div class="register-footer">
          <span>Already have an account?</span>
          <router-link to="/login">Back to Login</router-link>
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

const router = useRouter();
const userStore = useUserStore();
const registerFormRef = ref(null);
const loading = ref(false);

// Register form data
const registerForm = reactive({
  username: '',
  name: '',
  phone: '',
  password: '',
  confirmPassword: '',
});

// Validate password match
const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('Please re-enter password'));
  } else if (value !== registerForm.password) {
    callback(new Error('Passwords do not match'));
  } else {
    callback();
  }
};

// Form validation rules
const registerRules = reactive({
  username: [
    { required: true, message: 'Please enter username', trigger: 'blur' },
    { min: 3, max: 20, message: 'Length should be 3 to 20 characters', trigger: 'blur' },
  ],
  name: [
    { required: true, message: 'Please enter name', trigger: 'blur' },
  ],
  phone: [
    { required: true, message: 'Please enter phone number', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: 'Please enter a valid phone number', trigger: 'blur' },
  ],
  password: [
    { required: true, message: 'Please enter password', trigger: 'blur' },
    { min: 6, max: 20, message: 'Length should be 6 to 20 characters', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, validator: validatePass, trigger: 'blur' },
  ],
});

// Handle registration form submission
const handleRegister = () => {
  registerFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        loading.value = true;
        await userStore.register(registerForm);
        ElMessage.success('Registration successful, please login');
        router.push('/login');
      } catch (error) {
        ElMessage.error(error.message || 'Registration failed, please try again');
      } finally {
        loading.value = false;
      }
    } else {
      return false;
    }
  });
};
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.register-box {
  width: 400px;
  padding: 30px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.register-title {
  text-align: center;
  margin-bottom: 30px;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.register-form {
  margin-bottom: 20px;
}

.register-button {
  width: 100%;
}

.register-footer {
  text-align: center;
  font-size: 14px;
  color: #606266;
}

.register-footer a {
  color: #409EFF;
  text-decoration: none;
}
</style>