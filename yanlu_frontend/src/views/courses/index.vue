<template>
  <div class="courses">
    <!-- 搜索和筛选区域 -->
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="报名状态">
          <el-select v-model="filterForm.status" placeholder="选择报名状态">
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="课程类型">
          <el-select v-model="filterForm.type" placeholder="选择课程类型">
            <el-option
              v-for="item in courseTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="适合年龄">
          <el-select v-model="filterForm.ageGroup" placeholder="选择年龄段">
            <el-option
              v-for="item in ageGroups"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="课程时长">
          <el-select v-model="filterForm.duration" placeholder="选择课程时长">
            <el-option
              v-for="item in durations"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="价格区间">
          <el-select v-model="filterForm.priceRange" placeholder="选择价格区间">
            <el-option
              v-for="item in priceRanges"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="filterForm.keyword"
            placeholder="搜索课程名称"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 课程列表 -->
    <div class="course-list">
      <el-empty
        v-if="!courses.length"
        description="暂无符合条件的课程"
        :image-size="200"
      >
        <template #image>
          <el-icon :size="60" color="#909399"><Search /></el-icon>
        </template>
      </el-empty>
      <el-row v-else :gutter="20">
        <el-col
          v-for="course in courses"
          :key="course.id"
          :xs="24"
          :sm="12"
          :md="8"
          :lg="6"
        >
          <el-card class="course-card" :body-style="{ padding: '0px' }" @click="handleCourseClick(course)">
            <div class="course-image">
              <el-image :src="course.image" fit="cover" />
              <div class="course-tags">
                <el-tag size="small" :type="course.status === 'enrolling' ? 'success' : 'info'">
                  {{ course.status === 'enrolling' ? '报名中' : '已结束' }}
                </el-tag>
              </div>
            </div>
            <div class="course-info">
              <h3 class="course-title">{{ course.title }}</h3>
              <p class="course-desc">{{ course.description }}</p>
              <div class="course-meta">
                <div class="meta-item">
                  <el-icon><Calendar /></el-icon>
                  <span>{{ course.duration }}天</span>
                </div>
                <div class="meta-item">
                  <el-icon><User /></el-icon>
                  <span>{{ course.ageGroup }}</span>
                </div>
                <div class="meta-item">
                  <el-icon><Location /></el-icon>
                  <span>{{ course.location }}</span>
                </div>
              </div>
              <div class="course-features">
                <el-tag
                  v-for="(feature, index) in course.features"
                  :key="index"
                  size="small"
                  effect="plain"
                >
                  {{ feature }}
                </el-tag>
              </div>
              <div class="course-footer">
                <div class="price">
                  <span class="amount">¥{{ course.price }}</span>
                  <span class="original" v-if="course.originalPrice">¥{{ course.originalPrice }}</span>
                </div>
                <el-button
                  type="primary"
                  size="small"
                  :disabled="course.status !== 'enrolling'"
                  @click="handleEnroll(course)"
                >
                  {{ course.status === 'enrolling' ? '立即报名' : '已结束' }}
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[12, 24, 36, 48]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

// 筛选表单数据
const filterForm = reactive({
  status: '',
  type: '',
  ageGroup: '',
  duration: '',
  priceRange: '',
  keyword: ''
})

// 监听筛选条件变化
watch(filterForm, (newVal) => {
  loadCourses()
}, { deep: true })

// 选项数据
const statusOptions = [
  { value: '', label: '全部' },
  { value: 'enrolling', label: '报名中' },
  { value: 'ended', label: '报名结束' },
  { value: 'planning', label: '规划中' }
]

const courseTypes = [
  { value: '', label: '全部' },
  { value: 'nature', label: '自然科学' },
  { value: 'history', label: '历史文化' },
  { value: 'art', label: '艺术素养' },
  { value: 'technology', label: '科技创新' }
]

const ageGroups = [
  { value: '', label: '全部' },
  { value: '6-9', label: '6-9岁' },
  { value: '10-12', label: '10-12岁' },
  { value: '13-15', label: '13-15岁' },
  { value: '16-18', label: '16-18岁' }
]

const durations = [
  { value: '', label: '全部' },
  { value: '1', label: '1天' },
  { value: '2-3', label: '2-3天' },
  { value: '4-7', label: '4-7天' },
  { value: '7+', label: '7天以上' }
]

const priceRanges = [
  { value: '', label: '全部' },
  { value: '0-1000', label: '1000元以下' },
  { value: '1000-2000', label: '1000-2000元' },
  { value: '2000-3000', label: '2000-3000元' },
  { value: '3000+', label: '3000元以上' }
]

// 分页数据
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

// 展示用的课程列表
const courses = ref([])

