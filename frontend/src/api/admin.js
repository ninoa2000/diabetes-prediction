import axios from 'axios';
import { API_BASE_URL } from '@/config';

// 创建API客户端
const apiClient = axios.create({
  baseURL: `${API_BASE_URL}/admin`,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 添加请求拦截器用于身份验证
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export const adminService = {
  /**
   * 获取所有普通用户列表
   * @returns {Promise} 用户列表数据
   */
  getAllUsers() {
    return apiClient.get('/users');
  },

  /**
   * 创建新用户
   * @param {Object} userData - 用户数据对象
   * @returns {Promise} 创建的用户数据
   */
  createUser(userData) {
    return apiClient.post('/users', userData);
  },

  /**
   * 更新用户状态（启用/禁用）
   * @param {string} userId - 用户ID
   * @param {boolean} active - 是否启用
   * @returns {Promise} 更新后的用户数据
   */
  updateUserStatus(userId, active) {
    return apiClient.put(`/users/${userId}/status?active=${active}`);
  },

  /**
   * 删除用户
   * @param {string} userId - 用户ID
   * @returns {Promise} 删除结果
   */
  deleteUser(userId) {
    return apiClient.delete(`/users/${userId}`);
  }
}; 