import axios from 'axios'
import { API_BASE_URL } from '@/config'

// ----------------------------
// Spring Boot client (/api/predictions)
// ----------------------------
const apiClient = axios.create({
  baseURL: `${API_BASE_URL}/predictions`,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Add request interceptor for JWT + user id
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }

    const user = JSON.parse(localStorage.getItem('user'))
    if (user && user.id) {
      config.headers['X-User-ID'] = user.id
      
      // Inject userId into request body for POST/PUT if not already present
      if (['post', 'put'].includes(config.method.toLowerCase()) && config.data && typeof config.data === 'object') {
        if (!config.data.userId) {
          config.data.userId = user.id
        }
      }
    }

    return config
  },
  (error) => Promise.reject(error)
)

// ----------------------------
// Flask client (prediction microservice)
// ----------------------------
// If your Flask is running on port 5000:
const flaskClient = axios.create({
  baseURL: 'http://127.0.0.1:5000', // Flask base
  headers: {
    'Content-Type': 'application/json',
  },
})

// Optional: reuse auth headers for Flask too (harmless if Flask ignores them)
flaskClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }

    const user = JSON.parse(localStorage.getItem('user'))
    if (user && user.id) {
      config.headers['X-User-ID'] = user.id
    }

    return config
  },
  (error) => Promise.reject(error)
)

// ----------------------------
// Prediction service
// ----------------------------
export const predictionService = {
  // Save health data to Spring Boot
  saveHealthData(data) {
    return apiClient.post('', data)
  },

  // Load saved cases from Spring Boot
  getHealthCases() {
    return apiClient.get('/cases')
  },

  // Delete case from Spring Boot
  deleteCase(caseId) {
    return apiClient.delete(`/cases/${caseId}`)
  },

  // Update case results after predicting
  updateCaseResults(caseId, results) {
    return apiClient.post(`/cases/${caseId}/results`, results)
  },

  // ✅ Predict using Flask with a chosen model
  // healthData: object of features
  // modelType: "svm" | "xgboost" | "random_forest" | "mlp" | "decision_tree"
  predictWithFlask(healthData, modelType = 'svm') {
    return flaskClient.post('/api/predict', {
      ...healthData,
      modelType,
    })
  },

  // ✅ Compare all 5 models at once
  predictAllWithFlask(healthData) {
    return flaskClient.post('/api/predict_all', healthData)
  },

  // Sample data (if exists)
  getSampleData() {
    return axios.get(`${API_BASE_URL}/sample-data`)
  },

  // Local history (demo)
  getPredictionHistory() {
    const history = localStorage.getItem('prediction_history')
    return Promise.resolve({ data: history ? JSON.parse(history) : [] })
  },

  savePrediction(prediction) {
    const history = localStorage.getItem('prediction_history')
    const historyData = history ? JSON.parse(history) : []

    const newPrediction = {
      ...prediction,
      id: new Date().getTime(),
      date: new Date().toISOString(),
    }

    const updatedHistory = [newPrediction, ...historyData]
    localStorage.setItem('prediction_history', JSON.stringify(updatedHistory))

    return Promise.resolve({ data: newPrediction })
  },
}
