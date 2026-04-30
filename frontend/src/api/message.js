import axios from 'axios';
import { API_BASE_URL } from '@/config';

// Set up axios instance for messages
const apiClient = axios.create({
  baseURL: `${API_BASE_URL}/messages`,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor to include user ID
apiClient.interceptors.request.use(
  (config) => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.id) {
      config.headers['X-User-ID'] = user.id;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export const messageService = {
  /**
   * Send a new message to the bound doctor
   * @param {string} doctorId - The ID of the doctor to send the message to
   * @param {string} content - The message content
   * @returns {Promise} - The API response
   */
  sendMessage(doctorId, content) {
    if (!doctorId) {
      return Promise.reject(new Error('未绑定医生'));
    }
    return apiClient.post(`/send/${doctorId}`, {
      content
    });
  },

  /**
   * Reply to an existing message
   * @param {string} messageId - The ID of the message to reply to
   * @param {string} content - The reply content
   * @returns {Promise} - The API response
   */
  replyToMessage(messageId, content) {
    return apiClient.post(`/${messageId}/reply`, {
      content
    });
  },

  /**
   * Get all messages for the current patient
   * @returns {Promise} - The API response with messages data
   */
  getPatientMessages() {
    return apiClient.get('/my-messages');
  },

  /**
   * Get all messages for a doctor
   * @returns {Promise} - The API response with messages data
   */
  getDoctorMessages() {
    return apiClient.get('/my-messages');
  },

  /**
   * Mark a message as read
   * @param {string} messageId - The ID of the message to mark as read
   * @returns {Promise} - The API response
   */
  markAsRead(messageId) {
    return apiClient.put(`/${messageId}/read`);
  },

  /**
   * Delete a message
   * @param {string} messageId - The ID of the message to delete
   * @returns {Promise} - The API response
   */
  deleteMessage(messageId) {
    return apiClient.delete(`/${messageId}`);
  }
}; 