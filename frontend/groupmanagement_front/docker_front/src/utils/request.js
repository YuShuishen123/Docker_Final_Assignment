import axios from 'axios'
import router from '../router'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 5000,
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded'
  }
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    // 对于 POST 请求，确保使用正确的格式
    if (config.method.toLowerCase() === 'post') {
      config.headers['Content-Type'] = 'application/x-www-form-urlencoded'
      
      // 如果有 data，确保是 URLSearchParams 格式
      if (!(config.data instanceof URLSearchParams)) {
        const formData = new URLSearchParams()
        for (const key in config.data) {
          formData.append(key, config.data[key])
        }
        config.data = formData
      }
    }
    
    return config
  },
  error => {
    ElMessage.error('请求发送失败')
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 200) {
      return res
    } else {
      ElMessage.error(res.msg || '操作失败')
      return Promise.reject(new Error(res.msg || '操作失败'))
    }
  },
  error => {
    if (error.response) {
      switch (error.response.status) {
        case 400:
          ElMessage.error(error.response.data.msg || '请求参数错误')
          break
        case 401:
          ElMessage.error('未登录或登录已过期')
          localStorage.removeItem('token')
          router.push('/login')
          break
        case 403:
          ElMessage.error('没有操作权限')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器错误，请稍后重试')
          break
        default:
          ElMessage.error('未知错误')
      }
    } else {
      ElMessage.error('网络错误，请检查网络连接')
    }
    return Promise.reject(error)
  }
)

export default request 