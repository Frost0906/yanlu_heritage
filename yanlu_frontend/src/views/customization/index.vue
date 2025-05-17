<template>
  <div class="customization">
    <el-row :gutter="20">
      <!-- 左侧表单区域 -->
      <el-col :span="16">
        <el-card class="form-card">
          <template #header>
            <div class="card-header">
              <h2>线路定制</h2>
              <p>根据您的需求，为您量身定制专属方案</p>
            </div>
          </template>

          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            label-width="120px"
            class="customization-form"
          >
            <!-- 基本信息 -->
            <h3 class="form-section">基本信息</h3>
            <el-form-item label="联系人" prop="contactName">
              <el-input v-model="form.contactName" placeholder="请输入联系人姓名" />
            </el-form-item>
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" />
            </el-form-item>
            <el-form-item label="电子邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入电子邮箱" />
            </el-form-item>

            <!-- 需求信息 -->
            <h3 class="form-section">需求信息</h3>
            <el-form-item label="活动类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择活动类型">
                <el-option label="研学课程" value="course" />
                <el-option label="团建活动" value="teamBuilding" />
              </el-select>
            </el-form-item>
            <el-form-item label="参与人数" prop="peopleCount">
              <el-input-number v-model="form.peopleCount" :min="1" :max="1000" />
            </el-form-item>
            <el-form-item label="预期天数" prop="duration">
              <el-select v-model="form.duration" placeholder="请选择预期天数">
                <el-option label="半天" value="half" />
                <el-option label="1天" value="one" />
                <el-option label="2天" value="two" />
                <el-option label="3天" value="three" />
                <el-option label="3天以上" value="more" />
              </el-select>
            </el-form-item>
            <el-form-item label="预期时间" prop="expectedDate">
              <el-date-picker
                v-model="form.expectedDate"
                type="date"
                placeholder="选择预期活动时间"
                :disabled-date="disabledDate"
              />
            </el-form-item>
            <el-form-item label="预算范围" prop="budget">
              <el-select v-model="form.budget" placeholder="请选择预算范围">
                <el-option label="200元/人以下" value="200" />
                <el-option label="200-500元/人" value="500" />
                <el-option label="500-1000元/人" value="1000" />
                <el-option label="1000元/人以上" value="1000+" />
              </el-select>
            </el-form-item>
            <el-form-item label="目的地偏好" prop="destination">
              <el-input v-model="form.destination" placeholder="请输入期望的目的地（选填）" />
            </el-form-item>
            <el-form-item label="具体需求" prop="requirements">
              <el-input
                v-model="form.requirements"
                type="textarea"
                :rows="4"
                placeholder="请详细描述您的具体需求，包括活动目标、特殊要求等"
              />
            </el-form-item>

            <!-- 提交按钮 -->
            <el-form-item>
              <el-button type="primary" @click="submitForm">提交定制需求</el-button>
              <el-button @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <!-- 右侧信息区域 -->
      <el-col :span="8">
        <!-- AI助手 -->
        <el-card class="ai-assistant">
          <template #header>
            <div class="card-header">
              <h3>AI研学助手</h3>
            </div>
          </template>
          <div class="chat-container">
            <div class="chat-messages" ref="chatMessagesRef">
              <div
                v-for="(message, index) in chatMessages"
                :key="index"
                :class="['message', message.type]"
              >
                <el-avatar
                  :size="32"
                  :src="message.type === 'assistant' ? '/ai-avatar.png' : '/user-avatar.png'"
                />
                <div class="message-content">
                  <p>{{ message.content }}</p>
                  <span class="time">{{ message.time }}</span>
                </div>
              </div>
            </div>
            <div class="chat-input">
              <el-input
                v-model="chatInput"
                placeholder="输入您的问题..."
                @keyup.enter="sendMessage"
              >
                <template #append>
                  <el-button @click="sendMessage">发送</el-button>
                </template>
              </el-input>
            </div>
          </div>
        </el-card>

        <!-- 推荐线路 -->
        <el-card class="recommended-routes">
          <template #header>
            <div class="card-header">
              <h3>推荐线路</h3>
            </div>
          </template>
          <el-carousel height="200px">
            <el-carousel-item v-for="route in recommendedRoutes" :key="route.id">
              <div class="route-item">
                <img :src="route.image" :alt="route.title">
                <div class="route-info">
                  <h4>{{ route.title }}</h4>
                  <p>{{ route.description }}</p>
                  <el-button type="primary" size="small" @click="viewRouteDetail(route)">
                    查看详情
                  </el-button>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </el-card>

        <!-- 咨询客服 -->
        <el-card class="customer-service">
          <template #header>
            <div class="card-header">
              <h3>专业顾问</h3>
            </div>
          </template>
          <div class="service-info">
            <div class="service-item">
              <el-avatar src="/advisor-avatar.png" :size="64" />
              <div class="advisor-info">
                <h4>王老师</h4>
                <p>研学课程设计专家</p>
                <el-button type="primary" @click="contactAdvisor">
                  <el-icon><Phone /></el-icon>
                  立即咨询
                </el-button>
              </div>
            </div>
            <el-divider />
            <div class="contact-methods">
              <p>
                <el-icon><Phone /></el-icon>
                咨询电话：400-xxx-xxxx
              </p>
              <p>
                <el-icon><Message /></el-icon>
                工作时间：周一至周日 9:00-21:00
              </p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const formRef = ref(null)

