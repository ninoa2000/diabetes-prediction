<template>
  <div class="message-container">
    <el-card class="message-card">
      <template #header>
        <div class="card-header">
          <h2>我的留言</h2>
        </div>
      </template>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>
      
      <template v-else>
        <div v-if="!boundDoctor" class="no-doctor">
          <el-empty description="您尚未绑定医生" :image-size="200">
            <template #description>
              <p>绑定医生后才能发送留言</p>
            </template>
            <el-button type="primary" @click="goToDoctorBinding">绑定医生</el-button>
          </el-empty>
        </div>
        
        <template v-else>
          <div class="doctor-info">
            <span class="info-label">当前绑定医生：</span>
            <el-tag size="large">{{ boundDoctor.name }}</el-tag>
            <span class="info-detail">{{ boundDoctor.department }} - {{ boundDoctor.specialty }}</span>
          </div>
          
          <div class="message-form">
            <h3>发送新留言</h3>
            <el-form :model="messageForm" ref="messageFormRef" :rules="formRules">
              <el-form-item prop="content">
                <el-input
                  v-model="messageForm.content"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入留言内容，如健康问题咨询、预测结果解释等..."
                />
              </el-form-item>
              <el-form-item>
                <el-button 
                  type="primary"
                  @click="submitMessage"
                  :loading="submitting"
                >
                  发送留言
                </el-button>
              </el-form-item>
            </el-form>
          </div>
          
          <div class="message-history">
            <h3>历史留言</h3>
            
            <div v-if="messages.length === 0" class="empty-messages">
              <el-empty description="暂无历史留言" :image-size="100" />
            </div>
            
            <div v-else>
              <el-timeline>
                <el-timeline-item
                  v-for="message in messages"
                  :key="message.id"
                  :timestamp="formatDate(message.createdAt)"
                  :type="message.read ? 'success' : 'primary'"
                >
                  <el-card class="message-item">
                    <div class="message-content">{{ message.content }}</div>
                    <div v-if="message.replyContent" class="reply-content">
                      <div class="reply-label">医生回复：</div>
                      <div class="reply-text">{{ message.replyContent }}</div>
                    </div>
                    <div v-if="!message.read" class="message-status">
                      <el-tag size="small" type="info">未回复</el-tag>
                    </div>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </div>
          </div>
        </template>
      </template>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { messageService } from '@/api/message';
import { doctorService } from '@/api/doctor';

const router = useRouter();
const loading = ref(false);
const submitting = ref(false);
const boundDoctor = ref(null);
const messages = ref([]);
const messageFormRef = ref(null);

// Message form
const messageForm = reactive({
  content: ''
});

// Form validation rules
const formRules = {
  content: [
    { required: true, message: '请输入留言内容', trigger: 'blur' },
    { min: 5, max: 500, message: '留言长度在 5 到 500 个字符', trigger: 'blur' }
  ]
};

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

// Go to doctor binding page
const goToDoctorBinding = () => {
  router.push('/doctor-binding');
};

// Submit a new message
const submitMessage = () => {
  messageFormRef.value.validate(async (valid) => {
    if (!valid) return;
    
    if (!boundDoctor.value) {
      ElMessage.error('请先绑定医生');
      return;
    }
    
    try {
      submitting.value = true;
      await messageService.sendMessage(boundDoctor.value.id, messageForm.content);
      ElMessage.success('留言发送成功');
      
      // Clear form
      messageForm.content = '';
      
      // Reload messages
      await loadMessages();
    } catch (error) {
      ElMessage.error(error.message || '留言发送失败');
      console.error('Failed to send message:', error);
    } finally {
      submitting.value = false;
    }
  });
};

// Load bound doctor and messages
const loadData = async () => {
  try {
    loading.value = true;
    
    // Get bound doctor
    const doctorResponse = await doctorService.getBoundDoctor();
    boundDoctor.value = doctorResponse.data;
    
    // If a doctor is bound, get messages
    if (boundDoctor.value) {
      await loadMessages();
    }
  } catch (error) {
    ElMessage.error('加载数据失败');
    console.error('Failed to load data:', error);
  } finally {
    loading.value = false;
  }
};

// Load message history
const loadMessages = async () => {
  try {
    const response = await messageService.getPatientMessages();
    messages.value = response.data;
  } catch (error) {
    console.error('Failed to load messages:', error);
  }
};

// On component mount
onMounted(() => {
  loadData();
});
</script>

<style scoped>
.message-container {
  max-width: 800px;
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

.loading-container, .no-doctor {
  padding: 40px 0;
  text-align: center;
}

.doctor-info {
  margin-bottom: 20px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.info-label {
  margin-right: 10px;
  font-weight: 600;
}

.info-detail {
  margin-left: 10px;
  color: #606266;
}

.message-form, .message-history {
  margin-top: 30px;
}

.message-form h3, .message-history h3 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 10px;
}

.empty-messages {
  padding: 20px 0;
}

.message-item {
  margin-bottom: 10px;
}

.message-content {
  margin-bottom: 10px;
  line-height: 1.6;
}

.message-status {
  text-align: right;
}

.reply-content {
  margin-top: 10px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.reply-label {
  font-weight: 600;
  color: #606266;
  margin-bottom: 5px;
}

.reply-text {
  color: #303133;
  line-height: 1.6;
}
</style> 