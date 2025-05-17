<template>
  <div class="featured-detail">
    <el-card class="detail-card">
      <!-- 文章头部 -->
      <div class="article-header">
        <h1 class="title">{{ article.title }}</h1>
        <div class="meta">
          <div class="author-info">
            <el-avatar :size="40" :src="article.authorAvatar" />
            <div class="author-meta">
              <span class="author-name">{{ article.author }}</span>
              <span class="publish-time">{{ article.publishTime }}</span>
            </div>
          </div>
          <div class="stats">
            <span class="views">
              <el-icon><View /></el-icon>
              {{ article.views }} 次浏览
            </span>
            <span class="likes">
              <el-icon><Star /></el-icon>
              {{ article.likes }} 赞
            </span>
          </div>
        </div>
        <!-- 标签展示 -->
        <div class="tags" v-if="article.tags">
          <el-tag
            v-for="tag in article.tags"
            :key="tag"
            size="small"
            effect="plain"
            class="tag-item"
          >
            {{ tag }}
          </el-tag>
        </div>
      </div>

      <!-- 文章内容 -->
      <div class="article-content">
        <!-- 封面图 -->
        <div class="cover-image">
          <el-image :src="article.cover" fit="cover" />
        </div>

        <!-- 摘要 -->
        <div class="summary">
          <el-alert
            :title="article.summary"
            type="info"
            :closable="false"
          />
        </div>

        <!-- 正文内容 -->
        <div class="content-body" v-html="article.content"></div>

        <!-- 图片展示 -->
        <div class="image-gallery" v-if="article.images && article.images.length">
          <h3>活动照片</h3>
          <el-row :gutter="20">
            <el-col 
              :span="12" 
              v-for="(image, index) in article.images" 
              :key="index"
              class="image-item"
            >
              <el-image
                :src="image"
                fit="cover"
                :preview-src-list="article.images"
                :initial-index="index"
              />
            </el-col>
          </el-row>
        </div>

        <!-- 相关课程 -->
        <div class="related-course" v-if="article.relatedCourse">
          <h3>相关课程</h3>
          <el-card class="course-card" shadow="hover">
            <div class="course-info">
              <el-image :src="article.relatedCourse.image" class="course-image" />
              <div class="course-meta">
                <h4>{{ article.relatedCourse.title }}</h4>
                <p>{{ article.relatedCourse.description }}</p>
                <el-button type="primary" size="small" @click="viewCourse(article.relatedCourse.id)">
                  查看课程
                </el-button>
              </div>
            </div>
          </el-card>
        </div>
      </div>

      <!-- 互动区域 -->
      <div class="article-actions">
        <el-button :class="{ active: isLiked }" @click="handleLike">
          <el-icon><Star /></el-icon>
          点赞 {{ article.likes }}
        </el-button>
        <el-button @click="handleShare">
          <el-icon><Share /></el-icon>
          分享
        </el-button>
      </div>

      <!-- 评论区 -->
      <div class="comments-section">
        <h3>评论 ({{ article.comments.length }})</h3>
        
        <!-- 评论输入框 -->
        <div class="comment-input">
          <el-input
            v-model="commentContent"
            type="textarea"
            :rows="3"
            placeholder="写下你的评论..."
          />
          <div class="comment-tools">
            <el-button type="primary" @click="submitComment">发表评论</el-button>
          </div>
        </div>

        <!-- 评论列表 -->
        <div class="comment-list">
          <div v-if="article.comments.length === 0" class="no-comments">
            暂无评论，快来发表第一条评论吧！
          </div>
          <div v-else v-for="comment in article.comments" :key="comment.id" class="comment-item">
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
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { View, Star, Share } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

// 文章数据
const article = ref({})  // 初始化为空对象

