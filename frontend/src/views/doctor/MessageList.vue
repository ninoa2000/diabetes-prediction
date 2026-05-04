<template>
  <div class="message-container">
    <el-card class="message-card">
      <template #header>
        <div class="card-header">
          <h2>Patient Messages</h2>
        </div>
      </template>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>
      
      <template v-else>
        <div v-if="messages.length === 0" class="empty-messages">
          <el-empty description="No patient messages yet" :image-size="200">
            <template #description>
              <p>When patients send messages, they will appear here.</p>
            </template>
          </el-empty>
        </div>
        
        <div v-else class="messages-list">
          <el-tabs v-model="activeTab" class="message-tabs">
            <el-tab-pane label="Unread" name="unread">
              <div v-if="unreadMessages.length === 0" class="empty-tab">
                <el-empty description="No unread messages" :image-size="100" />
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
            
            <el-tab-pane label="Read" name="read">
              <div v-if="readMessages.length === 0" class="empty-tab">
                <el-empty description="No read messages" :image-size="100" />
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
      :title="currentMessage ? 'Reply to Message' : `Message to ${targetPatientName}`"
      width="500px"
      :close-on-click-modal="false"
    >
      <div v-if="currentMessage || targetPatientId" class="reply-dialog-content">
        <div v-if="currentMessage" class="original-message">
          <h4>Patient Message:</h4>
          <div class="message-meta">
            <div>
              <span class="meta-label">Patient:</span>
              <span>{{ currentMessage.fromUserName }}</span>
            </div>
            <div>
              <span class="meta-label">Time:</span>
              <span>{{ formatDate(currentMessage.createdAt) }}</span>
            </div>
          </div>
          <div class="message-text">{{ currentMessage.content }}</div>
        </div>
        
        <div v-else-if="targetPatientId" class="direct-message-info">
          <p>Sending message to: <strong>{{ targetPatientName }}</strong></p>
        </div>
        
        <el-form :model="replyForm" ref="replyFormRef" :rules="formRules">
          <el-form-item prop="content">
            <el-input
              v-model="replyForm.content"
              type="textarea"
              :rows="4"
              placeholder="Enter your reply here..."
            />
          </el-form-item>
        </el-form>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="replyDialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="submitReply" :loading="submitting">
            Send Reply
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, reactive, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { messageService } from '@/api/message';
import MessageItem from '@/components/doctor/MessageItem.vue';

const route = useRoute();
const loading = ref(false);
const submitting = ref(false);
const messages = ref([]);
const activeTab = ref('unread');
const replyDialogVisible = ref(false);
const currentMessage = ref(null);
const replyFormRef = ref(null);

// Direct message from patient list
const targetPatientId = ref(route.query.patientId || null);
const targetPatientName = ref(route.query.patientName || '');

// Reply form
const replyForm = reactive({
  content: ''
});

// Form validation rules
const formRules = {
  content: [
    { required: true, message: 'Please enter reply content', trigger: 'blur' },
    { min: 5, max: 500, message: 'Reply length should be between 5 and 500 characters', trigger: 'blur' }
  ]
};

// Computed properties for message filtering
const readMessages = computed(() => {
  return messages.value.filter(message => message.read || message.type === 'DOCTOR_TO_PATIENT');
});

const unreadMessages = computed(() => {
  return messages.value.filter(message => !message.read && message.type === 'PATIENT_TO_DOCTOR');
});

// Format date for display
const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleString('en-US', {
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
    
    ElMessage.success('Message marked as read');
  } catch (error) {
    ElMessage.error('Action failed');
    console.error('Failed to mark message as read:', error);
  }
};

// Submit reply to patient
const submitReply = () => {
  replyFormRef.value.validate(async (valid) => {
    if (!valid) return;
    
    try {
      submitting.value = true;
      
      if (currentMessage.value) {
        // Reply to existing message
        await messageService.replyToMessage(currentMessage.value.id, replyForm.content);
        // Also send as a new message to maintain chat history consistency if needed
        const patientId = currentMessage.value.fromUserId;
        await messageService.sendMessage(patientId, replyForm.content);
      } else if (targetPatientId.value) {
        // Start new message thread
        await messageService.sendMessage(targetPatientId.value, replyForm.content);
      }
      
      ElMessage.success('Message sent successfully');
      replyDialogVisible.value = false;
      targetPatientId.value = null; // Clear target after sending
      await loadMessages();
    } catch (error) {
      ElMessage.error('Failed to send message');
      console.error('Failed to send message:', error);
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
    
    // Check if we need to open a direct message dialog
    if (targetPatientId.value) {
      currentMessage.value = null;
      replyForm.content = '';
      replyDialogVisible.value = true;
    }
  } catch (error) {
    ElMessage.error('Failed to load messages');
    console.error('Failed to load messages:', error);
  } finally {
    loading.value = false;
  }
};

// Watch for route changes (e.g. clicking message button from patient list)
watch(() => route.query.patientId, (newId) => {
  if (newId) {
    targetPatientId.value = newId;
    targetPatientName.value = route.query.patientName || '';
    replyForm.content = '';
    replyDialogVisible.value = true;
  }
});

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