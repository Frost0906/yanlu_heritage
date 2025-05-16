import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)

  // 从localStorage加载用户信息
  try {
    const savedUserInfo = localStorage.getItem('userInfo')
    if (savedUserInfo) {
      userInfo.value = JSON.parse(savedUserInfo)
    }
  } catch (error) {
    console.error('Failed to load user info:', error)
  }

  // 设置token
  const setToken = (newToken) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  // 清除token
  const clearToken = () => {
    token.value = ''
    localStorage.removeItem('token')
  }

  // 设置用户信息
  const setUserInfo = (info) => {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  // 清除用户信息
  const clearUserInfo = () => {
    userInfo.value = null
    localStorage.removeItem('userInfo')
  }

  // 记住登录状态
  const rememberLogin = (username, password) => {
    localStorage.setItem('rememberedLogin', JSON.stringify({ username, password }))
  }

  // 获取记住的登录信息
  const getRememberedLogin = () => {
    try {
      const saved = localStorage.getItem('rememberedLogin')
      return saved ? JSON.parse(saved) : null
    } catch (error) {
      console.error('Failed to load remembered login:', error)
      return null
    }
  }

  // 清除记住的登录信息
  const clearRememberedLogin = () => {
    localStorage.removeItem('rememberedLogin')
  }

  // 登录
  const login = async (username, password, remember = false) => {
    try {
      // TODO: 调用登录API
      const response = await fetch('/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      })
      
      const data = await response.json()
      if (data.token) {
        setToken(data.token)
        setUserInfo(data.userInfo)
        
        // 如果选择记住登录状态
        if (remember) {
          rememberLogin(username, password)
        } else {
          clearRememberedLogin()
        }
        
        return true
      }
      return false
    } catch (error) {
      console.error('登录失败:', error)
      return false
    }
  }

  // 注册
  const register = async (username, password) => {
    try {
      // TODO: 调用注册API
      const response = await fetch('/api/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      })
      
      const data = await response.json()
      return data.success
    } catch (error) {
      console.error('注册失败:', error)
      return false
    }
  }

  // 登出
  const logout = () => {
    clearToken()
    clearUserInfo()
  }

  return {
    token,
    userInfo,
    login,
    register,
    logout,
    setUserInfo,
    getRememberedLogin,
    clearRememberedLogin,
  }
}) 