// 模拟文章数据
const articleData = {
  1: {
    id: 1,
    title: '探索恐龙化石的奥秘',
    author: '张老师',
    authorAvatar: 'https://example.com/avatar1.jpg',
    publishTime: '2024-03-20',
    views: 1234,
    likes: 88,
    cover: 'https://example.com/featured1.jpg',
    summary: '在为期一周的地质公园研学活动中，我们发现了许多珍贵的化石，这次探索之旅让同学们收获颇丰。',
    content: `
      <h3>探索过程</h3>
      <p>在专业地质专家的指导下，我们来到了化石发掘现场。同学们认真学习了化石发掘的基本技能，包括如何正确使用工具、如何识别可能存在化石的区域等。</p>
      
      <h3>重要发现</h3>
      <p>在发掘过程中，我们发现了几块保存完好的三叶虫化石，这些化石可以追溯到古生代。通过这些化石，同学们直观地了解到了地球的演化历史。</p>
      
      <h3>学习收获</h3>
      <p>1. 掌握了基本的化石发掘技能</p>
      <p>2. 学习了地层年代学的基础知识</p>
      <p>3. 了解了古生物学的研究方法</p>
      <p>4. 培养了科学探究精神</p>
    `,
    images: [
      'https://example.com/fossil1.jpg',
      'https://example.com/fossil2.jpg',
      'https://example.com/fossil3.jpg',
      'https://example.com/fossil4.jpg'
    ],
    relatedCourse: {
      id: 1,
      title: '地质探索之旅',
      description: '带你走进神奇的地质世界，探索地球的奥秘',
      image: 'https://example.com/course1.jpg'
    },
    tags: ['地质研学', '化石发掘', '科学探索'],
    comments: []
  },
  2: {
    id: 2,
    title: '传统文化的魅力',
    author: '李同学',
    authorAvatar: 'https://example.com/avatar2.jpg',
    publishTime: '2024-03-19',
    views: 986,
    likes: 45,
    cover: 'https://example.com/featured2.jpg',
    summary: '走进古城，感受历史的气息，探索传统文化的魅力。通过实地考察和体验，我们深入了解了中国传统文化的精髓。',
    content: `
      <h3>文化之旅</h3>
      <p>我们参观了古城的多处历史遗迹，包括古城墙、鼓楼、历史博物馆等。每一处建筑都诉说着悠久的历史故事。</p>
      
      <h3>传统艺术体验</h3>
      <p>在当地艺术家的指导下，我们体验了剪纸、书法、陶艺等传统工艺。通过亲手制作，更深入地理解了传统艺术的魅力。</p>
      
      <h3>文化传承</h3>
      <p>1. 学习了传统工艺制作技巧</p>
      <p>2. 了解了非物质文化遗产的重要性</p>
      <p>3. 感受到了传统文化的生命力</p>
      <p>4. 增强了文化自信和认同感</p>
    `,
    images: [
      'https://example.com/culture1.jpg',
      'https://example.com/culture2.jpg',
      'https://example.com/culture3.jpg',
      'https://example.com/culture4.jpg'
    ],
    relatedCourse: {
      id: 2,
      title: '文化寻根之旅',
      description: '探索中华传统文化，传承历史文明',
      image: 'https://example.com/course2.jpg'
    },
    tags: ['文化研学', '传统艺术', '非遗传承'],
    comments: []
  }
}

const isLiked = ref(false)
const commentContent = ref('')

// 获取文章详情
const fetchArticleDetail = async (id) => {
  try {
    console.log('获取文章ID：', id)
    // 模拟API调用
    const articleInfo = articleData[id]
    if (articleInfo) {
      article.value = articleInfo
    } else {
      ElMessage.error('文章不存在')
      router.push('/community')
    }
  } catch (error) {
    console.error('获取文章详情失败：', error)
    ElMessage.error('获取文章详情失败')
    router.push('/community')
  }
}

// 点赞处理
const handleLike = () => {
  isLiked.value = !isLiked.value
  article.value.likes += isLiked.value ? 1 : -1
  ElMessage.success(isLiked.value ? '点赞成功' : '已取消点赞')
}

// 分享处理
const handleShare = () => {
  ElMessage.success('分享功能开发中')
}

