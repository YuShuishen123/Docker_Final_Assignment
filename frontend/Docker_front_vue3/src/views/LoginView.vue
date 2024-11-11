<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2 class="login-title">成员管理系统</h2>
      </template>
      <el-form :model="loginForm" @submit.prevent="handleLogin">
        <el-form-item>
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            prefix-icon="User"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit" class="login-button">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const loginForm = reactive({
  username: '',
  password: ''
})

const handleLogin = async () => {
  try {
    if (!loginForm.username || !loginForm.password) {
      ElMessage.warning('请输入用户名和密码')
      return
    }

    const config = {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      }
    }
    
    const params = new URLSearchParams()
    params.append('username', loginForm.username)
    params.append('password', loginForm.password)
    
    console.log('发送登录请求:', params.toString())
    
    const response = await axios.post(
      'http://localhost:8080/api/auth/login', 
      params,
      config
    )
    
    console.log('登录响应:', response.data)
    
    if (response.data && response.data.userId) {
      localStorage.setItem('userId', response.data.userId)
      localStorage.setItem('member', JSON.stringify(response.data.member))
      if (response.data.token) {
        localStorage.setItem('token', response.data.token)
      }
      ElMessage.success(response.data.message || '登录成功')
      router.push('/members')
    } else {
      ElMessage.error('登录响应格式不正确')
    }
  } catch (error) {
    console.error('登录错误:', error.response?.data || error)
    
    if (error.response?.data) {
      const errorMessage = error.response.data.message || error.response.data.error || '登录失败'
      ElMessage.error(errorMessage)
    } else if (error.request) {
      ElMessage.error('服务器无响应，请检查后端服务是否启动')
    } else {
      ElMessage.error('请求错误: ' + error.message)
    }
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
}

.login-card {
  width: 400px;
}

.login-title {
  text-align: center;
  margin: 0;
  color: #409eff;
}

.login-button {
  width: 100%;
}
</style> 