/**
 * 应用全局配置
 */

// API基础URL - 使用相对路径配合Vite代理
export const API_BASE_URL = 'http://localhost:8080/api'


const config = {
  // 默认请求超时时间
  requestTimeout: 15000,
  
  // 默认分页大小
  defaultPageSize: 10,
  
  // 是否开启调试模式
  isDev: process.env.NODE_ENV === 'development',
  
  // 上传文件大小限制（单位：MB）
  maxUploadSize: 5,
  
  // localStorage 存储前缀
  storagePrefix: 'chronic_disease_',
  
  // 用户角色
  roles: {
    ADMIN: 'ADMIN',
    DOCTOR: 'DOCTOR',
    USER: 'USER'
  },
  
  // 预测风险级别
  riskLevels: {
    LOW: {text: '低风险', color: 'success', threshold: 0.3},
    MEDIUM: {text: '中度风险', color: 'warning', threshold: 0.6},
    HIGH: {text: '高风险', color: 'error', threshold: 1.0}
  }
};

export default config; 