// 提交评论
const submitComment = () => {
  if (!commentContent.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  article.value.comments.push({
    id: Date.now(),
    username: '当前用户',
    avatar: 'https://example.com/current-user.jpg',
    time: '刚刚',
    content: commentContent.value
  })

  commentContent.value = ''
  ElMessage.success('评论成功')
}

// 查看相关课程
const viewCourse = (courseId) => {
  router.push({
    name: 'courseDetail',
    params: { id: courseId }
  })
}

onMounted(() => {
  try {
    const articleId = parseInt(route.params.id)
    console.log('路由参数：', route.params)
    if (articleId) {
      fetchArticleDetail(articleId)
    } else {
      ElMessage.error('无效的文章ID')
      router.push('/community')
    }
  } catch (error) {
    console.error('组件加载错误：', error)
    ElMessage.error('页面加载失败')
    router.push('/community')
  }
})
</script>

<style lang="scss" scoped>
.featured-detail {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;

  .detail-card {
    .article-header {
      margin-bottom: 30px;

      .title {
        margin: 0 0 20px;
        font-size: 28px;
        font-weight: bold;
        line-height: 1.4;
      }

      .meta {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;

        .author-info {
          display: flex;
          align-items: center;
          gap: 15px;

          .author-meta {
            display: flex;
            flex-direction: column;

            .author-name {
              font-weight: bold;
              font-size: 16px;
            }

            .publish-time {
              font-size: 14px;
              color: #909399;
            }
          }
        }

        .stats {
          display: flex;
          gap: 20px;
          color: #606266;

          .views, .likes {
            display: flex;
            align-items: center;
            gap: 5px;
          }
        }
      }

      .tags {
        margin-top: 15px;
        
        .tag-item {
          margin-right: 10px;
        }
      }
    }

    .article-content {
      .cover-image {
        margin-bottom: 30px;

        .el-image {
          width: 100%;
          max-height: 400px;
          border-radius: 8px;
        }
      }

      .summary {
        margin-bottom: 30px;
      }

      .content-body {
        margin-bottom: 30px;
        line-height: 1.8;
        color: #303133;

        :deep(h3) {
          margin: 25px 0 15px;
          font-size: 20px;
          font-weight: bold;
        }

        :deep(p) {
          margin: 15px 0;
          font-size: 16px;
        }
      }

      .image-gallery {
        margin: 30px 0;

        h3 {
          margin-bottom: 20px;
          font-size: 20px;
          font-weight: bold;
        }

        .image-item {
          margin-bottom: 20px;

          .el-image {
            width: 100%;
            height: 250px;
            border-radius: 8px;
            cursor: pointer;
          }
        }
      }

      .related-course {
        margin-top: 40px;

        h3 {
          margin-bottom: 20px;
          font-size: 20px;
          font-weight: bold;
        }

        .course-card {
          .course-info {
            display: flex;
            gap: 20px;

            .course-image {
              width: 200px;
              height: 120px;
              border-radius: 4px;
            }

            .course-meta {
              flex: 1;

              h4 {
                margin: 0 0 10px;
                font-size: 18px;
                font-weight: bold;
              }

              p {
                margin: 0 0 15px;
                color: #606266;
                line-height: 1.6;
              }
            }
          }
        }
      }
    }

    .article-actions {
      display: flex;
      gap: 20px;
      margin: 30px 0;
      justify-content: center;

      .el-button {
        padding: 12px 30px;
        display: flex;
        align-items: center;
        gap: 5px;

        &.active {
          color: var(--el-color-primary);
        }
      }
    }

    .comments-section {
      margin-top: 40px;

      h3 {
        margin-bottom: 20px;
        font-size: 20px;
        font-weight: bold;
      }

      .comment-input {
        margin-bottom: 30px;

        .comment-tools {
          margin-top: 15px;
          display: flex;
          justify-content: flex-end;
        }
      }

      .comment-list {
        .no-comments {
          text-align: center;
          color: #909399;
          padding: 30px 0;
        }

        .comment-item {
          display: flex;
          gap: 15px;
          margin-bottom: 20px;
          padding-bottom: 20px;
          border-bottom: 1px solid #EBEEF5;

          &:last-child {
            border-bottom: none;
          }

          .comment-content {
            flex: 1;

            .comment-header {
              margin-bottom: 8px;

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
              color: #303133;
              line-height: 1.6;
            }
          }
        }
      }
    }
  }
}
</style> 