// 表单数据
const form = reactive({
  contactName: '',
  phone: '',
  email: '',
  type: '',
  peopleCount: 1,
  duration: '',
  expectedDate: '',
  budget: '',
  destination: '',
  requirements: ''
})

// 表单验证规则
const rules = {
  contactName: [
    { required: true, message: '请输入联系人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入电子邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择活动类型', trigger: 'change' }
  ],
  peopleCount: [
    { required: true, message: '请输入参与人数', trigger: 'blur' }
  ],
  duration: [
    { required: true, message: '请选择预期天数', trigger: 'change' }
  ],
  expectedDate: [
    { required: true, message: '请选择预期活动时间', trigger: 'change' }
  ],
  budget: [
    { required: true, message: '请选择预算范围', trigger: 'change' }
  ],
  requirements: [
    { required: true, message: '请描述您的具体需求', trigger: 'blur' },
    { min: 10, max: 500, message: '需求描述长度在 10 到 500 个字符', trigger: 'blur' }
  ]
}

// 禁用过去的日期
const disabledDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate((valid, fields) => {
    if (valid) {
      // TODO: 实现提交逻辑
      ElMessage.success('定制需求提交成功，我们的顾问将尽快与您联系')
      resetForm()
    } else {
      ElMessage.error('请完善必填信息')
      return false
    }
  })
}

// 重置表单
const resetForm = () => {
  if (!formRef.value) return
  formRef.value.resetFields()
}

// AI助手相关
const chatMessages = ref([
  {
    type: 'assistant',
    content: '您好！我是AI研学助手，很高兴为您服务。请问您想了解什么？',
    time: '刚刚'
  }
])
const chatInput = ref('')
const chatMessagesRef = ref(null)

// 发送消息
const sendMessage = () => {
  if (!chatInput.value.trim()) {
    ElMessage.warning('请输入内容')
    return
  }

  // 添加用户消息
  chatMessages.value.push({
    type: 'user',
    content: chatInput.value,
    time: '刚刚'
  })

  // TODO: 调用AI接口获取回复
  setTimeout(() => {
    chatMessages.value.push({
      type: 'assistant',
      content: '我理解您的需求，建议您可以考虑...',
      time: '刚刚'
    })
  }, 1000)

  chatInput.value = ''
}

// 推荐线路数据
const recommendedRoutes = ref([
  {
    id: 1,
    title: '地质探索之旅',
    description: '探索地球的奥秘，了解地质变迁',
    image: 'https://example.com/route1.jpg'
  },
  {
    id: 2,
    title: '古都文化之旅',
    description: '感受历史文化的魅力',
    image: 'https://example.com/route2.jpg'
  }
])

// 查看线路详情
const viewRouteDetail = (route) => {
  // TODO: 实现查看详情逻辑
  ElMessage.info(`查看线路：${route.title}`)
}

// 联系顾问
const contactAdvisor = () => {
  ElMessage.success('正在为您接通顾问...')
}
</script>

<style lang="scss" scoped>
.customization {
  padding: 20px;

  .form-card {
    .card-header {
      text-align: center;
      
      h2 {
        margin: 0 0 10px;
        font-size: 24px;
      }
      
      p {
        margin: 0;
        color: #909399;
      }
    }

    .form-section {
      margin: 20px 0 10px;
      padding-bottom: 10px;
      border-bottom: 1px solid #ebeef5;
      color: #303133;
      font-size: 18px;
    }
  }

  .ai-assistant {
    margin-bottom: 20px;

    .chat-container {
      height: 400px;
      display: flex;
      flex-direction: column;

      .chat-messages {
        flex: 1;
        overflow-y: auto;
        padding: 20px;

        .message {
          display: flex;
          gap: 10px;
          margin-bottom: 20px;

          &.assistant {
            flex-direction: row;

            .message-content {
              background-color: #f4f4f5;
            }
          }

          &.user {
            flex-direction: row-reverse;

            .message-content {
              background-color: #e6f6ff;
            }
          }

          .message-content {
            max-width: 70%;
            padding: 10px 15px;
            border-radius: 8px;

            p {
              margin: 0 0 5px;
              line-height: 1.4;
            }

            .time {
              font-size: 12px;
              color: #909399;
            }
          }
        }
      }

      .chat-input {
        padding: 20px;
        border-top: 1px solid #dcdfe6;
      }
    }
  }

  .recommended-routes {
    margin-bottom: 20px;

    .route-item {
      position: relative;
      height: 100%;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      .route-info {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        padding: 15px;
        background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
        color: #fff;

        h4 {
          margin: 0 0 10px;
          font-size: 18px;
        }

        p {
          margin: 0 0 10px;
          font-size: 14px;
          opacity: 0.9;
        }
      }
    }
  }

  .customer-service {
    .service-info {
      .service-item {
        display: flex;
        align-items: center;
        gap: 20px;
        padding: 20px 0;

        .advisor-info {
          h4 {
            margin: 0 0 5px;
            font-size: 18px;
          }

          p {
            margin: 0 0 10px;
            color: #909399;
          }
        }
      }

      .contact-methods {
        p {
          display: flex;
          align-items: center;
          gap: 8px;
          margin: 10px 0;
          color: #606266;

          .el-icon {
            font-size: 16px;
            color: var(--el-color-primary);
          }
        }
      }
    }
  }
}
</style> 