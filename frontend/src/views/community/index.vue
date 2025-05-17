<template>
  <div class="community">
    <el-row :gutter="20">
      <!-- 左侧内容区 -->
      <el-col :span="16">
        <!-- 发布框 -->
        <el-card class="post-editor">
          <el-input
            v-model="postContent"
            type="textarea"
            :rows="4"
            placeholder="分享你的研学心得..."
          />
          <div class="editor-footer">
            <div class="upload-buttons">
              <el-upload
                ref="uploadRef"
                action="#"
                list-type="picture-card"
                :auto-upload="false"
                :on-change="handleImageChange"
                :limit="9"
              >
                <el-icon><Plus /></el-icon>
                <template #tip>
                  <div class="upload-tip">支持jpg/png文件</div>
                </template>
              </el-upload>
            </div>
            <el-button type="primary" @click="handlePost">发布</el-button>
          </div>
        </el-card>

        <!-- 帖子列表 -->
        <div class="post-list">
          <el-card v-for="post in posts" :key="post.id" class="post-card">
            <!-- 帖子头部 -->
            <div class="post-header">
              <div class="user-info">
                <el-avatar :src="post.avatar" />
                <div class="user-meta">
                  <span class="username">{{ post.username }}</span>
                  <span class="time">{{ post.time }}</span>
                </div>
              </div>
              <el-dropdown v-if="post.isOwner">
                <el-button link>
                  <el-icon><MoreFilled /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="handleEdit(post)">编辑</el-dropdown-item>
                    <el-dropdown-item @click="handleDelete(post)" divided>删除</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>

            <!-- 帖子内容 -->
            <div class="post-content">
              <p>{{ post.content }}</p>
              <!-- 图片展示 -->
              <div class="image-grid" v-if="post.images && post.images.length">
                <el-image
                  v-for="(image, index) in post.images"
                  :key="index"
                  :src="image"
                  :preview-src-list="post.images"
                  fit="cover"
                />
              </div>
              <!-- 研学课程标签 -->
              <div class="course-tag" v-if="post.course">
                <el-tag>{{ post.course }}</el-tag>
              </div>
            </div>

            <!-- 互动区域 -->
            <div class="post-actions">
              <div class="action-buttons">
                <el-button link :class="{ active: post.isLiked }" @click="handleLike(post)">
                  <el-icon><Star /></el-icon>
                  {{ post.likes }} 赞
                </el-button>
                <el-button link @click="handleComment(post)">
                  <el-icon><ChatDotRound /></el-icon>
                  {{ post.comments.length }} 评论
                </el-button>
                <el-button link @click="handleShare(post)">
                  <el-icon><Share /></el-icon>
                  分享
                </el-button>
              </div>
            </div>

            <!-- 评论区 -->
            <div class="comments-section" v-if="post.showComments">
              <el-divider />
              <div class="comment-list">
                <div v-for="comment in post.comments" :key="comment.id" class="comment-item">
                  <el-avatar :size="32" :src="comment.avatar" />
                  <div class="comment-content">
                    <div class="comment-header">
                      <span class="username">{{ comment.username }}</span>
                      <span class="time">{{ comment.time }}</span>
                    </div>
                    <p>{{ comment.content }}</p>
                  </div>
                </div>
              </div>
              <!-- 评论输入框 -->
              <div class="comment-input">
                <el-input
                  v-model="commentContent"
                  placeholder="写下你的评论..."
                  @keyup.enter="submitComment(post)"
                >
                  <template #append>
                    <el-button @click="submitComment(post)">发送</el-button>
                  </template>
                </el-input>
              </div>
            </div>
          </el-card>
        </div>

        <!-- 加载更多 -->
        <div class="load-more">
          <el-button link @click="loadMore">加载更多</el-button>
        </div>
      </el-col>

      <!-- 右侧边栏 -->
      <el-col :span="8">
        <!-- 热门话题 -->
        <el-card class="hot-topics">
          <template #header>
            <div class="card-header">
              <h3>热门话题</h3>
            </div>
          </template>
          <el-tag
            v-for="topic in hotTopics"
            :key="topic.id"
            class="topic-tag"
            @click="handleTopicClick(topic)"
          >
            # {{ topic.name }}
          </el-tag>
        </el-card>

        <!-- 优秀成果展示 -->
        <el-card class="featured-works">
          <template #header>
            <div class="card-header">
              <h3>优秀成果展示</h3>
            </div>
          </template>
          <div class="featured-list">
            <div
              v-for="item in featuredList"
              :key="item.id"
              class="featured-item"
              @click="handleFeaturedClick(item)"
            >
              <el-image :src="item.cover" class="featured-image" />
              <div class="featured-info">
                <h4>{{ item.title }}</h4>
                <div class="meta">
                  <span class="author">{{ item.author }}</span>
                  <span class="views">{{ item.views }}浏览</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { View, Star } from '@element-plus/icons-vue'

