<template>
  <div class="users-container">
    <!-- 如果用户未创建，显示创建用户表单 -->
    <el-card v-if="!hasCreatedUser" class="user-card">
      <template #header>
        <h3>创建用户信息</h3>
      </template>
      
      <el-form
        ref="createFormRef"
        :model="createForm"
        :rules="formRules"
        label-width="80px">
        <el-form-item label="学号" prop="student_Id">
          <el-input v-model="createForm.student_Id"></el-input>
        </el-form-item>
        
        <el-form-item label="姓名" prop="name">
          <el-input v-model="createForm.name"></el-input>
        </el-form-item>
        
        <el-form-item label="工作" prop="work">
          <el-input v-model="createForm.work"></el-input>
        </el-form-item>
        
        <el-form-item label="班级" prop="class_">
          <el-input v-model="createForm.class_"></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            :loading="loading"
            @click="handleCreate">
            创建用户
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 如果用户已创建，显示用户信息和修改表单 -->
    <el-card v-else class="user-card">
      <template #header>
        <div class="card-header">
          <h3>用户信息管理</h3>
          <el-button 
            type="danger" 
            @click="handleDelete"
            :loading="deleteLoading">
            删除用户
          </el-button>
        </div>
      </template>
      
      <el-form
        ref="updateFormRef"
        :model="updateForm"
        :rules="formRules"
        label-width="80px">
        <el-form-item label="学号" prop="student_Id">
          <el-input v-model="updateForm.student_Id"></el-input>
        </el-form-item>
        
        <el-form-item label="姓名" prop="name">
          <el-input v-model="updateForm.name"></el-input>
        </el-form-item>
        
        <el-form-item label="工作" prop="work">
          <el-input v-model="updateForm.work"></el-input>
        </el-form-item>
        
        <el-form-item label="班级" prop="class_">
          <el-input v-model="updateForm.class_"></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            :loading="loading"
            @click="handleUpdate">
            保存修改
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 确认删除对话框 -->
    <el-dialog
      v-model="showDeleteConfirm"
      title="确认删除"
      width="300px">
      <span>确定要删除用户信息吗？此操作不可恢复。</span>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showDeleteConfirm = false">取消</el-button>
          <el-button 
            type="danger" 
            @click="confirmDelete">
            确定删除
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import request from '../utils/request'

const router = useRouter()
const loading = ref(false)
const deleteLoading = ref(false)
const showDeleteConfirm = ref(false)
const hasCreatedUser = ref(false)
const createFormRef = ref()
const updateFormRef = ref()

// 创建用户表单
const createForm = reactive({
  student_Id: '',
  name: '',
  work: '',
  class_: ''
})

// 更新用户表单
const updateForm = reactive({
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

// 检查用户是否已创建
const checkUserExists = () => {
  try {
    const token = localStorage.getItem('token')
    if (!token) {
      ElMessage.warning('请先登录')
      router.push('/login')
      return
    }

    const roleId = localStorage.getItem('roleId')
    if (!roleId) {
      ElMessage.warning('未找到角色信息')
      router.push('/login')
      return
    }

    const userList = JSON.parse(localStorage.getItem('userList') || '[]')
    const currentUser = userList.find(user => user.role_id === parseInt(roleId))
    
    if (currentUser) {
      hasCreatedUser.value = true
      Object.assign(updateForm, currentUser)
    } else {
      hasCreatedUser.value = false
      // 重置创建表单
      createFormRef.value?.resetFields()
    }
  } catch (error) {
    console.error('检查用户状态失败:', error)
    ElMessage.error('获取用户信息失败')
    router.push('/login')
  }
}

// 创建用户
const handleCreate = async () => {
  try {
    await createFormRef.value.validate()
    loading.value = true
    
    // 检查是否已经创建过用户
    const userList = JSON.parse(localStorage.getItem('userList') || '[]')
    const roleId = localStorage.getItem('roleId')
    const existingUser = userList.find(user => user.role_id === parseInt(roleId))
    
    if (existingUser) {
      ElMessage.error('该角色已创建用户，请勿重复创建')
      return
    }

    // 创建 URLSearchParams 对象
    const formData = new URLSearchParams()
    formData.append('student_Id', createForm.student_Id)
    formData.append('name', createForm.name)
    formData.append('work', createForm.work)
    formData.append('class_', createForm.class_)

    // 严格按照 curl 命令的方式发送请求
    const res = await request({
      url: '/users/add',
      method: 'POST',
      data: formData,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    
    if (res.code === 200) {
      ElMessage.success('创建成功')
      // 更新本地缓存
      const newUser = {
        ...res.data,
        role_id: parseInt(roleId)
      }
      userList.push(newUser)
      localStorage.setItem('userList', JSON.stringify(userList))
      hasCreatedUser.value = true
      Object.assign(updateForm, newUser)
    } else {
      ElMessage.error(res.msg || '创建失败')
    }
  } catch (error) {
    console.error('创建失败:', error)
    if (error.response?.status === 401) {
      ElMessage.error('登录已过期，请重新登录')
      localStorage.removeItem('token')
      router.push('/login')
    } else if (error.response?.data?.msg) {
      ElMessage.error(error.response.data.msg)
    } else {
      ElMessage.error('创建失败，请检查网络连接')
    }
  } finally {
    loading.value = false
  }
}

// 更新用户信息
const handleUpdate = async () => {
  try {
    await updateFormRef.value.validate()
    loading.value = true
    
    const res = await request({
      url: '/users/update',
      method: 'PUT',
      data: {
        id: updateForm.id,
        student_Id: updateForm.student_Id,
        name: updateForm.name,
        work: updateForm.work,
        class_: updateForm.class_
      }
    })
    
    if (res.code === 200) {
      ElMessage.success('修改成功')
      // 更新本地缓存
      const userList = JSON.parse(localStorage.getItem('userList') || '[]')
      const index = userList.findIndex(user => user.id === updateForm.id)
      if (index !== -1) {
        userList[index] = { ...updateForm }
        localStorage.setItem('userList', JSON.stringify(userList))
      }
    }
  } catch (error) {
    console.error('修改失败:', error)
    if (error.response?.status === 401) {
      ElMessage.error('登录已过期，请重新登录')
      localStorage.removeItem('token')
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

// 删除用户
const handleDelete = () => {
  showDeleteConfirm.value = true
}

const confirmDelete = async () => {
  try {
    deleteLoading.value = true
    
    const res = await request({
      url: '/users/delete',
      method: 'DELETE'
    })
    
    if (res.code === 200) {
      ElMessage.success('删除成功')
      // 更新本地缓存
      const userList = JSON.parse(localStorage.getItem('userList') || '[]')
      const index = userList.findIndex(user => user.id === updateForm.id)
      if (index !== -1) {
        userList.splice(index, 1)
        localStorage.setItem('userList', JSON.stringify(userList))
      }
      hasCreatedUser.value = false
      showDeleteConfirm.value = false
      // 重置创建表单
      createFormRef.value?.resetFields()
    }
  } catch (error) {
    console.error('删除失败:', error)
    if (error.response?.data?.msg) {
      ElMessage.error(error.response.data.msg)
    } else {
      ElMessage.error('删除失败，请检查网络连接')
    }
  } finally {
    deleteLoading.value = false
  }
}

// 页面加载时检查用户状态
onMounted(() => {
  checkUserExists()
})
</script>

<style scoped>
.users-container {
  padding: 20px;
}

.user-card {
  max-width: 600px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  color: #409EFF;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 