// 所有课程数据（模拟数据库数据）
const allCourses = ref([
  {
    id: 1,
    title: '地质探索之旅',
    description: '探索地球的奥秘，了解地质变迁',
    image: 'https://example.com/course1.jpg',
    status: 'enrolling',
    type: 'nature',
    duration: '3',
    ageGroup: '10-12岁',
    location: '云南石林',
    price: 1999,
    originalPrice: 2399,
    features: ['地质探索', '科学实验', '自然观察']
  },
  {
    id: 2,
    title: '古都文化之旅',
    description: '走访古都，感受历史文化魅力',
    image: 'https://example.com/course2.jpg',
    status: 'enrolling',
    type: 'history',
    duration: '4',
    ageGroup: '13-15岁',
    location: '西安',
    price: 2499,
    originalPrice: 2899,
    features: ['文化体验', '历史探索', '传统技艺']
  },
  {
    id: 3,
    title: '海洋生物探秘',
    description: '探索海洋生物的神奇世界',
    image: 'https://example.com/course3.jpg',
    status: 'enrolling',
    type: 'nature',
    duration: '5',
    ageGroup: '10-12岁',
    location: '青岛',
    price: 2999,
    originalPrice: 3299,
    features: ['海洋生态', '互动体验', '科普教育']
  },
  {
    id: 4,
    title: '航天科技营',
    description: '走进航天科技，探索太空奥秘',
    image: 'https://example.com/course4.jpg',
    status: 'enrolling',
    type: 'technology',
    duration: '7',
    ageGroup: '13-15岁',
    location: '酒泉',
    price: 3999,
    originalPrice: 4499,
    features: ['航天科技', '模拟训练', '创新思维']
  }
])

// 加载课程数据
const loadCourses = () => {
  const filteredCourses = allCourses.value.filter(course => {
    let match = true
    
    // 报名状态筛选
    if (filterForm.status && course.status !== filterForm.status) {
      match = false
    }
    
    // 课程类型筛选
    if (filterForm.type && course.type !== filterForm.type) {
      match = false
    }
    
    // 年龄段筛选
    if (filterForm.ageGroup && course.ageGroup !== filterForm.ageGroup) {
      match = false
    }
    
    // 课程时长筛选
    if (filterForm.duration) {
      const [min, max] = filterForm.duration.split('-').map(Number)
      const courseDuration = parseInt(course.duration)
      if (max) {
        if (courseDuration < min || courseDuration > max) {
          match = false
        }
      } else {
        if (courseDuration < min) {
          match = false
        }
      }
    }
    
    // 价格区间筛选
    if (filterForm.priceRange) {
      const [min, max] = filterForm.priceRange.split('-').map(Number)
      if (max) {
        if (course.price < min || course.price > max) {
          match = false
        }
      } else {
        if (course.price < min) {
          match = false
        }
      }
    }
    
    // 关键词搜索
    if (filterForm.keyword) {
      const keyword = filterForm.keyword.toLowerCase()
      const matchFields = [
        course.title,
        course.description,
        course.location
      ].map(field => field.toLowerCase())
      
      if (!matchFields.some(field => field.includes(keyword))) {
        match = false
      }
    }
    
    return match
  })
  
  // 更新总数
  total.value = filteredCourses.length
  
  // 计算当前页的数据
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  courses.value = filteredCourses.slice(start, end)
  
  // 如果当前页没有数据且不是第一页，回到第一页
  if (courses.value.length === 0 && currentPage.value > 1) {
    currentPage.value = 1
    loadCourses()
  }
}

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1 // 重置到第一页
  loadCourses()
}

// 重置筛选
const resetFilter = () => {
  Object.keys(filterForm).forEach(key => {
    filterForm[key] = ''
  })
  currentPage.value = 1 // 重置到第一页
  loadCourses()
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1 // 重置到第一页
  loadCourses()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadCourses()
}

// 初始化
onMounted(() => {
  loadCourses()
})

// 报名处理
const handleEnroll = (course) => {
  router.push({
    path: '/registration',
    query: { courseId: course.id }
  })
}

// 修改报名处理函数
const handleCourseClick = (course) => {
  router.push({
    name: 'courseDetail',
    params: { id: course.id }
  })
}

// 初始化：检查URL参数
if (route.query.id) {
  const courseId = parseInt(route.query.id)
  const course = allCourses.value.find(c => c.id === courseId)
  if (course) {
    // 如果是从首页点击课程进来，自动设置相应的筛选条件
    filterForm.type = course.type
    loadCourses()
  }
}
</script>

<style lang="scss" scoped>
.courses {
  padding: 20px;

  .filter-card {
    margin-bottom: 20px;

    .filter-form {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
    }
  }

  .course-list {
    min-height: 400px;

    :deep(.el-empty) {
      padding: 60px 0;
    }

    .course-card {
      margin-bottom: 20px;
      transition: transform 0.3s;

      &:hover {
        transform: translateY(-5px);
      }

      .course-image {
        position: relative;
        height: 200px;

        .el-image {
          width: 100%;
          height: 100%;
        }

        .course-tags {
          position: absolute;
          top: 10px;
          right: 10px;
        }
      }

      .course-info {
        padding: 15px;

        .course-title {
          margin: 0 0 10px;
          font-size: 18px;
          font-weight: bold;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .course-desc {
          height: 40px;
          margin-bottom: 10px;
          color: #666;
          font-size: 14px;
          line-height: 1.4;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
        }

        .course-meta {
          display: flex;
          justify-content: space-between;
          margin-bottom: 15px;
          color: #909399;
          font-size: 13px;

          .meta-item {
            display: flex;
            align-items: center;
            gap: 5px;
          }
        }

        .course-features {
          margin-bottom: 15px;
          display: flex;
          flex-wrap: wrap;
          gap: 8px;
        }

        .course-footer {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .price {
            .amount {
              color: #f56c6c;
              font-size: 20px;
              font-weight: bold;
              margin-right: 5px;
            }

            .original {
              color: #909399;
              text-decoration: line-through;
              font-size: 14px;
            }
          }
        }
      }
    }
  }

  .pagination {
    margin-top: 30px;
    text-align: center;
  }
}
</style> 