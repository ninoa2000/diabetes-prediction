<template>
  <div class="not-found-container text-center">
    <div class="error-content">
      <div class="error-code">404</div>
      <div class="error-title">页面未找到</div>
      <p class="error-desc">您访问的页面不存在或已被移除</p>
      <el-button type="primary" @click="$router.push('/')">
        返回首页
      </el-button>
    </div>
    <div class="error-image">
      <img 
        src="https://element-plus.org/images/element-plus-logo.svg"
        alt="404"
      />
    </div>
  </div>
</template>

<script>
export default {
  name: 'NotFound'
}
</script>

<style scoped>
.not-found-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.error-content {
  margin-bottom: 40px;
}

.error-code {
  font-size: 80px;
  font-weight: 700;
  color: var(--el-color-primary);
  margin-bottom: 20px;
}

.error-title {
  font-size: 32px;
  margin-bottom: 20px;
}

.error-desc {
  font-size: 18px;
  color: #606266;
  margin-bottom: 30px;
}

.error-image {
  max-width: 300px;
  margin: 0 auto;
}

.error-image img {
  width: 100%;
}
</style> 