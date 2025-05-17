<template>
  <div class="home-container">
    <!-- 轮播图 -->
    <el-carousel height="500px" class="banner">
      <el-carousel-item v-for="item in bannerList" :key="item.id">
        <img :src="item.image" :alt="item.title">
        <div class="carousel-content">
          <h2>{{ item.title }}</h2>
          <p>{{ item.description }}</p>
        </div>
      </el-carousel-item>
    </el-carousel>

    <!-- 功能区块 -->
    <div class="features">
      <el-row :gutter="20">
        <el-col :span="8" v-for="feature in features" :key="feature.title">
          <el-card class="feature-card" shadow="hover">
            <el-icon :size="40" class="feature-icon">
              <component :is="feature.icon" />
            </el-icon>
            <h3>{{ feature.title }}</h3>
            <p>{{ feature.description }}</p>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 热门课程 -->
    <div class="section">
      <div class="section-header">
        <h2>热门研学课程</h2>
        <router-link to="/courses">
          <el-button type="primary" text>查看更多</el-button>
        </router-link>
      </div>
      <el-row :gutter="20">
        <el-col :span="6" v-for="course in popularCourses" :key="course.id">
          <el-card class="course-card" shadow="hover" @click="router.push(`/courses/${course.id}`)">
            <img :src="course.image" class="course-image">
            <h4>{{ course.title }}</h4>
            <p>{{ course.description }}</p>
            <div class="card-footer">
              <span class="price">¥{{ course.price }}</span>
              <el-button type="primary" size="small">立即报名</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 团建项目 -->
    <div class="section">
      <div class="section-header">
        <h2>精选团建项目</h2>
        <router-link to="/team-building">
          <el-button type="primary" text>查看更多</el-button>
        </router-link>
      </div>
      <el-row :gutter="20">
        <el-col :span="6" v-for="project in teamProjects" :key="project.id">
          <el-card class="project-card" shadow="hover" @click="router.push(`/team-building/${project.id}`)">
            <img :src="project.image" class="project-image">
            <h4>{{ project.title }}</h4>
            <p>{{ project.description }}</p>
            <div class="card-footer">
              <span class="price">¥{{ project.price }}/人起</span>
              <el-button type="primary" size="small">了解详情</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  School,
  UserFilled,
  Calendar,
  Location,
  ChatDotRound,
  SetUp
} from '@element-plus/icons-vue'

const router = useRouter()

// 轮播图数据
const bannerList = ref([
  {
    id: 1,
    image: 'https://picsum.photos/1920/500?random=1',
    title: '探索自然 收获知识',
    description: '让孩子在实践中学习，在体验中成长'
  },
  {
    id: 2,
    image: 'https://picsum.photos/1920/500?random=2',
    title: '团队建设 凝聚力量',
    description: '专业的团建活动，助力企业文化建设'
  }
])

// 功能特色
const features = ref([
  {
    icon: School,
    title: '优质研学课程',
    description: '精心设计的课程体系，让学习更有趣'
  },
  {
    icon: UserFilled,
    title: '专业团建方案',
    description: '个性化定制，满足企业多样化需求'
  },
  {
    icon: Calendar,
    title: '便捷报名系统',
    description: '在线报名，快速确认，省时省力'
  }
])

// 热门课程
const popularCourses = ref([
  {
    id: 1,
    image: 'https://picsum.photos/400/300?random=3',
    title: '自然探索之旅',
    description: '探索大自然的奥秘，培养科学探究精神',
    price: 299
  },
  {
    id: 2,
    image: 'https://picsum.photos/400/300?random=4',
    title: '历史文化寻踪',
    description: '感受历史文化魅力，传承民族文化基因',
    price: 399
  },
  {
    id: 3,
    image: 'https://picsum.photos/400/300?random=5',
    title: '科技创新营',
    description: '激发创新思维，培养科技兴趣',
    price: 499
  },
  {
    id: 4,
    image: 'https://picsum.photos/400/300?random=6',
    title: '艺术体验课程',
    description: '培养艺术素养，提升审美能力',
    price: 299
  }
])

// 团建项目
const teamProjects = ref([
  {
    id: 1,
    image: 'https://picsum.photos/400/300?random=7',
    title: '户外拓展训练',
    description: '挑战自我，突破极限',
    price: 199
  },
  {
    id: 2,
    image: 'https://picsum.photos/400/300?random=8',
    title: '团队协作工坊',
    description: '增进默契，提升凝聚力',
    price: 299
  },
  {
    id: 3,
    image: 'https://picsum.photos/400/300?random=9',
    title: '创意团建活动',
    description: '激发创意，培养团队精神',
    price: 259
  },
  {
    id: 4,
    image: 'https://picsum.photos/400/300?random=10',
    title: '文化主题团建',
    description: '传承文化，凝聚团队',
    price: 399
  }
])
</script>

<style lang="scss" scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;

  .banner {
    margin-bottom: 40px;
    border-radius: 8px;
    overflow: hidden;

    .el-carousel-item {
      position: relative;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      .carousel-content {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        padding: 20px;
        background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
        color: #fff;

        h2 {
          margin: 0 0 10px;
          font-size: 24px;
        }

        p {
          margin: 0;
          font-size: 16px;
        }
      }
    }
  }

  .features {
    margin-bottom: 40px;

    .feature-card {
      text-align: center;
      padding: 20px;

      .feature-icon {
        color: var(--el-color-primary);
        margin-bottom: 15px;
      }

      h3 {
        margin: 10px 0;
        font-size: 18px;
      }

      p {
        color: #666;
        margin: 0;
      }
    }
  }

  .section {
    margin-bottom: 40px;

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      h2 {
        margin: 0;
        font-size: 24px;
      }
    }

    .course-card,
    .project-card {
      height: 100%;

      .course-image,
      .project-image {
        width: 100%;
        height: 200px;
        object-fit: cover;
        border-radius: 4px;
      }

      h4 {
        margin: 15px 0 10px;
        font-size: 16px;
      }

      p {
        color: #666;
        margin: 0 0 15px;
        height: 40px;
        overflow: hidden;
      }

      .card-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .price {
          color: var(--el-color-danger);
          font-size: 18px;
          font-weight: bold;
        }
      }
    }
  }
}
</style> 