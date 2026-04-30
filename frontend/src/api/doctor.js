import axios from 'axios';
import { API_BASE_URL } from '@/config';

// Set up axios instance for doctor endpoints
const apiClient = axios.create({
  baseURL: `${API_BASE_URL}/doctor`,
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

// Doctor service - Using localStorage for demonstration
export const doctorService = {
  /**
   * Get all doctors for selection
   * @returns {Promise} - The API response with doctors list
   */
  getAllDoctors() {
    return apiClient.get('');
  },

  /**
   * Get doctor details by ID
   * @param {string} doctorId - The doctor ID
   * @returns {Promise} - The API response with doctor details
   */
  getDoctorById(doctorId) {
    return apiClient.get(`/${doctorId}`);
  },

  /**
   * Create a new doctor
   * @param {Object} doctorData - The doctor data object
   * @returns {Promise} - The created doctor data
   */
  createDoctor(doctorData) {
    return apiClient.post('', doctorData);
  },

  /**
   * Update doctor status (enable/disable)
   * @param {string} doctorId - The doctor ID
   * @param {boolean} available - Whether the doctor is available
   * @returns {Promise} - The updated doctor data
   */
  updateDoctorStatus(doctorId, available) {
    return apiClient.put(`/${doctorId}/status?available=${available}`);
  },

  /**
   * Delete a doctor
   * @param {string} doctorId - The doctor ID
   * @returns {Promise} - The delete result
   */
  deleteDoctor(doctorId) {
    return apiClient.delete(`/${doctorId}`);
  },

  /**
   * Reset doctor's password
   * @param {string} doctorId - The doctor ID
   * @returns {Promise} - The reset result
   */
  resetPassword(doctorId) {
    return apiClient.post(`/${doctorId}/reset-password`);
  },

  /**
   * Get available doctors
   * @returns {Promise} - The list of available doctors
   */
  getAvailableDoctors() {
    return apiClient.get('/?available=true');
  },

  /**
   * Get the doctor bound to the current patient
   * @returns {Promise} - The API response with bound doctor details
   */
  getBoundDoctor() {
    return apiClient.get('/my-doctor');
  },

  /**
   * Bind a doctor to the current patient
   * @param {string} doctorId - The ID of the doctor to bind
   * @returns {Promise} - The API response
   */
  bindDoctor(doctorId) {
    return apiClient.post(`/bind/${doctorId}`);
  },

  /**
   * Unbind the current doctor
   * @returns {Promise} - The API response
   */
  unbindDoctor() {
    return apiClient.post('/unbind');
  },

  /**
   * Get all patients bound to the current doctor
   * @returns {Promise} - The API response with patients list
   */
  getBoundPatients() {
    return apiClient.get('/doctors/patients');
  },

  /**
   * Get detailed information about a specific patient
   * @param {string} patientId - The patient ID
   * @returns {Promise} - The API response with patient details
   */
  getPatientDetail(patientId) {
    return apiClient.get(`/patients/${patientId}`);
  },

  /**
   * Get prediction records for a specific patient
   * @param {string} patientId - The patient ID
   * @returns {Promise} - The API response with prediction records
   */
  getPatientPredictionRecords(patientId) {
    return apiClient.get(`/patients/${patientId}/predictions`);
  },

  /**
   * Add a recommendation for a patient's prediction
   * @param {string} predictionId - The prediction ID
   * @param {string} recommendation - The doctor's recommendation
   * @returns {Promise} - The API response
   */
  addRecommendation(predictionId, recommendation) {
    return apiClient.post(`/predictions/${predictionId}/recommendation`, {
      recommendation
    });
  },

  /**
   * Reply to a patient message
   * @param {string} messageId - The message ID to reply to
   * @param {string} content - The reply content
   * @returns {Promise} - The API response
   */
  replyToMessage(messageId, content) {
    return apiClient.post(`/messages/${messageId}/reply`, {
      content
    });
  },

  /**
   * Update doctor information
   * @param {string} doctorId - The doctor ID
   * @param {Object} doctorData - The updated doctor data
   * @returns {Promise} - The updated doctor data
   */
  updateDoctor(doctorId, doctorData) {
    return apiClient.put(`/${doctorId}`, doctorData);
  },

  // 获取当前医生的患者列表
  getPatients: async () => {
    try {
      const response = await apiClient.get('/my-patients');
      return response.data;
    } catch (error) {
      console.error('获取患者列表失败:', error);
      throw error;
    }
  },

  // 获取医生信息
  getDoctorInfo: async () => {
    try {
      const response = await apiClient.get('/profile');
      return response.data;
    } catch (error) {
      console.error('获取医生信息失败:', error);
      throw error;
    }
  },

  updateProfile: async (data) => {
    try {
      const response = await apiClient.put('/profile', data);
      return response.data;
    } catch (error) {
      console.error('更新医生信息失败:', error);
      throw error;
    }
  },

  getDashboardStats: () => {
    return apiClient.get('/dashboard-stats');
  },

  // 获取患者病例列表
  getPatientCases: async (patientId) => {
    try {
      const response = await apiClient.get(`/patients/${patientId}/cases`);
      return response.data;
    } catch (error) {
      console.error('获取患者病例失败:', error);
      throw error;
    }
  },

  /**
   * 获取患者信息
   * @param {string} patientId - 患者ID
   * @returns {Promise<Object>} - 患者信息
   */
  getPatientById(patientId) {
    return apiClient.get(`/patients/${patientId}`)
      .then(response => response.data)
      .catch(error => {
        console.error('获取患者信息失败:', error);
        throw error;
      });
  }
}; 