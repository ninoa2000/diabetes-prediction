<template>
  <div class="message-container">
    <el-card class="message-card">
      <template #header>
        <div class="card-header">
          <h2>My Messages</h2>
        </div>
      </template>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>
      
      <template v-else>
        <div v-if="!boundDoctor" class="no-doctor">
          <el-empty description="You have not bound a doctor yet" :image-size="200">
            <template #description>
              <p>You must bind a doctor to send messages</p>
            </template>
            <el-button type="primary" @click="goToDoctorBinding">Bind Doctor</el-button>
          </el-empty>
        </div>
        
        <template v-else>
          <div class="doctor-profile-mini">
            <el-avatar :size="50" icon="UserFilled" />
            <div class="doctor-details">
              <h3>{{ boundDoctor.name }}</h3>
              <p>{{ boundDoctor.department }} • {{ boundDoctor.specialty }}</p>
            </div>
            <el-button type="primary" plain @click="loadMessages">
              <el-icon><Refresh /></el-icon> Refresh
            </el-button>
          </div>
          
          <div class="chat-area">
            <div v-if="messages.length === 0" class="empty-chat">
              <el-empty description="Start a conversation with your doctor" />
            </div>
            
            <div v-else class="message-threads">
              <div v-for="message in messages" :key="message.id" class="message-item-container">
                <!-- Patient Message (Current User) -->
                <div v-if="message.type === 'PATIENT_TO_DOCTOR'" class="bubble patient-bubble">
                  <div class="bubble-header">
                    <span class="user-name">You</span>
                    <span class="time">{{ formatDate(message.createdAt) }}</span>
                  </div>
                  <div class="bubble-content">
                    <div v-if="message.imageUrl" class="message-image">
                      <el-image :src="message.imageUrl" :preview-src-list="[message.imageUrl]" fit="cover" />
                    </div>
                    {{ message.content }}
                  </div>
                </div>
                
                <!-- Doctor Message -->
                <div v-else-if="message.type === 'DOCTOR_TO_PATIENT'" class="bubble doctor-bubble">
                  <div class="bubble-header">
                    <span class="user-name">{{ boundDoctor.name }}</span>
                    <span class="time">{{ formatDate(message.createdAt) }}</span>
                  </div>
                  <div class="bubble-content">
                    <div v-if="message.imageUrl" class="message-image">
                      <el-image :src="message.imageUrl" :preview-src-list="[message.imageUrl]" fit="cover" />
                    </div>
                    {{ message.content }}
                  </div>
                </div>

                <!-- Legacy Reply Support -->
                <div v-if="message.replyContent" class="bubble doctor-bubble legacy-reply">
                  <div class="bubble-header">
                    <span class="user-name">{{ boundDoctor.name }} (Reply)</span>
                    <span class="time">{{ formatDate(message.updatedAt) }}</span>
                  </div>
                  <div class="bubble-content">{{ message.replyContent }}</div>
                </div>
              </div>
            </div>
          </div>
          
          <div class="message-input-area">
            <el-form :model="messageForm" ref="messageFormRef" :rules="formRules">
              <el-form-item prop="content">
                <el-input
                  v-model="messageForm.content"
                  type="textarea"
                  :rows="3"
                  placeholder="Type your message to the doctor here..."
                  resize="none"
                />
              </el-form-item>
              <el-form-item v-if="showImageUrlInput" label="Image URL">
                <el-input v-model="messageForm.imageUrl" placeholder="Paste image link here..." />
              </el-form-item>
              <div class="input-actions">
                <div class="left-actions">
                  <el-button @click="showImageUrlInput = !showImageUrlInput" size="small" :type="showImageUrlInput ? 'primary' : ''">
                    <el-icon><Picture /></el-icon> {{ showImageUrlInput ? 'Cancel Image' : 'Add Image' }}
                  </el-button>
                  <span class="hint ml-2">Your doctor will be notified.</span>
                </div>
                <el-button 
                  type="primary"
                  size="large"
                  @click="submitMessage"
                  :loading="submitting"
                  class="send-btn"
                >
                  <el-icon class="mr-1"><Position /></el-icon> Send Message
                </el-button>
              </div>
            </el-form>
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
const showImageUrlInput = ref(false);
const boundDoctor = ref(null);
const messages = ref([]);
const messageFormRef = ref(null);

// Message form
const messageForm = reactive({
  content: '',
  imageUrl: ''
});

// Form validation rules
const formRules = {
  content: [
    { required: true, message: 'Please enter message content', trigger: 'blur' },
    { min: 5, max: 500, message: 'Message length must be between 5 and 500 characters', trigger: 'blur' }
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
      ElMessage.error('Please bind a doctor first');
      return;
    }
    
    try {
      submitting.value = true;
      await messageService.sendMessage(boundDoctor.value.id, messageForm.content, messageForm.imageUrl);
      ElMessage.success('Message sent successfully');
      
      // Clear form
      messageForm.content = '';
      messageForm.imageUrl = '';
      showImageUrlInput.value = false;
      
      // Reload messages
      await loadMessages();
    } catch (error) {
      ElMessage.error(error.message || 'Failed to send message');
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
    ElMessage.error('Failed to load data');
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
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}

.message-card {
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}

.doctor-profile-mini {
  display: flex;
  align-items: center;
  padding: 15px;
  background: #f8fafc;
  border-radius: 12px;
  margin-bottom: 20px;
}

.doctor-details {
  flex: 1;
  margin-left: 15px;
}

.doctor-details h3 {
  margin: 0;
  font-size: 16px;
  color: #1e293b;
}

.doctor-details p {
  margin: 4px 0 0;
  font-size: 13px;
  color: #64748b;
}

.chat-area {
  height: 450px;
  overflow-y: auto;
  padding: 20px;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  margin-bottom: 20px;
}

.message-item-container {
  margin-bottom: 25px;
  display: flex;
  flex-direction: column;
}

.bubble {
  max-width: 85%;
  padding: 12px 18px;
  border-radius: 20px;
  margin-bottom: 10px;
  position: relative;
  line-height: 1.6;
  box-shadow: 0 2px 6px rgba(0,0,0,0.05);
  font-size: 14.5px;
}

.bubble-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6px;
  font-size: 12px;
  font-weight: 500;
  opacity: 0.7;
}

.patient-bubble {
  align-self: flex-end;
  background: linear-gradient(135deg, #4f46e5 0%, #3b82f6 100%);
  color: white;
  border-bottom-right-radius: 4px;
}

.doctor-bubble {
  align-self: flex-start;
  background: #ffffff;
  color: #1e293b;
  border: 1px solid #f1f5f9;
  border-bottom-left-radius: 4px;
}

.legacy-reply {
  margin-top: -5px;
  background: #f8fafc;
  border-style: dashed;
}

.message-input-area {
  padding-top: 25px;
  border-top: 1px solid #f1f5f9;
}

.input-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.hint {
  font-size: 12px;
  color: #94a3b8;
}

.send-btn {
  padding: 0 30px;
}

.mr-1 {
  margin-right: 8px;
}

/* Custom Scrollbar */
.chat-area::-webkit-scrollbar {
  width: 6px;
}
.chat-area::-webkit-scrollbar-track {
  background: transparent;
}
.chat-area::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 3px;
}
</style>