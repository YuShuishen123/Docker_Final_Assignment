<template>
  <div class="members-container">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="请输入关键字搜索"
        class="search-input"
        clearable
        @input="handleSearch">
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </div>

    <!-- 成员列表 -->
    <el-table
      v-loading="loading"
      :data="filteredMembers"
      border
      style="width: 100%">
      <el-table-column
        prop="id"
        label="ID"
        width="80">
      </el-table-column>
      <el-table-column
        prop="student_Id"
        label="学号"
        width="120">
      </el-table-column>
      <el-table-column
        prop="name"
        label="姓名"
        width="120">
      </el-table-column>
      <el-table-column
        prop="work"
        label="负责工作">
      </el-table-column>
      <el-table-column
        prop="class_"
        label="班级"
        width="120">
      </el-table-column>
      <el-table-column
        prop="role_id"
        label="角色ID"
        width="100">
      </el-table-column>
    </el-table>

    <!-- 分页器 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 30, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange">
      </el-pagination>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import request from '../utils/request'

// 数据相关
const members = ref([])
const loading = ref(false)
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 获取所有用户数据
const getAllUsers = async () => {
  try {
    loading.value = true
    // 首先尝试从localStorage获取数据
    const cachedData = localStorage.getItem('userList')
    if (cachedData) {
      members.value = JSON.parse(cachedData)
      total.value = members.value.length
      return
    }

    // 如果没有缓存数据，则从服务器获取
    const res = await request.get('/users/getAllUser')
    if (res.code === 200) {
      members.value = res.data || []
      total.value = members.value.length
      // 更新缓存
      localStorage.setItem('userList', JSON.stringify(res.data))
    } else {
      ElMessage.error(res.msg || '获取用户列表失败')
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
    ElMessage.error('获取用户列表失败，请重新登录')
    // 如果获取失败，可能是token过期，清除缓存并跳转到登录页
    localStorage.removeItem('token')
    localStorage.removeItem('userList')
    router.push('/login')
  } finally {
    loading.value = false
  }
}

// 搜索过滤
const filteredMembers = computed(() => {
  const keyword = searchKeyword.value.toLowerCase()
  let filtered = members.value
  
  if (keyword) {
    filtered = members.value.filter(member => 
      (member.student_Id && member.student_Id.toString().toLowerCase().includes(keyword)) ||
      (member.name && member.name.toLowerCase().includes(keyword)) ||
      (member.work && member.work.toLowerCase().includes(keyword)) ||
      (member.class_ && member.class_.toLowerCase().includes(keyword))
    )
  }
  
  total.value = filtered.length
  
  return filtered.slice(
    (currentPage.value - 1) * pageSize.value,
    currentPage.value * pageSize.value
  )
})

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1 // 重置页码
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
}

const handleCurrentChange = (val) => {
  currentPage.value = val
}

// 页面加载时获取数据
onMounted(() => {
  getAllUsers()
})
</script>

<style scoped>
.members-container {
  padding: 20px;
}

.search-bar {
  margin-bottom: 20px;
}

.search-input {
  width: 300px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 