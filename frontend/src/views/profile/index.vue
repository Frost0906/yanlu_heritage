<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <!-- 左侧个人信息卡片 -->
      <el-col :span="6">
        <el-card class="profile-card">
          <div class="avatar-container">
            <el-avatar :size="100" :src="userInfo.avatar || defaultAvatar" />
            <h3>{{ userInfo.username }}</h3>
            <p>{{ userInfo.bio || '这个用户很懒，还没有填写个人简介' }}</p>
          </div>
          <el-divider />
          <div class="user-stats">
            <div class="stat-item">
              <h4>{{ userInfo.coursesCount || 0 }}</h4>
              <span>已报名课程</span>
            </div>
            <div class="stat-item">
              <h4>{{ userInfo.commentsCount || 0 }}</h4>
              <span>评论数</span>
            </div>
            <div class="stat-item">
              <h4>{{ userInfo.likesCount || 0 }}</h4>
              <span>获赞数</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧内容区 -->
      <el-col :span="18">
        <el-card>
          <template #header>
            <div class="card-header">
              <el-tabs v-model="activeTab">
                <el-tab-pane label="我的课程" name="courses">
                  <div v-if="myCourses.length > 0" class="courses-list">
                    <el-card v-for="course in myCourses" :key="course.id" class="course-item">
                      <div class="course-info">
                        <img :src="course.image" :alt="course.title">
                        <div class="course-detail">
                          <h4>{{ course.title }}</h4>
                          <p>{{ course.description }}</p>
                          <div class="course-meta">
                            <span>报名时间：{{ course.enrollTime }}</span>
                            <span>状态：{{ course.status }}</span>
                          </div>
                        </div>
                      </div>
                    </el-card>
                  </div>
                  <el-empty v-else description="暂无报名课程" />
                </el-tab-pane>
                
                <el-tab-pane label="我的评论" name="comments">
                  <div v-if="myComments.length > 0" class="comments-list">
                    <el-card v-for="comment in myComments" :key="comment.id" class="comment-item">
                      <div class="comment-content">
                        <p>{{ comment.content }}</p>
                        <div class="comment-meta">
                          <span>{{ comment.createTime }}</span>
                          <span>{{ comment.courseName }}</span>
                        </div>
                      </div>
                    </el-card>
                  </div>
                  <el-empty v-else description="暂无评论" />
                </el-tab-pane>

                <el-tab-pane label="账号设置" name="settings">
                  <el-form :model="userForm" :rules="rules" ref="userFormRef" label-width="100px">
                    <el-form-item label="用户名" prop="username">
                      <el-input v-model="userForm.username" />
                    </el-form-item>
                    <el-form-item label="个人简介" prop="bio">
                      <el-input type="textarea" v-model="userForm.bio" rows="4" />
                    </el-form-item>
                    <el-form-item label="修改密码" prop="newPassword">
                      <el-input type="password" v-model="userForm.newPassword" show-password />
                    </el-form-item>
                    <el-form-item label="确认密码" prop="confirmPassword">
                      <el-input type="password" v-model="userForm.confirmPassword" show-password />
                    </el-form-item>
                    <el-form-item>
                      <el-button type="primary" @click="handleUpdateProfile">保存修改</el-button>
                    </el-form-item>
                  </el-form>
                </el-tab-pane>
              </el-tabs>
            </div>
          </template>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '../../stores/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

const activeTab = ref('courses')
const userFormRef = ref(null)

// 用户信息
const userInfo = reactive({
  username: '示例用户',
  avatar: '',
  bio: '热爱学习，热爱生活',
  coursesCount: 2,
  commentsCount: 5,
  likesCount: 10
})

// 我的课程列表
const myCourses = ref([
  {
    id: 1,
    title: '自然探索之旅',
    description: '探索大自然的奥秘，培养科学探究精神',
    image: 'https://picsum.photos/400/300?random=3',
    enrollTime: '2024-01-15',
    status: '进行中'
  }
])

// 我的评论列表
const myComments = ref([
  {
    id: 1,
    content: '课程很有趣，收获很多！',
    createTime: '2024-01-16 14:30',
    courseName: '自然探索之旅'
  }
])

// 表单数据
const userForm = reactive({
  username: '',
  bio: '',
  newPassword: '',
  confirmPassword: ''
})

// 表单验证规则
const rules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  bio: [
    { max: 200, message: '个人简介不能超过200个字符', trigger: 'blur' }
  ],
  newPassword: [
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { validator: (rule, value, callback) => {
      if (value !== userForm.newPassword) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }, trigger: 'blur' }
  ]
})

// 更新个人信息
const handleUpdateProfile = async () => {
  if (!userFormRef.value) return
  
  await userFormRef.value.validate(async (valid, fields) => {
    if (valid) {
      try {
        // TODO: 调用接口更新用户信息
        ElMessage.success('个人信息更新成功')
      } catch (error) {
        console.error(error)
        ElMessage.error('更新失败，请稍后重试')
      }
    }
  })
}

// 组件挂载时获取用户信息
onMounted(() => {
  // TODO: 获取用户信息
  userForm.username = userInfo.username
  userForm.bio = userInfo.bio
})
</script>

<style lang="scss" scoped>
.profile-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;

  .profile-card {
    .avatar-container {
      text-align: center;
      padding: 20px 0;

      h3 {
        margin: 10px 0 5px;
      }

      p {
        color: #666;
        font-size: 14px;
        margin: 0;
      }
    }

    .user-stats {
      display: flex;
      justify-content: space-around;
      text-align: center;

      .stat-item {
        h4 {
          margin: 0;
          font-size: 20px;
          color: var(--el-color-primary);
        }

        span {
          font-size: 12px;
          color: #666;
        }
      }
    }
  }

  .courses-list,
  .comments-list {
    .course-item,
    .comment-item {
      margin-bottom: 15px;

      .course-info {
        display: flex;
        gap: 15px;

        img {
          width: 120px;
          height: 80px;
          object-fit: cover;
          border-radius: 4px;
        }

        .course-detail {
          flex: 1;

          h4 {
            margin: 0 0 10px;
          }

          p {
            color: #666;
            margin: 0 0 10px;
          }

          .course-meta {
            display: flex;
            gap: 20px;
            color: #999;
            font-size: 12px;
          }
        }
      }
    }

    .comment-item {
      .comment-content {
        p {
          margin: 0 0 10px;
        }

        .comment-meta {
          display: flex;
          gap: 20px;
          color: #999;
          font-size: 12px;
        }
      }
    }
  }
}
</style> 