import axios from 'axios';

// 糖尿病知识
export function listChronic() {
  return axios.get('/api/admin/chronic')
    .then(res => res.data);
}
export function createChronic(payload) {
  return axios.post('/api/admin/chronic', payload)
    .then(res => res.data);
}
export function updateChronic(id, payload) {
  return axios.put(`/api/admin/chronic/${id}`, payload)
    .then(res => res.data);
}
export function deleteChronic(id) {
  return axios.delete(`/api/admin/chronic/${id}`)
    .then(res => res.data);
}

// 健康生活指南
export function listNutrition() {
  return axios.get('/api/admin/nutrition')
    .then(res => res.data);
}
export function createNutrition(payload) {
  return axios.post('/api/admin/nutrition', payload)
    .then(res => res.data);
}
export function updateNutrition(id, payload) {
  return axios.put(`/api/admin/nutrition/${id}`, payload)
    .then(res => res.data);
}
export function deleteNutrition(id) {
  return axios.delete(`/api/admin/nutrition/${id}`)
    .then(res => res.data);
}

// 最新研究资讯
export function listResearch() {
  return axios.get('/api/admin/research')
    .then(res => res.data);
}
export function createResearch(payload) {
  return axios.post('/api/admin/research', payload)
    .then(res => res.data);
}
export function updateResearch(id, payload) {
  return axios.put(`/api/admin/research/${id}`, payload)
    .then(res => res.data);
}
export function deleteResearch(id) {
  return axios.delete(`/api/admin/research/${id}`)
    .then(res => res.data);
}

// 季节性健康提示
export function listHabit() {
  return axios.get('/api/admin/habits')
    .then(res => res.data);
}
export function createHabit(payload) {
  return axios.post('/api/admin/habits', payload)
    .then(res => res.data);
}
export function updateHabit(id, payload) {
  return axios.put(`/api/admin/habits/${id}`, payload)
    .then(res => res.data);
}
export function deleteHabit(id) {
  return axios.delete(`/api/admin/habits/${id}`)
    .then(res => res.data);
}