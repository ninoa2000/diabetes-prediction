import { defineStore } from 'pinia';
import axios from 'axios';
import { API_BASE_URL } from '@/config';

// Create API client instance
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Add request interceptor to include token in every request
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    console.log('Request URL:', config.url, 'Full URL:', config.baseURL + config.url);
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Safe JSON parse function
const safeJSONParse = (jsonString) => {
  if (!jsonString) return null;
  try {
    return JSON.parse(jsonString);
  } catch (e) {
    console.error('JSON parse error:', e);
    return null;
  }
};

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: safeJSONParse(localStorage.getItem('user')) || null,
    role: localStorage.getItem('userRole') || '',
    loading: false,
    error: null,
  }),
  
  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.role === 'ROLE_ADMIN',
    isDoctor: (state) => state.role === 'ROLE_DOCTOR',
    isUser: (state) => state.role === 'ROLE_USER',
    username: (state) => state.user?.username || '',
  },
  
  actions: {
    // Login action
    async login(credentials) {
      this.loading = true;
      this.error = null;
      
      try {
        console.log('Attempting login, username:', credentials.username);
        const response = await apiClient.post('/auth/login', {
          username: credentials.username,
          password: credentials.password
        });
        
        console.log('Login successful, response:', response);
        
        if (response.data) {
          console.log('Backend returned data:', response.data);
          
          // Store authentication data
          this.setUser(response.data);
          
          // Return success message
          return { code: 200, message: 'Login successful' };
        } else {
          throw new Error('Invalid login response data format');
        }
      } catch (error) {
        console.error('Login error:', error);
        console.error('Error details:', error.response);
        
        // Return error message
        const errorMessage = error.response?.data?.message || 'Login failed, please check username and password';
        this.error = errorMessage;
        throw new Error(errorMessage);
      } finally {
        this.loading = false;
      }
    },
    
    // Register action
    async register(userData) {
      this.loading = true;
      this.error = null;
      
      try {
        // 调用真实的注册API
        const response = await apiClient.post('/auth/register', userData);
        
        return Promise.resolve(response.data);
      } catch (error) {
        this.error = error.response?.data?.message || '注册失败，请稍后再试';
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },
    
    // Set user data and token
    setUser(data) {
      console.log('后端返回的数据:', data);
      
      // 适配后端JwtResponse的数据结构
      this.token = data.token || '';
      
      // 使用响应中的用户数据创建user对象
      this.user = {
        id: data.id || '',
        username: data.username || '',
        name: data.name || '',
        email: data.email || ''
      };
      
      // 从角色集合中确定主要角色
      this.role = 'ROLE_USER'; // 默认角色
      
      if (data.roles) {
        // 处理不同格式的roles数据
        const rolesArray = Array.isArray(data.roles) ? data.roles : 
                          (typeof data.roles === 'string' ? [data.roles] : []);
        
        console.log('角色数组:', rolesArray);
        
        if (rolesArray.some(role => role === 'ROLE_ADMIN')) {
          this.role = 'ROLE_ADMIN';
        } else if (rolesArray.some(role => role === 'ROLE_DOCTOR')) {
          this.role = 'ROLE_DOCTOR';
        } else if (rolesArray.some(role => role === 'ROLE_USER')) {
          this.role = 'ROLE_USER';
        }
      }
      
      console.log('设置用户数据:', { token: this.token, user: this.user, role: this.role });
      
      // Store in localStorage
      localStorage.setItem('token', this.token);
      localStorage.setItem('user', JSON.stringify(this.user));
      localStorage.setItem('userRole', this.role);
    },
    
    // Logout action
    logout() {
      this.token = '';
      this.user = null;
      this.role = '';
      
      // Clear localStorage
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      localStorage.removeItem('userRole');
    },
    
    // Update user profile
    async updateProfile(profileData) {
      this.loading = true;
      this.error = null;
      
      try {
        // 调用真实的更新用户资料API
        const response = await apiClient.put('/users/profile', profileData);
        
        // 更新用户数据
        this.user = response.data;
        localStorage.setItem('user', JSON.stringify(response.data));
        
        return Promise.resolve(response.data);
      } catch (error) {
        this.error = error.response?.data?.message || '更新个人信息失败';
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },
  },
}); 