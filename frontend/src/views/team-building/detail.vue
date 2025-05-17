<template>
  <div class="project-detail" v-if="project">
    <el-card class="detail-card">
      <!-- 项目头部信息 -->
      <div class="project-header">
        <el-image :src="project.image" fit="cover" class="project-image" />
        <div class="project-info">
          <h1 class="title">{{ project.title }}</h1>
          <div class="tags">
            <el-tag size="small" :type="project.status === 'enrolling' ? 'success' : 'info'">
              {{ project.status === 'enrolling' ? '报名中' : '已结束' }}
            </el-tag>
            <el-tag
              v-for="feature in project.features"
              :key="feature"
              size="small"
              effect="plain"
              class="feature-tag"
            >
              {{ feature }}
            </el-tag>
          </div>
          <div class="meta">
            <div class="meta-item">
              <el-icon><Timer /></el-icon>
              <span>活动时长：{{ project.durationLabel }}</span>
            </div>
            <div class="meta-item">
              <el-icon><User /></el-icon>
              <span>适合人数：{{ project.peopleCount }}人</span>
            </div>
            <div class="meta-item">
              <el-icon><Location /></el-icon>
              <span>活动地点：{{ project.location }}</span>
            </div>
          </div>
          <div class="price-info">
            <span class="current-price">¥{{ project.price }}</span>
            <span class="unit">/人起</span>
          </div>
          <div class="actions">
            <el-button type="primary" size="large" @click="handleConsult">立即报名</el-button>
            <el-button size="large" @click="handleCustomize">定制方案</el-button>
          </div>
        </div>
      </div>

      <!-- 项目详细信息 -->
      <el-tabs class="detail-tabs">
        <el-tab-pane label="项目介绍">
          <div class="project-description">
            <h3>项目简介</h3>
            <p>{{ project.description }}</p>
            
            <h3>项目特色</h3>
            <ul class="feature-list">
              <li v-for="(feature, index) in project.features" :key="index">
                <el-icon><Check /></el-icon>
                <span>{{ feature }}</span>
              </li>
            </ul>

            <h3>适合团队</h3>
            <p>本项目适合{{ project.peopleCount }}人的团队参与，通过精心设计的团建活动，增进团队默契，提升团队凝聚力。</p>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="活动流程">
          <div class="schedule">
            <el-timeline>
              <el-timeline-item
                v-for="(activity, index) in project.schedule"
                :key="index"
                :timestamp="activity.time"
                placement="top"
              >
                <h4>{{ activity.title }}</h4>
                <p>{{ activity.description }}</p>
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-tab-pane>

        <el-tab-pane label="费用说明">
          <div class="price-details">
            <h3>费用包含</h3>
            <ul>
              <li>专业教练服务</li>
              <li>场地使用费</li>
              <li>活动器材</li>
              <li>团建用品</li>
              <li>保险费用</li>
            </ul>

            <h3>费用不含</h3>
            <ul>
              <li>往返交通费</li>
              <li>餐饮费用</li>
              <li>个人消费</li>
              <li>其他未提及的费用</li>
            </ul>
          </div>
        </el-tab-pane>

        <el-tab-pane label="预订须知">
          <div class="booking-notice">
            <h3>预订流程</h3>
            <ol>
              <li>在线咨询，确定具体方案</li>
              <li>签订合同，支付定金</li>
              <li>确认活动细节</li>
              <li>按时参加活动</li>
            </ol>

            <h3>取消政策</h3>
            <ul>
              <li>活动开始7天前取消，扣除10%定金</li>
              <li>活动开始3-7天前取消，扣除30%定金</li>
              <li>活动开始3天内取消，扣除50%定金</li>
              <li>活动开始后不予退款</li>
            </ul>

            <h3>注意事项</h3>
            <ul>
              <li>请提前10分钟到达集合地点</li>
              <li>建议穿着运动服装和运动鞋</li>
              <li>注意人身和财物安全</li>
              <li>遵守活动场地规章制度</li>
            </ul>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 咨询对话框 -->
    <el-dialog
      v-model="consultDialogVisible"
      title="项目咨询"
      width="500px"
    >
      <el-form
        ref="consultFormRef"
        :model="consultForm"
        :rules="consultRules"
        label-width="100px"
      >
        <el-form-item label="联系人" prop="name">
          <el-input v-model="consultForm.name" placeholder="请输入联系人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="consultForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="参与人数" prop="peopleCount">
          <el-input-number
            v-model="consultForm.peopleCount"
            :min="10"
            :max="1000"
            placeholder="请输入参与人数"
          />
        </el-form-item>
        <el-form-item label="预期时间" prop="expectedDate">
          <el-date-picker
            v-model="consultForm.expectedDate"
            type="date"
            placeholder="选择预期活动时间"
            style="width: 100%"
            :disabled-date="disabledDate"
          />
        </el-form-item>
        <el-form-item label="需求描述" prop="description">
          <el-input
            v-model="consultForm.description"
            type="textarea"
            :rows="4"
            placeholder="请描述您的具体需求"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="consultDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitConsult">提交</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Timer, User, Location, Check } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const project = ref(null)
