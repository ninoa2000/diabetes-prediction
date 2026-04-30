<template>
  <div class="message-container">
    <el-card class="message-card">
      <template #header>
        <div class="card-header">
          <h2>患者留言</h2>
        </div>
      </template>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>
      
      <template v-else>
        <div v-if="messages.length === 0" class="empty-messages">
          <el-empty description="暂无患者留言" :image-size="200">
            <template #description>
              <p>当患者发送留言后，将会显示在这里</p>
            </template>
          </el-empty>
        </div>
        
        <div v-else class="messages-list">
          <el-tabs v-model="activeTab" class="message-tabs">
            <el-tab-pane label="未读留言" name="unread">
              <div v-if="unreadMessages.length === 0" class="empty-tab">
                <el-empty description="暂无未读留言" :image-size="100" />
              </div>
              <message-item
                v-else
                v-for="message in unreadMessages"
                :key="message.id"
                :message="message"
                @reply="handleReply"
                @mark-read="handleMarkAsRead"
              />
            </el-tab-pane>
            
            <el-tab-pane label="已读留言" name="read">
              <div v-if="readMessages.length === 0" class="empty-tab">
                <el-empty description="暂无已读留言" :image-size="100" />
              </div>
              <message-item
                v-else
                v-for="message in readMessages"
                :key="message.id"
                :message="message"
                @reply="handleReply"
                @mark-read="handleMarkAsRead"
              />
            </el-tab-pane>
          </el-tabs>
        </div>
      </template>
    </el-card>
    
    <!-- Reply Dialog -->
    <el-dialog
      v-model="replyDialogVisible"
      title="回复留言"
      width="500px"
      :close-on-click-modal="false"
    >
      <div v-if="currentMessage" class="reply-dialog-content">
        <div class="original-message">
          <h4>患者留言：</h4>
          <div class="message-meta">
            <div>
              <span class="meta-label">患者：</span>
              <span>{{ currentMessage.fromUserName }}</span>
            </div>
            <div>
              <span class="meta-label">时间：</span>
              <span>{{ formatDate(currentMessage.createdAt) }}</span>
            </div>
          </div>
          <div class="message-text">{{ currentMessage.content }}</div>
        </div>
        
        <el-form :model="replyForm" ref="replyFormRef" :rules="formRules">
          <el-form-item prop="content">
            <el-input
              v-model="replyForm.content"
              type="textarea"
              :rows="4"
              placeholder="请输入回复内容..."
            />
          </el-form-item>
        </el-form>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="replyDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitReply" :loading="submitting">
            发送回复
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { messageService } from '@/api/message';
import MessageItem from '@/components/doctor/MessageItem.vue';

const loading = ref(false);
const submitting = ref(false);
const messages = ref([]);
const activeTab = ref('unread');
const replyDialogVisible = ref(false);
const currentMessage = ref(null);
const replyFormRef = ref(null);

// Reply form
const replyForm = reactive({
  content: ''
});

// Form validation rules
const formRules = {
  content: [
    { required: true, message: '请输入回复内容', trigger: 'blur' },
    { min: 5, max: 500, message: '回复长度在 5 到 500 个字符', trigger: 'blur' }
  ]
};

// Computed properties for message filtering
const readMessages = computed(() => {
  return messages.value.filter(message => message.read);
});

const unreadMessages = computed(() => {
  return messages.value.filter(message => !message.read);
});

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

// Handle reply button click
const handleReply = (message) => {
  currentMessage.value = message;
  replyForm.content = '';
  replyDialogVisible.value = true;
};

// Mark a message as read
const handleMarkAsRead = async (messageId) => {
  try {
    await messageService.markAsRead(messageId);
    
    // Update message status locally
    const message = messages.value.find(m => m.id === messageId);
    if (message) {
      message.read = true;
    }
    
    ElMessage.success('留言已标记为已读');
  } catch (error) {
    ElMessage.error('操作失败');
    console.error('Failed to mark message as read:', error);
  }
};

// Submit reply to patient
const submitReply = () => {
  replyFormRef.value.validate(async (valid) => {
    if (!valid) return;
    
    try {
      submitting.value = true;
      
      // Send reply through API
      await messageService.replyToMessage(currentMessage.value.id, replyForm.content);
      
      // Update message status locally
      const message = messages.value.find(m => m.id === currentMessage.value.id);
      if (message) {
        message.read = true;
        // Reload messages to get the updated reply content
        await loadMessages();
      }
      
      ElMessage.success('回复已发送');
      replyDialogVisible.value = false;
    } catch (error) {
      ElMessage.error('回复发送失败');
      console.error('Failed to send reply:', error);
    } finally {
      submitting.value = false;
    }
  });
};

// Load doctor messages
const loadMessages = async () => {
  try {
    loading.value = true;
    const response = await messageService.getDoctorMessages();
    messages.value = response.data;
  } catch (error) {
    ElMessage.error('加载留言失败');
    console.error('Failed to load messages:', error);
  } finally {
    loading.value = false;
  }
};

// On component mount
onMounted(() => {
  loadMessages();
});
</script>

<style scoped>
.message-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}

.message-card {
  width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
}

.loading-container, .empty-messages {
  padding: 40px 0;
  text-align: center;
}

.empty-tab {
  padding: 20px 0;
  text-align: center;
}

.messages-list {
  margin-top: 10px;
}

.message-tabs {
  margin-bottom: 20px;
}

.reply-dialog-content {
  padding: 10px 0;
}

.original-message {
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.original-message h4 {
  margin-top: 0;
  margin-bottom: 10px;
  color: #303133;
  font-size: 16px;
}

.message-meta {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  color: #606266;
  margin-bottom: 10px;
}

.meta-label {
  font-weight: bold;
  margin-right: 5px;
}

.message-text {
  line-height: 1.5;
  white-space: pre-wrap;
}
</style> 