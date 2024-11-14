<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <template #header>
        <h3>修改个人信息</h3>
      </template>
      
      <el-form
        ref="formRef"
        :model="userForm"
        :rules="formRules"
        label-width="80px">
        <el-form-item label="学号" prop="student_Id">
          <el-input v-model="userForm.student_Id"></el-input>
        </el-form-item>
        
        <el-form-item label="姓名" prop="name">
          <el-input v-model="userForm.name"></el-input>
        </el-form-item>
        
        <el-form-item label="工作" prop="work">
          <el-input v-model="userForm.work"></el-input>
        </el-form-item>
        
        <el-form-item label="班级" prop="class_">
          <el-input v-model="userForm.class_"></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            :loading="loading"
            @click="handleSubmit">
            提交修改
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import request from '../utils/request'

const router = useRouter()
const loading = ref(false)
const formRef = ref()

// 表单数据
const userForm = reactive({
  id: '',
  student_Id: '',
  name: '',
  work: '',
  class_: ''
})

// 表单验证规则
const formRules = {
  student_Id: [
    { required: true, message: '请输入学号', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  work: [
    { required: true, message: '请输入负责工作', trigger: 'blur' }
  ],
  class_: [
    { required: true, message: '请输入班级', trigger: 'blur' }
  ]
}

// 获取当前用户信息
const getUserInfo = () => {
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  const userList = JSON.parse(localStorage.getItem('userList') || '[]')
  // 从请求中获取roleId
  const roleId = localStorage.getItem('roleId')
  
  if (!roleId) {
    ElMessage.warning('未找到角色信息')
    router.push('/login')
    return
  }
  
  const currentUser = userList.find(user => user.role_id === parseInt(roleId))
  
  if (currentUser) {
    Object.assign(userForm, currentUser)
  } else {
    ElMessage.warning('未找到用户信息，请先创建用户信息')
    router.push('/users')
  }
}

// 提交修改
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    loading.value = true
    
    const token = localStorage.getItem('token')
    if (!token) {
      ElMessage.warning('请先登录')
      router.push('/login')
      return
    }

    // 发送请求
    const res = await request.put('/users/update', {
      id: userForm.id,
      student_Id: userForm.student_Id,
      name: userForm.name,
      work: userForm.work,
      class_: userForm.class_
    })
    
    if (res.code === 200) {
      ElMessage.success('修改成功')
      // 更新本地缓存的用户列表
      const userList = JSON.parse(localStorage.getItem('userList') || '[]')
      const index = userList.findIndex(user => user.id === userForm.id)
      if (index !== -1) {
        userList[index] = { ...userForm }
        localStorage.setItem('userList', JSON.stringify(userList))
      }
    } else {
      ElMessage.error(res.msg || '修改失败')
    }
  } catch (error) {
    console.error('修改失败:', error)
    if (error.response?.status === 401) {
      ElMessage.error('登录已过期，请重新登录')
      localStorage.removeItem('token')
      localStorage.removeItem('roleId')
      router.push('/login')
    } else if (error.response?.data?.msg) {
      ElMessage.error(error.response.data.msg)
    } else {
      ElMessage.error('修改失败，请检查网络连接')
    }
  } finally {
    loading.value = false
  }
}

// 页面加载时获取用户信息
onMounted(() => {
  getUserInfo()
})
</script>

<style scoped>
.profile-container {
  padding: 20px;
}

.profile-card {
  max-width: 600px;
  margin: 0 auto;
}

.profile-card :deep(.el-card__header) {
  padding: 15px 20px;
}

.profile-card :deep(.el-card__header h3) {
  margin: 0;
  color: #409EFF;
}
</style> 