const router = useRouter()

// 发帖相关
const postContent = ref('')
const imageList = ref([])
const uploadRef = ref(null)

// 评论相关
const commentContent = ref('')

// 模拟数据
const posts = ref([
  {
    id: 1,
    username: '小明',
    avatar: 'https://example.com/avatar1.jpg',
    time: '10分钟前',
    content: '今天的地质考察太有趣了！看到了很多奇特的岩石构造，学到了很多知识。',
    images: [
      'https://example.com/image1.jpg',
      'https://example.com/image2.jpg'
    ],
    course: '地质探索之旅',
    likes: 23,
    isLiked: false,
    isOwner: true,
    showComments: false,
    comments: [
      {
        id: 1,
        username: '小红',
        avatar: 'https://example.com/avatar2.jpg',
        time: '5分钟前',
        content: '确实很精彩，我也学到了很多！'
      }
    ]
  }
])

const hotTopics = ref([
  { id: 1, name: '地质探索' },
  { id: 2, name: '文化之旅' },
  { id: 3, name: '科技创新' },
  { id: 4, name: '艺术体验' }
])

// 优秀成果数据
const featuredList = ref([
  {
    id: 1,
    title: '探索恐龙化石的奥秘',
    cover: 'https://example.com/featured1.jpg',
    author: '张老师',
    views: 1234
  },
  {
    id: 2,
    title: '传统文化的魅力',
    cover: 'https://example.com/featured2.jpg',
    author: '李同学',
    views: 986
  }
])

// 处理图片上传
const handleImageChange = (file) => {
  // 将文件转换为URL以便预览
  const reader = new FileReader()
  reader.onload = (e) => {
    // 将图片URL添加到图片列表中
    imageList.value.push(e.target.result)
  }
  reader.readAsDataURL(file.raw)
}

// 发布帖子
const handlePost = () => {
  if (!postContent.value.trim()) {
    ElMessage.warning('请输入内容')
    return
  }
  
  // 创建新帖子
  const newPost = {
    id: Date.now(),
    username: '当前用户',
    avatar: 'https://example.com/current-user.jpg',
    time: '刚刚',
    content: postContent.value,
    images: [...imageList.value],
    likes: 0,
    isLiked: false,
    isOwner: true,
    showComments: false,
    comments: []
  }

  // 将新帖子添加到列表开头
  posts.value.unshift(newPost)

  // 清空输入
  postContent.value = ''
  imageList.value = []
  // 清除上传组件中的文件
  if (uploadRef.value) {
    uploadRef.value.clearFiles()
  }
  ElMessage.success('发布成功')
}

// 点赞
const handleLike = (post) => {
  post.isLiked = !post.isLiked
  post.likes += post.isLiked ? 1 : -1
}

// 评论
const handleComment = (post) => {
  post.showComments = !post.showComments
}

// 分享
const handleShare = (post) => {
  ElMessage.success('分享功能开发中')
}

