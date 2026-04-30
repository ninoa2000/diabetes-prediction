import axios from 'axios';
import { API_BASE_URL } from '@/config';

// Set up axios instance for user endpoints
const apiClient = axios.create({
  baseURL: `${API_BASE_URL}/user`,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor to include auth token
apiClient.interceptors.request.use(
  (config) => {
    try {
      // Get user ID from localStorage
      const userStr = localStorage.getItem('user');
      console.log('User from localStorage:', userStr);
      
      if (userStr) {
        const user = JSON.parse(userStr);
        console.log('Parsed user object:', user);
        
        if (user && user.id) {
          console.log('Setting X-User-ID header:', user.id);
          config.headers['X-User-ID'] = user.id;
        } else {
          console.warn('User object or user.id is missing');
        }
      } else {
        console.warn('No user found in localStorage');
      }
    } catch (error) {
      console.error('Error parsing user from localStorage:', error);
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export const userService = {
  /**
   * Get current user information
   * @returns {Promise} - The API response with user details
   */
  getUserInfo() {
    return apiClient.get('/profile').then(response => {
      console.log('Raw user info response:', response);
      return response.data;
    });
  },

  /**
   * Update user information
   * @param {Object} userData - The user data to update
   * @returns {Promise} - The updated user data
   */
  updateUser(userData) {
    return apiClient.put('/profile', userData).then(response => {
      console.log('Raw update response:', response);
      return response.data;
    });
  },

  changePassword(data) {
    return apiClient.post('/change-password', data).then(response => {
      console.log('Raw update response:', response);
      return response.data;
    });
  }
}; 