<template>
  <div class="members-container">
    <el-container>
      <el-header>
        <div class="header-content">
          <h2>成员管理系统</h2>
          <el-button type="danger" @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      <el-main>
        <div class="cards-container">
          <el-card v-for="member in members" :key="member.id" class="member-card">
            <template #header>
              <div class="card-header">
                <span>成员信息</span>
                <el-button
                  v-if="member.userId === currentUserId"
                  type="primary"
                  @click="openEditDialog(member)"
                >
                  修改
                </el-button>
              </div>
            </template>
            <div class="member-info">
              <p><strong>姓名：</strong>{{ member.name }}</p>
              <p><strong>学号：</strong>{{ member.studentNumber }}</p>
            </div>
          </el-card>
        </div>
      </el-main>
    </el-container>

    <el-dialog v-model="editDialogVisible" title="修改信息" width="500px">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="学号">
          <el-input v-model="editForm.studentNumber" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleEdit">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const members = ref([])
const currentUserId = parseInt(localStorage.getItem('userId'))
const editDialogVisible = ref(false)
const editForm = ref({})
const currentEditId = ref(null)

// 创建axios实例，设置默认配置
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
axiosInstance.interceptors.request.use(config => {
  const userId = localStorage.getItem('userId')
  
  if (!userId) {
    // 如果没有 userId，直接跳转到登录页
    router.push('/login')
    return Promise.reject('未登录')
  }
  
  // 设置请求头
  config.headers['userId'] = userId
  return config
}, error => {
  return Promise.reject(error)
})

// 添加响应拦截器
axiosInstance.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401 || error.response?.data?.message?.includes('未登录')) {
      ElMessage.error('请先登录')
      handleLogout()
    }
    return Promise.reject(error)
  }
)

const fetchMembers = async () => {
  try {
    const response = await axiosInstance.get('/members')
    members.value = response.data
  } catch (error) {
    handleError(error)
  }
}

const openEditDialog = (member) => {
  editForm.value = {
    name: member.name,
    studentNumber: member.studentNumber
  }
  currentEditId.value = member.id
  editDialogVisible.value = true
}

const handleEdit = async () => {
  try {
    const response = await axiosInstance.put(
      `/members/${currentEditId.value}`,
      {
        name: editForm.value.name,
        studentNumber: editForm.value.studentNumber
      }
    )
    
    ElMessage.success('更新成功')
    editDialogVisible.value = false
    fetchMembers()
  } catch (error) {
    if (error === '未登录') return // 已经在拦截器中处理
    handleError(error)
  }
}

const handleError = (error) => {
  console.error('请求错误:', error.response?.data || error)
  
  if (error.response?.status === 401) {
    ElMessage.error('请先登录')
    handleLogout()
    return
  }
  
  ElMessage.error(error.response?.data?.message || '操作失败')
}

const handleLogout = () => {
  localStorage.clear() // 清除所有存储的信息
  router.push('/login')
}

onMounted(() => {
  if (!currentUserId) {
    router.push('/login')
    return
  }
  fetchMembers()
})
</script>

<style scoped>
.members-container {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 100%;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.cards-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  padding: 20px;
}

.member-card {
  transition: all 0.3s;
}

.member-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.member-info p {
  margin: 10px 0;
}
</style> 