const consultDialogVisible = ref(false)
const consultFormRef = ref(null)

// 模拟项目数据库
const projectsData = {
  1: {
    id: 1,
    title: '趣味运动会',
    description: '通过趣味运动项目增进团队默契。我们精心设计了一系列有趣的团队运动项目，让团队成员在欢乐的氛围中增进了解，提升凝聚力。',
    image: 'https://example.com/project1.jpg',
    status: 'enrolling',
    type: 'sports',
    typeLabel: '趣味运动会',
    duration: 'one',
    durationLabel: '1天',
    peopleCount: '50-200',
    location: '室内体育馆',
    venue: 'indoor',
    features: ['趣味项目', '团队协作', '增进默契'],
    price: 199,
    schedule: [
      {
        time: '09:00',
        title: '破冰活动',
        description: '通过趣味游戏打破陌生感，活跃气氛'
      },
      {
        time: '10:30',
        title: '团队竞技',
        description: '分组进行趣味运动竞赛，培养团队协作'
      },
      {
        time: '14:00',
        title: '大型团建',
        description: '参与大型团队协作项目，提升凝聚力'
      },
      {
        time: '16:30',
        title: '总结分享',
        description: '分享活动感受，巩固团建效果'
      }
    ]
  },
  2: {
    id: 2,
    title: '森林探险',
    description: '在森林中进行团队探险和挑战。通过野外生存技能训练、团队协作任务等活动，增强团队凝聚力和抗压能力。',
    image: 'https://example.com/project2.jpg',
    status: 'enrolling',
    type: 'outdoor',
    typeLabel: '户外拓展',
    duration: 'two',
    durationLabel: '2天',
    peopleCount: '20-100',
    location: '森林公园',
    venue: 'outdoor',
    features: ['野外生存', '团队协作', '挑战极限'],
    price: 399,
    schedule: [
      {
        time: '第一天 09:00',
        title: '安全培训',
        description: '野外安全知识培训和基本技能学习'
      },
      {
        time: '第一天 10:30',
        title: '团队任务',
        description: '完成一系列需要团队合作的户外任务'
      },
      {
        time: '第一天 15:00',
        title: '野外生存',
        description: '学习搭建帐篷、生火等野外生存技能'
      },
      {
        time: '第二天 09:00',
        title: '探险挑战',
        description: '进行森林定向越野和探险活动'
      },
      {
        time: '第二天 14:00',
        title: '团队建设',
        description: '进行团队建设活动和经验总结'
      }
    ]
  },
  3: {
    id: 3,
    title: '创意工作坊',
    description: '激发团队创造力，培养创新思维。通过各种创意活动和工作坊，激发团队成员的创造力，培养创新思维。',
    image: 'https://example.com/project3.jpg',
    status: 'enrolling',
    type: 'creative',
    typeLabel: '创意工作坊',
    duration: 'half',
    durationLabel: '半天',
    peopleCount: '10-50',
    location: '创意空间',
    venue: 'indoor',
    features: ['创意思维', '团队协作', '成果展示'],
    price: 299,
    schedule: [
      {
        time: '13:30',
        title: '创意启发',
        description: '通过趣味游戏激发创意思维'
      },
      {
        time: '14:30',
        title: '创意工作坊',
        description: '分组进行创意项目设计和制作'
      },
      {
        time: '16:00',
        title: '作品展示',
        description: '各组展示创意作品并进行分享'
      },
      {
        time: '17:00',
        title: '总结反馈',
        description: '活动总结和经验分享'
      }
    ]
  },
  4: {
    id: 4,
    title: '文化寻宝之旅',
    description: '探索城市文化遗产，体验传统文化魅力。通过寻宝游戏的形式，让团队成员在探索中了解城市文化，增进团队默契。',
    image: 'https://example.com/project4.jpg',
    status: 'enrolling',
    type: 'culture',
    typeLabel: '文化体验',
    duration: 'one',
    durationLabel: '1天',
    peopleCount: '30-100',
    location: '历史文化街区',
    venue: 'outdoor',
    features: ['文化体验', '团队协作', '知识探索'],
    price: 259,
    schedule: [
      {
        time: '09:00',
        title: '文化导览',
        description: '了解城市历史文化背景'
      },
      {
        time: '10:00',
        title: '寻宝任务',
        description: '分组进行文化寻宝游戏'
      },
      {
        time: '14:00',
        title: '传统体验',
        description: '体验传统文化项目'
      },
      {
        time: '16:00',
        title: '成果分享',
        description: '分享寻宝成果和文化体验感受'
      }
    ]
  }
}

