<template>
  <el-dialog
    v-model="dialogVisible"
    :title="isLogin ? '登录' : '注册'"
    width="400px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="80px"
      size="large"
    >
      <el-form-item label="用户名" prop="username">
        <el-input v-model="formData.username" placeholder="请输入用户名" />
      </el-form-item>
      
      <el-form-item label="密码" prop="password">
        <el-input
          v-model="formData.password"
          type="password"
          placeholder="请输入密码"
          show-password
        />
      </el-form-item>

      <el-form-item v-if="!isLogin" label="确认密码" prop="confirmPassword">
        <el-input
          v-model="formData.confirmPassword"
          type="password"
          placeholder="请再次输入密码"
          show-password
        />
      </el-form-item>

      <el-form-item v-if="isLogin">
        <div class="form-options">
          <el-checkbox v-model="rememberMe">记住我</el-checkbox>
          <el-button link type="primary" @click="handleForgotPassword">忘记密码？</el-button>
        </div>
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit">
          {{ isLogin ? '登录' : '注册' }}
        </el-button>
      </div>
      <div class="switch-mode">
        <el-button link type="primary" @click="toggleMode">
          {{ isLogin ? '没有账号？立即注册' : '已有账号？立即登录' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'

const props = defineProps({
  visible: {
    type: Boolean,
    required: true
  }
})

const emit = defineEmits(['update:visible', 'success'])

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const isLogin = ref(true)
const rememberMe = ref(false)

const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

const formData = reactive({
  username: '',
  password: '',
  confirmPassword: ''
})

const validatePass2 = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== formData.password) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}

const rules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validatePass2, trigger: 'blur' }
  ]
})

const handleClose = () => {
  dialogVisible.value = false
  formRef.value?.resetFields()
}

const toggleMode = () => {
  isLogin.value = !isLogin.value
  formRef.value?.resetFields()
}

// 组件挂载时检查是否有记住的登录信息
onMounted(() => {
  const remembered = userStore.getRememberedLogin()
  if (remembered) {
    formData.username = remembered.username
    formData.password = remembered.password
    rememberMe.value = true
  }
})

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid, fields) => {
    if (valid) {
      try {
        const { username, password } = formData
        let success = false
        
        if (isLogin.value) {
          success = await userStore.login(username, password, rememberMe.value)
          if (success) {
            ElMessage.success('登录成功')
            handleClose()
            emit('success')
            
            // 如果有重定向信息，进行重定向
            const redirect = localStorage.getItem('redirect')
            if (redirect) {
              const { path, query } = JSON.parse(redirect)
              router.push({ path, query })
              localStorage.removeItem('redirect')
            }
          } else {
            ElMessage.error('登录失败，请检查用户名和密码')
          }
        } else {
          success = await userStore.register(username, password)
          if (success) {
            ElMessage.success('注册成功，请登录')
            isLogin.value = true
            formRef.value.resetFields()
          } else {
            ElMessage.error('注册失败，请稍后重试')
          }
        }
      } catch (error) {
        console.error(error)
        ElMessage.error(isLogin.value ? '登录失败' : '注册失败')
      }
    } else {
      console.log('error submit!', fields)
    }
  })
}

const handleForgotPassword = () => {
  ElMessage.info('找回密码功能开发中')
}
</script>

<style lang="scss" scoped>
.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dialog-footer {
  text-align: right;
}

.switch-mode {
  margin-top: 16px;
  text-align: center;
}
</style> 