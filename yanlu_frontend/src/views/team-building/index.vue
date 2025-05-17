<template>
  <div class="team-building">
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
        <el-form-item label="活动类型">
          <el-select v-model="filterForm.type" placeholder="选择活动类型">
            <el-option
              v-for="item in activityTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="参与人数">
          <el-select v-model="filterForm.peopleCount" placeholder="选择人数范围">
            <el-option
              v-for="item in peopleCountRanges"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="活动时长">
          <el-select v-model="filterForm.duration" placeholder="选择活动时长">
            <el-option
              v-for="item in durations"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="场地类型">
          <el-select v-model="filterForm.venue" placeholder="选择场地类型">
            <el-option
              v-for="item in venueTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="filterForm.keyword"
            placeholder="搜索项目名称"
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

    <!-- 项目列表 -->
    <div class="project-list">
      <el-empty
        v-if="!projects.length"
        description="暂无符合条件的项目"
        :image-size="200"
      >
        <template #image>
          <el-icon :size="60" color="#909399"><Search /></el-icon>
        </template>
      </el-empty>
      <el-row v-else :gutter="20">
        <el-col
          v-for="project in projects"
          :key="project.id"
          :xs="24"
          :sm="12"
          :md="6"
          :lg="6"
        >
          <el-card class="project-card" :body-style="{ padding: '0px' }" @click="handleProjectClick(project)">
            <div class="project-image">
              <el-image :src="project.image" fit="cover" />
              <div class="project-tags">
                <el-tag size="small" :type="project.status === 'enrolling' ? 'success' : 'info'">
                  {{ project.status === 'enrolling' ? '报名中' : '已结束' }}
                </el-tag>
              </div>
            </div>
            <div class="project-info">
              <h3 class="project-title">{{ project.title }}</h3>
              <p class="project-desc">{{ project.description }}</p>
              <div class="project-meta">
                <div class="meta-item">
                  <el-icon><Timer /></el-icon>
                  <span>{{ project.durationLabel }}</span>
                </div>
                <div class="meta-item">
                  <el-icon><User /></el-icon>
                  <span>{{ project.peopleCount }}人</span>
                </div>
                <div class="meta-item">
                  <el-icon><Location /></el-icon>
                  <span>{{ project.location }}</span>
                </div>
              </div>
              <div class="project-features">
                <el-tag
                  v-for="(feature, index) in project.features"
                  :key="index"
                  size="small"
                  effect="plain"
                >
                  {{ feature }}
                </el-tag>
              </div>
              <div class="project-footer">
                <div class="price">
                  <span class="amount">¥{{ project.price }}</span>
                  <span class="unit">/人起</span>
                </div>
                <el-button type="primary" size="small" @click="handleConsult(project)">
                  立即报名
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
import { ref, reactive, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()

// 筛选表单数据
const filterForm = reactive({
  status: '',
  type: '',
  peopleCount: '',
  duration: '',
  venue: '',
  keyword: ''
})

// 选项数据
const statusOptions = [
  { value: '', label: '全部' },
  { value: 'enrolling', label: '报名中' },
  { value: 'ended', label: '报名结束' },
  { value: 'planning', label: '规划中' }
]

const activityTypes = [
  { value: '', label: '全部' },
  { value: 'outdoor', label: '户外拓展' },
  { value: 'creative', label: '创意工作坊' },
  { value: 'sports', label: '趣味运动会' },
  { value: 'culture', label: '文化体验' }
]

const peopleCountRanges = [
  { value: '', label: '全部' },
  { value: '10-50', label: '10-50人' },
  { value: '51-100', label: '51-100人' },
  { value: '101-200', label: '101-200人' },
  { value: '200+', label: '200人以上' }
]

const durations = [
  { value: '', label: '全部' },
  { value: 'half', label: '半天' },
  { value: 'one', label: '1天' },
  { value: 'two', label: '2天' },
  { value: 'more', label: '2天以上' }
]

const venueTypes = [
  { value: '', label: '全部' },
  { value: 'indoor', label: '室内场地' },
  { value: 'outdoor', label: '户外场地' },
  { value: 'hotel', label: '酒店场地' },
  { value: 'park', label: '主题乐园' }
]

// 分页数据
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

// 项目列表数据
const allProjects = ref([
  {
    id: 1,
    title: '趣味运动会',
    description: '通过趣味运动项目增进团队默契',
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
    price: 199
  },
  {
    id: 2,
    title: '森林探险',
    description: '在森林中进行团队探险和挑战',
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
    price: 399
  },
  {
    id: 3,
    title: '创意工作坊',
    description: '激发团队创造力，培养创新思维',
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
    price: 299
  },
  {
    id: 4,
    title: '文化寻宝之旅',
    description: '探索城市文化遗产，体验传统文化魅力',
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
    price: 259
  }
])

// 展示用的项目列表
const projects = ref([])

// 咨询相关
const consultDialogVisible = ref(false)
const consultFormRef = ref(null)
const selectedProject = ref(null)

const consultForm = reactive({
  name: '',
  phone: '',
  peopleCount: 10,
  expectedDate: '',
  description: ''
})

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

// 监听筛选条件变化
watch(filterForm, (newVal) => {
  loadProjects()
}, { deep: true })

// 加载项目数据
const loadProjects = () => {
  const filteredProjects = allProjects.value.filter(project => {
    let match = true
    
    // 报名状态筛选
    if (filterForm.status && project.status !== filterForm.status) {
      match = false
    }
    
    // 活动类型筛选
    if (filterForm.type && project.type !== filterForm.type) {
      match = false
    }
    
    // 参与人数筛选
    if (filterForm.peopleCount) {
      const [filterMin, filterMax] = filterForm.peopleCount.split('-').map(num => num === '+' ? Infinity : Number(num))
      const [projectMin, projectMax] = project.peopleCount.split('-').map(Number)
      
      if (projectMin < filterMin || (projectMax && projectMax > (filterMax || Infinity))) {
        match = false
      }
    }
    
    // 活动时长筛选
    if (filterForm.duration && project.duration !== filterForm.duration) {
      match = false
    }
    
    // 场地类型筛选
    if (filterForm.venue && project.venue !== filterForm.venue) {
      match = false
    }
    
    // 关键词搜索
    if (filterForm.keyword) {
      const keyword = filterForm.keyword.toLowerCase()
      const matchFields = [
        project.title,
        project.description,
        project.location
      ].map(field => field.toLowerCase())
      
      if (!matchFields.some(field => field.includes(keyword))) {
        match = false
      }
    }
    
    return match
  })
  
  // 更新总数
  total.value = filteredProjects.length
  
  // 计算当前页的数据
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  projects.value = filteredProjects.slice(start, end)
  
  // 如果当前页没有数据且不是第一页，回到第一页
  if (projects.value.length === 0 && currentPage.value > 1) {
    currentPage.value = 1
    loadProjects()
  }
}

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1 // 重置到第一页
  loadProjects()
}

