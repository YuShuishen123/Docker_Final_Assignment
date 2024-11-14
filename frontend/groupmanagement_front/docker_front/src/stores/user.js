import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    username: localStorage.getItem('username') || '',
    roleName: localStorage.getItem('roleName') || ''
  }),
  
  actions: {
    setUserInfo(token, username, roleName) {
      this.token = token
      this.username = username
      this.roleName = roleName
      
      localStorage.setItem('token', token)
      localStorage.setItem('username', username)
      localStorage.setItem('roleName', roleName)
    },
    
    clearUserInfo() {
      this.token = ''
      this.username = ''
      this.roleName = ''
      
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      localStorage.removeItem('roleName')
    }
  }
}) 