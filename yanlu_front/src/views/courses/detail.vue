<template>
  <div class="course-detail" v-if="course">
    <el-card class="detail-card">
      <!-- 课程头部信息 -->
      <div class="course-header">
        <el-image :src="course.image" fit="cover" class="course-image" />
        <div class="course-info">
          <h1 class="title">{{ course.title }}</h1>
          <div class="tags">
            <el-tag size="small" :type="course.status === 'enrolling' ? 'success' : 'info'">
              {{ course.status === 'enrolling' ? '报名中' : '已结束' }}
            </el-tag>
            <el-tag
              v-for="feature in course.features"
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
              <el-icon><Calendar /></el-icon>
              <span>课程时长：{{ course.duration }}天</span>
            </div>
            <div class="meta-item">
              <el-icon><User /></el-icon>
              <span>适合年龄：{{ course.ageGroup }}</span>
            </div>
            <div class="meta-item">
              <el-icon><Location /></el-icon>
              <span>活动地点：{{ course.location }}</span>
            </div>
          </div>
          <div class="price-info">
            <span class="current-price">¥{{ course.price }}</span>
            <span class="original-price" v-if="course.originalPrice">¥{{ course.originalPrice }}</span>
          </div>
          <div class="actions">
            <el-button type="primary" size="large" :disabled="course.status !== 'enrolling'" @click="handleEnroll">
              {{ course.status === 'enrolling' ? '立即报名' : '报名已结束' }}
            </el-button>
            <el-button size="large" @click="handleConsult">在线咨询</el-button>
          </div>
        </div>
      </div>

      <!-- 课程详细信息 -->
      <el-tabs class="detail-tabs">
        <el-tab-pane label="课程介绍">
          <div class="course-description">
            <h3>课程简介</h3>
            <p>{{ course.description }}</p>
            
            <h3>课程特色</h3>
            <ul class="feature-list">
              <li v-for="(feature, index) in course.features" :key="index">
                <el-icon><Check /></el-icon>
                <span>{{ feature }}</span>
              </li>
            </ul>

            <h3>适合人群</h3>
            <p>本课程适合{{ course.ageGroup }}的学生参加，通过专业的课程设计和丰富的实践活动，帮助学生深入了解{{ course.title }}相关知识。</p>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="行程安排">
          <div class="schedule">
            <el-timeline>
              <el-timeline-item
                v-for="(activity, index) in course.schedule"
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
              <li>课程费用</li>
              <li>专业导师指导</li>
              <li>场地使用费</li>
              <li>必要的器材使用</li>
              <li>保险费用</li>
            </ul>

            <h3>费用不含</h3>
            <ul>
              <li>往返交通费</li>
              <li>个人消费</li>
              <li>其他未提及的费用</li>
            </ul>
          </div>
        </el-tab-pane>

        <el-tab-pane label="预订须知">
          <div class="booking-notice">
            <h3>报名流程</h3>
            <ol>
              <li>选择课程并提交报名信息</li>
              <li>支付课程费用</li>
              <li>收到确认短信</li>
              <li>按时参加课程</li>
            </ol>

            <h3>退订政策</h3>
            <ul>
              <li>课程开始7天前取消，全额退款</li>
              <li>课程开始3-7天前取消，退款80%</li>
              <li>课程开始3天内取消，退款50%</li>
              <li>课程开始后不予退款</li>
            </ul>

            <h3>注意事项</h3>
            <ul>
              <li>请准时到达集合地点</li>
              <li>穿着舒适的运动装和运动鞋</li>
              <li>注意防晒和保暖</li>
              <li>听从导师指导，确保安全</li>
            </ul>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const course = ref(null)

