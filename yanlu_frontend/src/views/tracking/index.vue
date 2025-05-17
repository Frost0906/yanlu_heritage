<template>
  <div class="tracking">
    <el-row :gutter="20">
      <!-- 左侧行程列表 -->
      <el-col :span="6">
        <el-card class="trip-list">
          <template #header>
            <div class="card-header">
              <h3>我的行程</h3>
              <el-input
                v-model="searchKeyword"
                placeholder="搜索行程"
                prefix-icon="Search"
                clearable
              />
            </div>
          </template>
          
          <el-menu
            :default-active="activeTrip"
            @select="handleTripSelect"
          >
            <el-menu-item
              v-for="trip in filteredTrips"
              :key="trip.id"
              :index="trip.id"
            >
              <div class="trip-item">
                <div class="trip-info">
                  <h4>{{ trip.title }}</h4>
                  <p class="time">
                    <el-icon><Calendar /></el-icon>
                    {{ trip.startDate }} - {{ trip.endDate }}
                  </p>
                </div>
                <el-tag :type="trip.status === 'active' ? 'success' : 'info'">
                  {{ trip.status === 'active' ? '进行中' : '已结束' }}
                </el-tag>
              </div>
            </el-menu-item>
          </el-menu>
        </el-card>
      </el-col>

      <!-- 右侧行程详情 -->
      <el-col :span="18">
        <template v-if="currentTrip">
          <el-card class="trip-header">
            <div class="trip-title">
              <h2>{{ currentTrip.title }}</h2>
              <div class="trip-meta">
                <span>
                  <el-icon><Calendar /></el-icon>
                  {{ currentTrip.startDate }} - {{ currentTrip.endDate }}
                </span>
                <span>
                  <el-icon><Location /></el-icon>
                  {{ currentTrip.location }}
                </span>
                <span>
                  <el-icon><User /></el-icon>
                  带队老师：{{ currentTrip.teacher }}
                </span>
              </div>
            </div>
          </el-card>

          <!-- 实时位置 -->
          <el-card class="location-card" v-if="currentTrip.status === 'active'">
            <template #header>
              <div class="card-header">
                <h3>实时位置</h3>
                <span class="update-time">更新时间：{{ currentTime }}</span>
              </div>
            </template>
            <div class="map-container">
              <!-- 这里集成实际的地图组件 -->
              <div class="map-placeholder">
                地图加载中...
              </div>
            </div>
          </el-card>

          <!-- 行程进度 -->
          <el-card class="schedule-card">
            <template #header>
              <div class="card-header">
                <h3>行程进度</h3>
              </div>
            </template>
            <el-timeline>
              <el-timeline-item
                v-for="(activity, index) in currentTrip.schedule"
                :key="index"
                :type="getTimelineItemType(activity.status)"
                :color="getTimelineItemColor(activity.status)"
                :timestamp="activity.time"
              >
                <div class="timeline-content">
                  <h4>{{ activity.title }}</h4>
                  <p>{{ activity.description }}</p>
                  <div class="activity-photos" v-if="activity.photos && activity.photos.length">
                    <el-image
                      v-for="(photo, photoIndex) in activity.photos"
                      :key="photoIndex"
                      :src="photo"
                      :preview-src-list="activity.photos"
                      fit="cover"
                    />
                  </div>
                </div>
              </el-timeline-item>
            </el-timeline>
          </el-card>

          <!-- 紧急联系人 -->
          <el-card class="contacts-card">
            <template #header>
              <div class="card-header">
                <h3>紧急联系人</h3>
              </div>
            </template>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="带队老师">
                {{ currentTrip.teacher }}
                <el-button link type="primary" @click="handleCall(currentTrip.teacherPhone)">
                  <el-icon><Phone /></el-icon>
                  {{ currentTrip.teacherPhone }}
                </el-button>
              </el-descriptions-item>
              <el-descriptions-item label="安全负责人">
                {{ currentTrip.safetyOfficer }}
                <el-button link type="primary" @click="handleCall(currentTrip.safetyOfficerPhone)">
                  <el-icon><Phone /></el-icon>
                  {{ currentTrip.safetyOfficerPhone }}
                </el-button>
              </el-descriptions-item>
              <el-descriptions-item label="24小时值班电话">
                <el-button link type="primary" @click="handleCall('400-xxx-xxxx')">
                  <el-icon><Phone /></el-icon>
                  400-xxx-xxxx
                </el-button>
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </template>

        <el-empty
          v-else
          description="请选择要查看的行程"
        />
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'

