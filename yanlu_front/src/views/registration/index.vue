<template>
  <div class="registration">
    <el-card class="registration-card">
      <template #header>
        <div class="card-header">
          <h2>在线报名</h2>
          <p>请填写以下信息完成报名</p>
        </div>
      </template>

      <el-steps :active="activeStep" finish-status="success" class="registration-steps">
        <el-step title="选择课程" />
        <el-step title="填写信息" />
        <el-step title="确认支付" />
      </el-steps>

      <!-- 步骤1：选择课程 -->
      <div v-if="activeStep === 0" class="step-content">
        <div class="selected-course" v-if="selectedCourse">
          <el-card>
            <div class="course-info">
              <img :src="selectedCourse.image" :alt="selectedCourse.title">
              <div class="course-details">
                <h3>{{ selectedCourse.title }}</h3>
                <p class="time">
                  <el-icon><Calendar /></el-icon>
                  开始时间：{{ selectedCourse.startDate }}
                </p>
                <p class="duration">
                  <el-icon><Timer /></el-icon>
                  课程时长：{{ selectedCourse.duration }}
                </p>
                <p class="price">
                  <span class="amount">¥{{ selectedCourse.price }}</span>
                  <span class="original" v-if="selectedCourse.originalPrice">¥{{ selectedCourse.originalPrice }}</span>
                </p>
              </div>
            </div>
          </el-card>
        </div>
        <div v-else class="course-selection">
          <el-empty description="请先选择要报名的课程" />
          <el-button type="primary" @click="$router.push('/courses')">去选择课程</el-button>
        </div>
      </div>

      <!-- 步骤2：填写信息 -->
      <div v-if="activeStep === 1" class="step-content">
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="120px"
          class="registration-form"
        >
          <!-- 学员信息 -->
          <h3 class="form-section-title">学员信息</h3>
          <el-form-item label="学员姓名" prop="studentName">
            <el-input v-model="form.studentName" placeholder="请输入学员姓名" />
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-radio-group v-model="form.gender">
              <el-radio label="male">男</el-radio>
              <el-radio label="female">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="出生日期" prop="birthday">
            <el-date-picker
              v-model="form.birthday"
              type="date"
              placeholder="选择日期"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="身份证号" prop="idCard">
            <el-input v-model="form.idCard" placeholder="请输入身份证号" />
          </el-form-item>
          <el-form-item label="学校名称" prop="school">
            <el-input v-model="form.school" placeholder="请输入学校名称" />
          </el-form-item>

          <!-- 家长信息 -->
          <h3 class="form-section-title">家长信息</h3>
          <el-form-item label="家长姓名" prop="parentName">
            <el-input v-model="form.parentName" placeholder="请输入家长姓名" />
          </el-form-item>
          <el-form-item label="联系电话" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入联系电话" />
          </el-form-item>
          <el-form-item label="电子邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请输入电子邮箱" />
          </el-form-item>
          <el-form-item label="紧急联系人" prop="emergencyContact">
            <el-input v-model="form.emergencyContact" placeholder="请输入紧急联系人姓名" />
          </el-form-item>
          <el-form-item label="紧急联系电话" prop="emergencyPhone">
            <el-input v-model="form.emergencyPhone" placeholder="请输入紧急联系电话" />
          </el-form-item>

          <!-- 其他信息 -->
          <h3 class="form-section-title">其他信息</h3>
          <el-form-item label="特殊饮食要求">
            <el-input
              v-model="form.dietaryRequirements"
              type="textarea"
              placeholder="如有特殊饮食要求请说明"
            />
          </el-form-item>
          <el-form-item label="备注">
            <el-input
              v-model="form.remarks"
              type="textarea"
              placeholder="如有其他需要说明的信息请填写"
            />
          </el-form-item>
        </el-form>
      </div>

      <!-- 步骤3：确认支付 -->
      <div v-if="activeStep === 2" class="step-content">
        <div class="payment-info">
          <el-descriptions title="订单信息" :column="1" border>
            <el-descriptions-item label="课程名称">{{ selectedCourse?.title }}</el-descriptions-item>
            <el-descriptions-item label="学员姓名">{{ form.studentName }}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{ form.phone }}</el-descriptions-item>
            <el-descriptions-item label="应付金额">
              <span class="payment-amount">¥{{ selectedCourse?.price }}</span>
            </el-descriptions-item>
          </el-descriptions>

          <div class="payment-methods">
            <h3>选择支付方式</h3>
            <el-radio-group v-model="paymentMethod">
              <el-radio label="wechat">
                <el-icon><WechatFilled /></el-icon>
                微信支付
              </el-radio>
              <el-radio label="alipay">
                <img src="@/assets/alipay-icon.png" alt="支付宝" class="payment-icon">
                支付宝
              </el-radio>
            </el-radio-group>
          </div>
        </div>
      </div>

      <!-- 步骤控制按钮 -->
      <div class="step-actions">
        <el-button v-if="activeStep > 0" @click="prevStep">上一步</el-button>
        <el-button
          v-if="activeStep < 2"
          type="primary"
          @click="nextStep"
        >下一步</el-button>
        <el-button
          v-else
          type="primary"
          @click="handlePayment"
        >确认支付</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

