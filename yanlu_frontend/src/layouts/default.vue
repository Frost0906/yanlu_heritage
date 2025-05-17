<template>
  <div class="layout-default">
    <el-container>
      <el-header height="60px">
        <nav class="nav-container">
          <div class="logo">
            <router-link to="/">研学课程与团建项目平台</router-link>
          </div>
          <div class="menu-container">
            <el-menu
              mode="horizontal"
              :router="true"
              :default-active="activeMenu"
            >
              <el-menu-item index="/">首页</el-menu-item>
              <el-menu-item index="/courses">研学课程</el-menu-item>
              <el-menu-item index="/team-building">团建项目</el-menu-item>
              <el-menu-item index="/registration">在线报名</el-menu-item>
              <el-menu-item index="/tracking">行程跟踪</el-menu-item>
              <el-menu-item index="/community">学习交流</el-menu-item>
              <el-menu-item index="/customization">线路定制</el-menu-item>
            </el-menu>
          </div>
          <div class="user-actions">
            <el-dropdown v-if="userStore.token" @command="handleCommand">
              <span class="el-dropdown-link">
                {{ userStore.userInfo?.username || '用户' }}
                <el-icon class="el-icon--right">
                  <arrow-down />
                </el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <el-button v-else type="primary" @click="showLoginDialog = true">登录/注册</el-button>
          </div>
        </nav>
      </el-header>
      
      <el-main>
        <router-view />
      </el-main>
      
      <el-footer height="60px">
        <div class="footer-content">
          <p>&copy; 2024 研学课程与团建项目平台 版权所有</p>
        </div>
      </el-footer>
    </el-container>

    <!-- 登录/注册弹窗 -->
    <login-dialog
      v-model:visible="showLoginDialog"
      @success="handleLoginSuccess"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ArrowDown } from '@element-plus/icons-vue'
import LoginDialog from '../components/LoginDialog.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const showLoginDialog = ref(false)

const activeMenu = computed(() => route.path)

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'logout':
      userStore.logout()
      router.push('/')
      break
  }
}

const handleLoginSuccess = () => {
  showLoginDialog.value = false
}
</script>

<style lang="scss" scoped>
.layout-default {
  min-height: 100vh;
  
  .el-container {
    height: 100vh;
  }
  
  .el-header {
    background-color: #fff;
    border-bottom: 1px solid #eee;
    padding: 0;
    
    .nav-container {
      max-width: 1200px;
      margin: 0 auto;
      height: 100%;
      display: flex;
      align-items: center;
      padding: 0 20px;
      
      .logo {
        width: 180px;
        font-size: 18px;
        font-weight: bold;
        white-space: nowrap;
        
        a {
          color: #333;
          text-decoration: none;
        }
      }
      
      .menu-container {
        flex: 1;
        display: flex;
        justify-content: center;
        margin-right: 20px;
        
        .el-menu {
          border-bottom: none;
          display: flex;
          justify-content: space-between;
          width: 100%;
          max-width: 800px;
          
          .el-menu-item {
            flex: 1;
            text-align: center;
            font-size: 15px;
            padding: 0;
            min-width: auto;
          }
        }
      }
      
      .user-actions {
        width: 100px;
        display: flex;
        justify-content: flex-end;
        margin-left: auto;
        
        .el-dropdown-link {
          cursor: pointer;
          display: flex;
          align-items: center;
          color: #333;
          white-space: nowrap;
          
          .el-icon {
            margin-left: 5px;
          }
        }

        .el-button {
          white-space: nowrap;
        }
      }
    }
  }
  
  .el-main {
    background-color: #f5f5f5;
    padding: 20px;
    
    :deep(.el-card) {
      margin-bottom: 20px;
    }
  }
  
  .el-footer {
    background-color: #fff;
    border-top: 1px solid #eee;
    
    .footer-content {
      max-width: 1200px;
      margin: 0 auto;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      
      p {
        color: #666;
        margin: 0;
      }
    }
  }
}
</style> 