// 重置筛选
const resetFilter = () => {
  Object.keys(filterForm).forEach(key => {
    filterForm[key] = ''
  })
  currentPage.value = 1 // 重置到第一页
  loadProjects()
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1 // 重置到第一页
  loadProjects()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadProjects()
}

// 初始化
onMounted(() => {
  loadProjects()
})

// 咨询处理
const handleConsult = (project) => {
  router.push({
    path: '/registration',
    query: { projectId: project.id }
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

// 修改点击处理函数
const handleProjectClick = (project) => {
  router.push({
    name: 'teamBuildingDetail',
    params: { id: project.id }
  })
}
</script>

<style lang="scss" scoped>
.team-building {
  padding: 20px;

  .filter-card {
    margin-bottom: 20px;

    .filter-form {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
    }
  }

  .project-list {
    min-height: 400px;

    :deep(.el-empty) {
      padding: 60px 0;
    }

    .project-card {
      margin-bottom: 20px;
      transition: transform 0.3s;

      &:hover {
        transform: translateY(-5px);
      }

      .project-image {
        position: relative;
        height: 200px;

        .el-image {
          width: 100%;
          height: 100%;
        }

        .project-tags {
          position: absolute;
          top: 10px;
          right: 10px;
        }
      }

      .project-info {
        padding: 15px;

        .project-title {
          margin: 0 0 10px;
          font-size: 18px;
          font-weight: bold;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .project-desc {
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

        .project-meta {
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

        .project-features {
          margin-bottom: 15px;
          display: flex;
          flex-wrap: wrap;
          gap: 8px;
        }

        .project-footer {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .price {
            .amount {
              color: #f56c6c;
              font-size: 20px;
              font-weight: bold;
            }

            .unit {
              color: #909399;
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