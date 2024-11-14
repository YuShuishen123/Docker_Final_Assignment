<template>
  <el-container class="layout-container">
    <!-- 头部导航 -->
    <el-header class="header">
      <div class="header-left">
        <h2>成员管理系统</h2>
      </div>
      <div class="header-right">
        <span class="user-info">
          <el-icon><User /></el-icon>
          {{ userInfo.roleName }} ({{ userInfo.username }})
        </span>
        <el-dropdown @command="handleCommand">
          <el-button type="primary" icon="Setting">
            设置
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              <el-dropdown-item command="deleteRole" divided>注销角色</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="200px">
        <el-menu
          :default-active="activeMenu"
          class="side-menu"
          router>
          <el-menu-item index="/members">
            <el-icon><List /></el-icon>
            <span>全部成员</span>
          </el-menu-item>
          <el-menu-item index="/profile">
            <el-icon><Edit /></el-icon>
            <span>修改个人信息</span>
          </el-menu-item>
          <el-menu-item index="/role">
            <el-icon><Setting /></el-icon>
            <span>修改角色信息</span>
          </el-menu-item>
          <el-menu-item index="/users">
            <el-icon><UserFilled /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主要内容区 -->
      <el-main>
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>

  <!-- 确认对话框 -->
  <el-dialog
    v-model="showConfirmDialog"
    :title="confirmDialogTitle"
    width="300px">
    <span>{{ confirmDialogMessage }}</span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="showConfirmDialog = false">取消</el-button>
        <el-button 
          type="primary" 
          :loading="confirmLoading"
          @click="handleConfirm">
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'
import { User, List, Edit, Setting, UserFilled } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// 当前激活的菜单项
const activeMenu = computed(() => route.path)

// 用户信息 (实际项目中应该从状态管理或API获取)
const userInfo = ref({
  username: localStorage.getItem('username') || '',
  roleName: localStorage.getItem('roleName') || ''
})

// 确认对话框相关
const showConfirmDialog = ref(false)
const confirmDialogTitle = ref('')
const confirmDialogMessage = ref('')
const confirmLoading = ref(false)
const confirmAction = ref(null)

// 处理下拉菜单命令
const handleCommand = (command) => {
  switch (command) {
    case 'logout':
      showConfirmDialog.value = true
      confirmDialogTitle.value = '退出登录'
      confirmDialogMessage.value = '确定要退出登录吗？'
      confirmAction.value = handleLogout
      break
    case 'deleteRole':
      showConfirmDialog.value = true
      confirmDialogTitle.value = '注销角色'
      confirmDialogMessage.value = '注销后将无法恢复，确定要注销吗？'
      confirmAction.value = handleDeleteRole
      break
  }
}

// 处理确认
const handleConfirm = () => {
  if (confirmAction.value) {
    confirmAction.value()
  }
}

// 退出登录
const handleLogout = async () => {
  try {
    confirmLoading.value = true
    const res = await request.post('/roles/logout')
    if (res.code === 200) {
      ElMessage.success('退出成功')
      clearUserInfo()
      router.push('/login')
    } else {
      ElMessage.error(res.msg)
    }
  } catch (error) {
    console.error('退出失败:', error)
    ElMessage.error('退出失败')
  } finally {
    confirmLoading.value = false
    showConfirmDialog.value = false
  }
}

// 注销角色
const handleDeleteRole = async () => {
  try {
    confirmLoading.value = true
    const res = await request.delete('/roles/deleteRole')
    if (res.code === 200) {
      ElMessage.success('注销成功')
      clearUserInfo()
      router.push('/login')
    } else {
      ElMessage.error(res.msg)
    }
  } catch (error) {
    console.error('注销失败:', error)
    ElMessage.error('注销失败')
  } finally {
    confirmLoading.value = false
    showConfirmDialog.value = false
  }
}

// 清除用户信息
const clearUserInfo = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  localStorage.removeItem('roleName')
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #dcdfe6;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-left h2 {
  margin: 0;
  color: #409eff;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.side-menu {
  height: calc(100vh - 60px);
  border-right: none;
}

.el-main {
  background-color: #f5f7fa;
  padding: 20px;
}

/* 路由过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 