// 搜索关键词
const searchKeyword = ref('')

// 行程数据
const trips = ref([
  {
    id: '1',
    title: '地质探索之旅',
    startDate: '2024-07-01',
    endDate: '2024-07-03',
    location: '云南石林风景区',
    status: 'active',
    teacher: '张老师',
    teacherPhone: '13800138000',
    safetyOfficer: '李安全',
    safetyOfficerPhone: '13900139000',
    schedule: [
      {
        time: '2024-07-01 09:00',
        title: '集合出发',
        description: '在学校门口集合，准时出发',
        status: 'completed',
        photos: [
          'https://example.com/photo1.jpg',
          'https://example.com/photo2.jpg'
        ]
      },
      {
        time: '2024-07-01 12:00',
        title: '抵达石林',
        description: '抵达石林景区，开始地质考察活动',
        status: 'completed',
        photos: [
          'https://example.com/photo3.jpg'
        ]
      },
      {
        time: '2024-07-01 15:00',
        title: '地质讲解',
        description: '专业地质专家讲解岩石形成过程',
        status: 'in-progress'
      },
      {
        time: '2024-07-01 18:00',
        title: '晚餐与总结',
        description: '享用晚餐，总结今天的收获',
        status: 'pending'
      }
    ]
  },
  // 更多行程数据...
])

// 当前时间
const currentTime = ref(new Date().toLocaleString())
setInterval(() => {
  currentTime.value = new Date().toLocaleString()
}, 1000)

// 筛选后的行程列表
const filteredTrips = computed(() => {
  if (!searchKeyword.value) return trips.value
  const keyword = searchKeyword.value.toLowerCase()
  return trips.value.filter(trip => 
    trip.title.toLowerCase().includes(keyword) ||
    trip.location.toLowerCase().includes(keyword)
  )
})

// 当前选中的行程ID
const activeTrip = ref('')

// 当前行程详情
const currentTrip = computed(() => 
  trips.value.find(trip => trip.id === activeTrip.value)
)

// 选择行程
const handleTripSelect = (tripId) => {
  activeTrip.value = tripId
}

// 获取时间线项目类型
const getTimelineItemType = (status) => {
  switch (status) {
    case 'completed':
      return 'success'
    case 'in-progress':
      return 'primary'
    case 'pending':
      return 'info'
    default:
      return ''
  }
}

// 获取时间线项目颜色
const getTimelineItemColor = (status) => {
  switch (status) {
    case 'completed':
      return '#67C23A'
    case 'in-progress':
      return '#409EFF'
    case 'pending':
      return '#909399'
    default:
      return ''
  }
}

// 处理拨打电话
const handleCall = (phone) => {
  ElMessage.success(`正在拨打电话：${phone}`)
  // TODO: 实现实际的拨打电话功能
}
</script>

<style lang="scss" scoped>
.tracking {
  padding: 20px;

  .trip-list {
    .card-header {
      h3 {
        margin: 0 0 15px;
      }
    }

    .trip-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 10px 0;

      .trip-info {
        h4 {
          margin: 0 0 5px;
          font-size: 16px;
        }

        .time {
          display: flex;
          align-items: center;
          gap: 5px;
          color: #909399;
          font-size: 12px;
          margin: 0;
        }
      }
    }
  }

  .trip-header {
    margin-bottom: 20px;

    .trip-title {
      h2 {
        margin: 0 0 15px;
      }

      .trip-meta {
        display: flex;
        gap: 20px;
        color: #606266;

        span {
          display: flex;
          align-items: center;
          gap: 5px;
        }
      }
    }
  }

  .location-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      h3 {
        margin: 0;
      }

      .update-time {
        color: #909399;
        font-size: 14px;
      }
    }

    .map-container {
      height: 300px;
      background-color: #f5f7fa;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 4px;
    }
  }

  .schedule-card {
    margin-bottom: 20px;

    .timeline-content {
      h4 {
        margin: 0 0 10px;
        font-size: 16px;
      }

      p {
        color: #606266;
        margin: 0 0 10px;
      }

      .activity-photos {
        display: flex;
        gap: 10px;
        margin-top: 10px;

        .el-image {
          width: 100px;
          height: 100px;
          border-radius: 4px;
        }
      }
    }
  }

  .contacts-card {
    .el-button {
      margin-left: 10px;
    }
  }
}
</style> 