// 模拟课程数据库
const coursesData = {
  1: {
    id: 1,
    title: '地质探索之旅',
    description: '探索地球的奥秘，了解地质变迁。通过实地考察、化石挖掘、岩石分析等活动，让学生深入了解地球科学知识，培养科学探究精神。',
    image: 'https://example.com/course1.jpg',
    status: 'enrolling',
    type: 'nature',
    duration: '3',
    ageGroup: '10-12岁',
    location: '云南石林',
    price: 1999,
    originalPrice: 2399,
    features: ['地质探索', '科学实验', '自然观察'],
    schedule: [
      {
        time: '第一天',
        title: '地质博物馆参观',
        description: '参观地质博物馆，了解地球演化历史'
      },
      {
        time: '第二天',
        title: '野外地质考察',
        description: '实地考察石林地貌，学习岩石分类'
      },
      {
        time: '第三天',
        title: '化石挖掘体验',
        description: '参与化石挖掘活动，了解古生物知识'
      }
    ]
  },
  2: {
    id: 2,
    title: '古都文化之旅',
    description: '走访古都，感受历史文化魅力。深入了解中国传统文化，体验非物质文化遗产，培养文化自信。',
    image: 'https://example.com/course2.jpg',
    status: 'enrolling',
    type: 'history',
    duration: '4',
    ageGroup: '13-15岁',
    location: '西安',
    price: 2499,
    originalPrice: 2899,
    features: ['文化体验', '历史探索', '传统技艺'],
    schedule: [
      {
        time: '第一天',
        title: '兵马俑博物馆',
        description: '参观世界第八大奇迹，了解秦朝历史'
      },
      {
        time: '第二天',
        title: '古城墙探访',
        description: '骑行古城墙，体验古都风貌'
      },
      {
        time: '第三天',
        title: '非遗体验',
        description: '体验皮影戏、剪纸等非物质文化遗产'
      },
      {
        time: '第四天',
        title: '大唐文化',
        description: '参观大雁塔，了解唐朝文化'
      }
    ]
  },
  3: {
    id: 3,
    title: '海洋生物探秘',
    description: '探索海洋生物的神奇世界。通过实地观察、互动体验，了解海洋生态系统，培养环保意识。',
    image: 'https://example.com/course3.jpg',
    status: 'enrolling',
    type: 'nature',
    duration: '5',
    ageGroup: '10-12岁',
    location: '青岛',
    price: 2999,
    originalPrice: 3299,
    features: ['海洋生态', '互动体验', '科普教育'],
    schedule: [
      {
        time: '第一天',
        title: '海洋博物馆',
        description: '参观海洋博物馆，了解海洋生物多样性'
      },
      {
        time: '第二天',
        title: '潮间带考察',
        description: '实地考察潮间带生态系统'
      },
      {
        time: '第三天',
        title: '海豚表演',
        description: '观看海豚表演，了解海洋哺乳动物'
      },
      {
        time: '第四天',
        title: '海洋牧场',
        description: '参观海洋牧场，了解海洋养殖'
      },
      {
        time: '第五天',
        title: '海洋科技',
        description: '参观海洋科技馆，了解海洋科技发展'
      }
    ]
  },
  4: {
    id: 4,
    title: '航天科技营',
    description: '走进航天科技，探索太空奥秘。通过模拟训练、科学实验，激发学生对航天科技的兴趣。',
    image: 'https://example.com/course4.jpg',
    status: 'enrolling',
    type: 'technology',
    duration: '7',
    ageGroup: '13-15岁',
    location: '酒泉',
    price: 3999,
    originalPrice: 4499,
    features: ['航天科技', '模拟训练', '创新思维'],
    schedule: [
      {
        time: '第一天',
        title: '航天博物馆',
        description: '参观航天博物馆，了解航天历史'
      },
      {
        time: '第二天',
        title: '航天员训练',
        description: '体验航天员基础训练项目'
      },
      {
        time: '第三天',
        title: '火箭科技',
        description: '了解火箭原理，制作火箭模型'
      },
      {
        time: '第四天',
        title: '空间站生活',
        description: '模拟空间站生活场景'
      },
      {
        time: '第五天',
        title: '天文观测',
        description: '进行天文观测，了解宇宙奥秘'
      },
      {
        time: '第六天',
        title: '航天科技',
        description: '了解最新航天科技发展'
      },
      {
        time: '第七天',
        title: '总结汇报',
        description: '进行课程总结，分享学习心得'
      }
    ]
  }
}

onMounted(() => {
  // 获取路由参数中的课程ID
  const courseId = parseInt(route.params.id)
  // 根据ID获取对应的课程数据
  course.value = coursesData[courseId]
  
  // 如果没有找到对应的课程数据，提示错误并返回列表页
  if (!course.value) {
    ElMessage.error('未找到课程信息')
    router.push('/courses')
  }
})

// 报名处理
const handleEnroll = () => {
  router.push({
    path: '/registration',
    query: { courseId: course.value.id }
  })
}

// 咨询处理
const handleConsult = () => {
  ElMessage.success('客服将会尽快与您联系')
}
</script>

<style lang="scss" scoped>
.course-detail {
  padding: 20px;

  .detail-card {
    .course-header {
      display: flex;
      gap: 30px;
      margin-bottom: 30px;

      .course-image {
        width: 400px;
        height: 300px;
        border-radius: 8px;
      }

      .course-info {
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
            margin-right: 10px;
          }

          .original-price {
            color: #999;
            text-decoration: line-through;
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