// 咨询表单数据
const consultForm = reactive({
  name: '',
  phone: '',
  peopleCount: 10,
  expectedDate: '',
  description: ''
})

// 表单验证规则
const consultRules = {
  name: [
    { required: true, message: '请输入联系人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  peopleCount: [
    { required: true, message: '请输入参与人数', trigger: 'blur' }
  ],
  expectedDate: [
    { required: true, message: '请选择预期活动时间', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请描述您的具体需求', trigger: 'blur' }
  ]
}

// 禁用过去的日期
const disabledDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7
}

onMounted(() => {
  // 获取路由参数中的项目ID
  const projectId = parseInt(route.params.id)
  // 根据ID获取对应的项目数据
  project.value = projectsData[projectId]
  
  // 如果没有找到对应的项目数据，提示错误并返回列表页
  if (!project.value) {
    ElMessage.error('未找到项目信息')
    router.push('/team-building')
  }
})

// 咨询处理
const handleConsult = () => {
  consultDialogVisible.value = true
}

// 定制方案
const handleCustomize = () => {
  router.push({
    path: '/customization',
    query: { projectId: project.value.id }
  })
}

// 提交咨询
const submitConsult = async () => {
  if (!consultFormRef.value) return
  
  await consultFormRef.value.validate((valid, fields) => {
    if (valid) {
      // TODO: 实现提交逻辑
      ElMessage.success('咨询信息提交成功，我们的顾问将尽快与您联系')
      consultDialogVisible.value = false
    } else {
      ElMessage.error('请完善必填信息')
      return false
    }
  })
}
</script>

<style lang="scss" scoped>
.project-detail {
  padding: 20px;

  .detail-card {
    .project-header {
      display: flex;
      gap: 30px;
      margin-bottom: 30px;

      .project-image {
        width: 400px;
        height: 300px;
        border-radius: 8px;
      }

      .project-info {
        flex: 1;

        .title {
          margin: 0 0 20px;
          font-size: 24px;
          font-weight: bold;
        }

        .tags {
          margin-bottom: 20px;
          
          .feature-tag {
            margin-left: 10px;
          }
        }

        .meta {
          margin-bottom: 20px;

          .meta-item {
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 10px;
            color: #666;
          }
        }

        .price-info {
          margin-bottom: 30px;

          .current-price {
            font-size: 28px;
            color: #f56c6c;
            font-weight: bold;
            margin-right: 5px;
          }

          .unit {
            color: #999;
          }
        }

        .actions {
          display: flex;
          gap: 20px;
        }
      }
    }

    .detail-tabs {
      :deep(.el-tabs__content) {
        padding: 20px 0;
      }

      h3 {
        margin: 20px 0 10px;
        font-size: 18px;
        font-weight: bold;
      }

      .feature-list {
        list-style: none;
        padding: 0;

        li {
          display: flex;
          align-items: center;
          gap: 8px;
          margin-bottom: 10px;
          color: #666;
        }
      }

      .schedule {
        padding: 20px;
      }

      .price-details, .booking-notice {
        ul, ol {
          padding-left: 20px;
          color: #666;
          line-height: 1.8;
        }
      }
    }
  }
}
</style> 