<template>
  <div class="message-item" :class="{ 'is-read': message.read }">
    <el-card shadow="hover">
      <div class="message-header">
        <div class="patient-info">
          <span class="patient-name">{{ message.fromUserName }}</span>
          <el-tag 
            size="small" 
            :type="message.read ? 'success' : 'danger'"
            effect="dark"
          >
            {{ message.read ? '已读' : '未读' }}
          </el-tag>
        </div>
        <div class="message-time">{{ formatDate(message.createdAt) }}</div>
      </div>
      
      <div class="message-content">{{ message.content }}</div>
      
      <div v-if="message.read && message.replyContent" class="reply-content">
        <div class="reply-header">
          <span class="reply-label">回复：</span>
          <span class="reply-time">{{ formatDate(message.updatedAt) }}</span>
        </div>
        <div class="reply-text">{{ message.replyContent }}</div>
      </div>
      
      <div v-else class="message-actions">
        <el-button 
          type="primary" 
          size="small" 
          @click="$emit('reply', message)"
        >
          回复
        </el-button>
        
        <el-button 
          v-if="!message.read" 
          type="info" 
          size="small" 
          @click="$emit('mark-read', message.id)"
        >
          标记为已读
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  message: {
    type: Object,
    required: true
  }
});

// Define emits
defineEmits(['reply', 'mark-read']);

// Format date for display
const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
};
</script>

<style scoped>
.message-item {
  margin-bottom: 16px;
}

.message-item.is-read :deep(.el-card) {
  border-left: 3px solid #67c23a;
}

.message-item:not(.is-read) :deep(.el-card) {
  border-left: 3px solid #f56c6c;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.patient-info {
  display: flex;
  align-items: center;
}

.patient-name {
  font-weight: bold;
  margin-right: 10px;
  font-size: 16px;
}

.message-time {
  color: #909399;
  font-size: 14px;
}

.message-content {
  padding: 10px 0;
  line-height: 1.6;
  white-space: pre-wrap;
  color: #303133;
}

.reply-content {
  margin-top: 15px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.reply-label {
  font-weight: bold;
  color: #606266;
}

.reply-time {
  color: #909399;
  font-size: 12px;
}

.reply-text {
  line-height: 1.6;
  white-space: pre-wrap;
  color: #303133;
}

.message-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 10px;
}
</style> 