// 当前步骤
const activeStep = ref(0)

// 课程和项目数据库
const coursesData = {
  1: {
    id: 1,
    title: '地质探索之旅',
    image: 'https://example.com/course1.jpg',
    startDate: '2024-07-01',
    duration: '3天',
    price: 1999,
    originalPrice: 2399,
    type: 'course'
  },
  2: {
    id: 2,
    title: '古都文化之旅',
    image: 'https://example.com/course2.jpg',
    startDate: '2024-07-15',
    duration: '4天',
    price: 2499,
    originalPrice: 2899,
    type: 'course'
  },
  3: {
    id: 3,
    title: '海洋生物探秘',
    image: 'https://example.com/course3.jpg',
    startDate: '2024-08-01',
    duration: '5天',
    price: 2999,
    originalPrice: 3299,
    type: 'course'
  },
  4: {
    id: 4,
    title: '航天科技营',
    image: 'https://example.com/course4.jpg',
    startDate: '2024-08-15',
    duration: '7天',
    price: 3999,
    originalPrice: 4499,
    type: 'course'
  }
}

const projectsData = {
  1: {
    id: 1,
    title: '趣味运动会',
    image: 'https://example.com/project1.jpg',
    startDate: '2024-07-01',
    duration: '1天',
    price: 199,
    type: 'project'
  },
  2: {
    id: 2,
    title: '森林探险',
    image: 'https://example.com/project2.jpg',
    startDate: '2024-07-15',
    duration: '2天',
    price: 399,
    type: 'project'
  },
  3: {
    id: 3,
    title: '创意工作坊',
    image: 'https://example.com/project3.jpg',
    startDate: '2024-08-01',
    duration: '半天',
    price: 299,
    type: 'project'
  },
  4: {
    id: 4,
    title: '文化寻宝之旅',
    image: 'https://example.com/project4.jpg',
    startDate: '2024-08-15',
    duration: '1天',
    price: 259,
    type: 'project'
  }
}

// 选中的课程或项目
const selectedCourse = ref(null)

// 初始化数据
onMounted(() => {
  const { courseId, projectId } = route.query
  
  if (courseId) {
    // 如果是研学课程
    selectedCourse.value = coursesData[courseId]
    if (!selectedCourse.value) {
      ElMessage.error('未找到课程信息')
      router.push('/courses')
    }
  } else if (projectId) {
    // 如果是团建项目
    selectedCourse.value = projectsData[projectId]
    if (!selectedCourse.value) {
      ElMessage.error('未找到项目信息')
      router.push('/team-building')
    }
  }
  
  // 如果没有选择任何课程或项目
  if (!selectedCourse.value) {
    ElMessage.warning('请先选择要报名的课程或项目')
    router.push('/courses')
  }
})

