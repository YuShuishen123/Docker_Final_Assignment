<template>
  <div class="login-container">
    <div class="background-animation">
      <div v-for="n in 6" :key="n" class="circle-container">
        <div class="circle"></div>
      </div>
    </div>
    
    <el-card class="login-card">
      <div class="login-header">
        <div class="logo-container">
          <div class="logo-circle"></div>
          <div class="logo-ring"></div>
        </div>
        <h2 class="login-title">成员管理系统</h2>
        <p class="login-subtitle">Member Management System</p>
      </div>
      
      <el-form 
        :model="loginForm" 
        @submit.prevent="handleLogin"
        class="login-form"
      >
        <el-form-item>
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            :prefix-icon="User"
            class="custom-input"
          >
            <template #prefix>
              <div class="input-icon-wrapper">
                <el-icon><User /></el-icon>
              </div>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item>
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            :prefix-icon="Lock"
            show-password
            class="custom-input"
          >
            <template #prefix>
              <div class="input-icon-wrapper">
                <el-icon><Lock /></el-icon>
              </div>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            native-type="submit" 
            class="login-button"
            :loading="loading"
          >
            {{ loading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import axios from 'axios'

const router = useRouter()
const loading = ref(false)
const loginForm = reactive({
  username: '',
  password: ''
})

const handleLogin = async () => {
  if (!loginForm.username || !loginForm.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }

  loading.value = true
  try {
    const params = new URLSearchParams()
    params.append('username', loginForm.username)
    params.append('password', loginForm.password)
    
    const response = await axios.post(
      'http://localhost:8080/api/auth/login', 
      params,
      {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        }
      }
    )
    
    if (response.data && response.data.userId) {
      localStorage.setItem('userId', response.data.userId)
      localStorage.setItem('member', JSON.stringify(response.data.member))
      ElMessage.success(response.data.message || '登录成功')
      router.push('/members')
    }
  } catch (error) {
    const errorMessage = error.response?.data?.message || '登录失败'
    ElMessage.error(errorMessage)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #1a1a1a 0%, #0a192f 100%);
  position: relative;
  overflow: hidden;
}

.background-animation {
  position: absolute;
  width: 100%;
  height: 100%;
}

.circle-container {
  position: absolute;
  animation: float 15s infinite linear;
}

.circle {
  width: 100px;
  height: 100px;
  background: rgba(64, 158, 255, 0.1);
  border-radius: 50%;
  position: absolute;
  animation: pulse 4s infinite ease-in-out;
}

@keyframes float {
  0% { transform: translate(0, 0) rotate(0deg); }
  100% { transform: translate(400px, 400px) rotate(360deg); }
}

@keyframes pulse {
  0% { transform: scale(1); opacity: 0.3; }
  50% { transform: scale(1.5); opacity: 0.1; }
  100% { transform: scale(1); opacity: 0.3; }
}

.login-card {
  width: 420px;
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
  border-radius: 15px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  animation: cardAppear 0.6s ease-out;
}

@keyframes cardAppear {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo-container {
  position: relative;
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
}

.logo-circle {
  position: absolute;
  width: 60px;
  height: 60px;
  background: #409eff;
  border-radius: 50%;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: logoRotate 4s infinite linear;
}

.logo-ring {
  position: absolute;
  width: 80px;
  height: 80px;
  border: 4px solid #409eff;
  border-radius: 50%;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: ringPulse 2s infinite ease-in-out;
}

@keyframes logoRotate {
  100% { transform: translate(-50%, -50%) rotate(360deg); }
}

@keyframes ringPulse {
  0% { transform: translate(-50%, -50%) scale(1); }
  50% { transform: translate(-50%, -50%) scale(1.1); }
  100% { transform: translate(-50%, -50%) scale(1); }
}

.login-title {
  color: #409eff;
  font-size: 28px;
  margin: 0;
  font-weight: 600;
}

.login-subtitle {
  color: #909399;
  font-size: 14px;
  margin-top: 5px;
}

.login-form {
  padding: 0 20px;
}

.custom-input :deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  transition: all 0.3s ease;
}

.custom-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.1);
}

.custom-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #409eff;
}

.input-icon-wrapper {
  display: flex;
  align-items: center;
  color: #409eff;
  font-size: 18px;
}

.login-button {
  width: 100%;
  height: 44px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  letter-spacing: 1px;
  background: linear-gradient(45deg, #409eff, #36cfc9);
  border: none;
  transition: all 0.3s ease;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
  background: linear-gradient(45deg, #36cfc9, #409eff);
}

.login-button:active {
  transform: translateY(0);
}

/* 为圆圈设置不同的初始位置和动画延迟 */
.circle-container:nth-child(1) { top: 20%; left: 20%; animation-delay: 0s; }
.circle-container:nth-child(2) { top: 60%; left: 40%; animation-delay: -2s; }
.circle-container:nth-child(3) { top: 40%; left: 60%; animation-delay: -4s; }
.circle-container:nth-child(4) { top: 80%; left: 20%; animation-delay: -6s; }
.circle-container:nth-child(5) { top: 30%; left: 80%; animation-delay: -8s; }
.circle-container:nth-child(6) { top: 70%; left: 70%; animation-delay: -10s; }

.circle:nth-child(odd) {
  animation-direction: alternate;
}
</style> 