// 提交评论
const submitComment = (post) => {
  if (!commentContent.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  // 添加新评论
  post.comments.push({
    id: Date.now(),
    username: '当前用户',
    avatar: 'https://example.com/current-user.jpg',
    time: '刚刚',
    content: commentContent.value
  })
  // 确保评论区显示
  post.showComments = true
  // 清空评论输入框
  commentContent.value = ''
  ElMessage.success('评论成功')
}

// 编辑帖子
const handleEdit = (post) => {
  // TODO: 实现编辑逻辑
  ElMessage.info('编辑功能开发中')
}

// 删除帖子
const handleDelete = (post) => {
  // TODO: 实现删除逻辑
  ElMessage.success('删除成功')
  posts.value = posts.value.filter(p => p.id !== post.id)
}

// 加载更多
const loadMore = () => {
  // TODO: 实现加载更多逻辑
  ElMessage.info('加载更多功能开发中')
}

// 点击话题
const handleTopicClick = (topic) => {
  // TODO: 实现话题筛选逻辑
  ElMessage.info(`查看话题：${topic.name}`)
}

// 处理优秀成果点击
const handleFeaturedClick = (item) => {
  try {
    router.push({
      name: 'featuredDetail',
      params: { id: item.id.toString() }
    })
  } catch (error) {
    console.error('跳转失败：', error)
    ElMessage.error('页面跳转失败')
  }
}
</script>

<style lang="scss" scoped>
.community {
  padding: 20px;

  .post-editor {
    margin-bottom: 20px;

    .editor-footer {
      margin-top: 20px;
      display: flex;
      justify-content: space-between;
      align-items: center;

      .upload-buttons {
        :deep(.el-upload--picture-card) {
          width: 80px;
          height: 80px;
          line-height: 84px;
        }

        .upload-tip {
          font-size: 12px;
          color: #909399;
          margin-top: 5px;
        }
      }
    }
  }

  .post-card {
    margin-bottom: 20px;

    .post-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15px;

      .user-info {
        display: flex;
        align-items: center;
        gap: 10px;

        .user-meta {
          display: flex;
          flex-direction: column;

          .username {
            font-weight: bold;
          }

          .time {
            font-size: 12px;
            color: #909399;
          }
        }
      }
    }

    .post-content {
      margin-bottom: 15px;

      p {
        margin: 0 0 15px;
        line-height: 1.6;
      }

      .image-grid {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 10px;
        margin-bottom: 15px;

        .el-image {
          width: 100%;
          height: 200px;
          border-radius: 4px;
        }
      }

      .course-tag {
        margin-top: 10px;
      }
    }

    .post-actions {
      .action-buttons {
        display: flex;
        gap: 20px;

        .el-button {
          display: flex;
          align-items: center;
          gap: 5px;

          &.active {
            color: var(--el-color-primary);
          }
        }
      }
    }

    .comments-section {
      .comment-list {
        margin-bottom: 15px;

        .comment-item {
          display: flex;
          gap: 10px;
          margin-bottom: 15px;

          .comment-content {
            flex: 1;

            .comment-header {
              margin-bottom: 5px;

              .username {
                font-weight: bold;
                margin-right: 10px;
              }

              .time {
                font-size: 12px;
                color: #909399;
              }
            }

            p {
              margin: 0;
              color: #606266;
            }
          }
        }
      }
    }
  }

  .load-more {
    text-align: center;
    margin: 20px 0;
  }

  .hot-topics {
    margin-bottom: 20px;

    .card-header {
      h3 {
        margin: 0;
      }
    }

    .topic-tag {
      margin: 0 10px 10px 0;
      cursor: pointer;
    }
  }

  .featured-works {
    margin-top: 20px;

    .card-header {
      h3 {
        margin: 0;
        font-size: 16px;
        font-weight: bold;
      }
    }

    .featured-list {
      .featured-item {
        display: flex;
        padding: 12px 0;
        border-bottom: 1px solid #ebeef5;
        cursor: pointer;
        transition: all 0.3s;

        &:last-child {
          border-bottom: none;
        }

        &:hover {
          background-color: #f5f7fa;
        }

        .featured-image {
          width: 80px;
          height: 60px;
          border-radius: 4px;
          margin-right: 12px;
        }

        .featured-info {
          flex: 1;
          display: flex;
          flex-direction: column;
          justify-content: space-between;

          h4 {
            margin: 0;
            font-size: 14px;
            line-height: 1.4;
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            display: box;
            -webkit-line-clamp: 1;
            line-clamp: 1;
            -webkit-box-orient: vertical;
            box-orient: vertical;
          }

          .meta {
            display: flex;
            justify-content: space-between;
            font-size: 12px;
            color: #909399;

            .author {
              color: #606266;
            }
          }
        }
      }
    }
  }
}
</style> 