// 表单数据
const form = reactive({
  studentName: '',
  gender: '',
  birthday: '',
  idCard: '',
  school: '',
  parentName: '',
  phone: '',
  email: '',
  emergencyContact: '',
  emergencyPhone: '',
  dietaryRequirements: '',
  remarks: ''
})

// 表单验证规则
const rules = {
  studentName: [
    { required: true, message: '请输入学员姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  birthday: [
    { required: true, message: '请选择出生日期', trigger: 'change' }
  ],
  idCard: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号', trigger: 'blur' }
  ],
  parentName: [
    { required: true, message: '请输入家长姓名', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入电子邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  emergencyContact: [
    { required: true, message: '请输入紧急联系人', trigger: 'blur' }
  ],
  emergencyPhone: [
    { required: true, message: '请输入紧急联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

const formRef = ref(null)
const paymentMethod = ref('wechat')

// 下一步
const nextStep = async () => {
  if (activeStep.value === 1) {
    // 表单验证
    await formRef.value.validate((valid, fields) => {
      if (valid) {
        activeStep.value++
      } else {
        ElMessage.error('请完善必填信息')
        return false
      }
    })
  } else {
    activeStep.value++
  }
}

// 上一步
const prevStep = () => {
  activeStep.value--
}

// 处理支付
const handlePayment = () => {
  if (!paymentMethod.value) {
    ElMessage.warning('请选择支付方式')
    return
  }
  // TODO: 实现支付逻辑
  ElMessage.success('正在跳转到支付页面...')
}
</script>

<style lang="scss" scoped>
.registration {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;

  .registration-card {
    .card-header {
      text-align: center;
      
      h2 {
        margin: 0;
        font-size: 24px;
        color: #303133;
      }
      
      p {
        margin: 10px 0 0;
        color: #909399;
      }
    }
  }

  .registration-steps {
    margin: 40px 0;
  }

  .step-content {
    margin-bottom: 40px;

    .selected-course {
      .course-info {
        display: flex;
        gap: 20px;

        img {
          width: 200px;
          height: 150px;
          object-fit: cover;
          border-radius: 4px;
        }

        .course-details {
          flex: 1;

          h3 {
            margin: 0 0 15px;
            font-size: 20px;
          }

          p {
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 10px;
            color: #606266;

            .el-icon {
              font-size: 16px;
            }
          }

          .price {
            .amount {
              color: #f56c6c;
              font-size: 24px;
              font-weight: bold;
              margin-right: 10px;
            }

            .original {
              color: #909399;
              text-decoration: line-through;
            }
          }
        }
      }
    }

    .course-selection {
      text-align: center;
      padding: 40px 0;
    }
  }

  .registration-form {
    max-width: 600px;
    margin: 0 auto;

    .form-section-title {
      margin: 30px 0 20px;
      padding-left: 10px;
      border-left: 4px solid var(--el-color-primary);
      font-size: 18px;
    }
  }

  .payment-info {
    max-width: 600px;
    margin: 0 auto;

    .payment-amount {
      color: #f56c6c;
      font-size: 20px;
      font-weight: bold;
    }

    .payment-methods {
      margin-top: 30px;

      h3 {
        margin-bottom: 20px;
      }

      .el-radio-group {
        display: flex;
        gap: 30px;

        .el-radio {
          display: flex;
          align-items: center;
          padding: 15px 30px;
          border: 1px solid #dcdfe6;
          border-radius: 4px;

          &.is-checked {
            border-color: var(--el-color-primary);
          }

          .el-icon {
            font-size: 24px;
            margin-right: 8px;
          }

          .payment-icon {
            width: 24px;
            height: 24px;
            margin-right: 8px;
          }
        }
      }
    }
  }

  .step-actions {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin-top: 40px;
  }
}
</style> 