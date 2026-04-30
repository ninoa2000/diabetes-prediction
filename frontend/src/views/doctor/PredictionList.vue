<template>
  <el-container>
    <el-main>
      <el-card>
        <template #header>
          <div class="card-header">
            <span>患者预测记录</span>
            <div class="header-actions">
              <el-input
                v-model="search"
                placeholder="搜索患者..."
                style="width: 300px"
                class="mr-3"
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
              <el-select
                v-model="selectedDisease"
                placeholder="疾病类型"
                clearable
                style="width: 180px"
                class="mr-3"
              >
                <el-option
                  v-for="disease in diseaseTypes"
                  :key="disease"
                  :label="disease"
                  :value="disease"
                ></el-option>
              </el-select>
              <el-select
                v-model="riskLevel"
                placeholder="风险等级"
                clearable
                style="width: 150px"
                class="mr-3"
              >
                <el-option
                  v-for="level in riskLevels"
                  :key="level.value"
                  :label="level.label"
                  :value="level.value"
                ></el-option>
              </el-select>
            </div>
          </div>
        </template>
        
        <el-table
          :data="filteredPredictions"
          style="width: 100%"
          border
          :default-sort="{ prop: 'date', order: 'descending' }"
        >
          <el-table-column prop="patientName" label="患者姓名" width="120" sortable></el-table-column>
          <el-table-column prop="diseaseType" label="疾病类型" width="120"></el-table-column>
          <el-table-column prop="date" label="预测日期" width="180" sortable>
            <template #default="scope">
              {{ formatDate(scope.row.date) }}
            </template>
          </el-table-column>
          <el-table-column prop="riskScore" label="风险评分" width="100" sortable>
            <template #default="scope">
              <el-progress
                :percentage="scope.row.riskScore"
                :color="getRiskColor(scope.row.riskScore)"
                :stroke-width="18"
              ></el-progress>
            </template>
          </el-table-column>
          <el-table-column prop="riskLevel" label="风险等级" width="120">
            <template #default="scope">
              <el-tag :type="getRiskTagType(scope.row.riskLevel)">
                {{ scope.row.riskLevel }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="120">
            <template #default="scope">
              <el-tag :type="getStatusTagType(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="250">
            <template #default="scope">
              <el-button
                size="small"
                @click="viewDetails(scope.row)"
                type="primary"
                plain
                class="mr-2"
              >
                详情
              </el-button>
              <el-button
                size="small"
                @click="provideFeedback(scope.row)"
                type="success"
                plain
                :disabled="scope.row.status === '已反馈'"
                class="mr-2"
              >
                反馈
              </el-button>
              <el-button
                size="small"
                @click="contactPatient(scope.row)"
                type="info"
                plain
              >
                联系患者
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="filteredPredictions.length"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
      
      <!-- 详情对话框 -->
      <el-dialog
        v-model="detailsDialog"
        title="预测详情"
        width="800px"
      >
        <template v-if="selectedPrediction">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="患者姓名" span="1">{{ selectedPrediction.patientName }}</el-descriptions-item>
            <el-descriptions-item label="年龄" span="1">{{ selectedPrediction.patientAge }}</el-descriptions-item>
            <el-descriptions-item label="性别" span="1">{{ selectedPrediction.patientGender }}</el-descriptions-item>
            <el-descriptions-item label="联系方式" span="1">{{ selectedPrediction.patientContact }}</el-descriptions-item>
            <el-descriptions-item label="疾病类型" span="1">{{ selectedPrediction.diseaseType }}</el-descriptions-item>
            <el-descriptions-item label="预测日期" span="1">{{ formatDate(selectedPrediction.date) }}</el-descriptions-item>
            <el-descriptions-item label="风险评分" span="1">
              <el-progress 
                :percentage="selectedPrediction.riskScore" 
                :color="getRiskColor(selectedPrediction.riskScore)"
                :stroke-width="18"
              ></el-progress>
            </el-descriptions-item>
            <el-descriptions-item label="风险等级" span="1">
              <el-tag :type="getRiskTagType(selectedPrediction.riskLevel)">
                {{ selectedPrediction.riskLevel }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="状态" span="1">
              <el-tag :type="getStatusTagType(selectedPrediction.status)">
                {{ selectedPrediction.status }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="关键指标" span="2">
              <el-table :data="selectedPrediction.indicators" style="width: 100%">
                <el-table-column prop="name" label="指标名称" width="180"></el-table-column>
                <el-table-column prop="value" label="指标值" width="120"></el-table-column>
                <el-table-column prop="normalRange" label="正常范围" width="180"></el-table-column>
                <el-table-column prop="status" label="状态">
                  <template #default="scope">
                    <el-tag :type="scope.row.status === '正常' ? 'success' : 'danger'">
                      {{ scope.row.status }}
                    </el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </el-descriptions-item>
            <el-descriptions-item label="风险因素" span="2">{{ selectedPrediction.riskFactors }}</el-descriptions-item>
            <el-descriptions-item label="建议" span="2">{{ selectedPrediction.recommendations }}</el-descriptions-item>
            <el-descriptions-item 
              v-if="selectedPrediction.doctorFeedback" 
              label="医生反馈" 
              span="2"
            >
              {{ selectedPrediction.doctorFeedback }}
            </el-descriptions-item>
          </el-descriptions>
        </template>
      </el-dialog>
      
      <!-- 反馈对话框 -->
      <el-dialog
        v-model="feedbackDialog"
        title="医生反馈"
        width="600px"
      >
        <el-form :model="feedbackForm" ref="feedbackFormRef" :rules="feedbackRules" label-width="120px">
          <el-form-item label="预测准确性" prop="accuracy">
            <el-rate
              v-model="feedbackForm.accuracy"
              :colors="rateColors"
              show-score
            ></el-rate>
          </el-form-item>
          <el-form-item label="医生诊断" prop="diagnosis">
            <el-input v-model="feedbackForm.diagnosis" type="textarea" :rows="2"></el-input>
          </el-form-item>
          <el-form-item label="治疗方案" prop="treatmentPlan">
            <el-input v-model="feedbackForm.treatmentPlan" type="textarea" :rows="3"></el-input>
          </el-form-item>
          <el-form-item label="生活建议" prop="lifestyleAdvice">
            <el-input v-model="feedbackForm.lifestyleAdvice" type="textarea" :rows="3"></el-input>
          </el-form-item>
          <el-form-item label="随访计划" prop="followUpPlan">
            <el-date-picker
              v-model="feedbackForm.followUpDate"
              type="date"
              placeholder="选择随访日期"
              style="width: 100%"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="额外备注" prop="notes">
            <el-input v-model="feedbackForm.notes" type="textarea" :rows="2"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="feedbackDialog = false">取消</el-button>
            <el-button type="primary" @click="submitFeedback">提交反馈</el-button>
          </div>
        </template>
      </el-dialog>
    </el-main>
  </el-container>
</template>

<script>
import { ref, computed, reactive } from 'vue';
import { Search } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';

export default {
  name: 'PredictionList',
  components: {
    Search
  },
  setup() {
    const search = ref('');
    const selectedDisease = ref('');
    const riskLevel = ref('');
    const currentPage = ref(1);
    const pageSize = ref(10);
    const detailsDialog = ref(false);
    const feedbackDialog = ref(false);
    const selectedPrediction = ref(null);
    const feedbackFormRef = ref(null);
    
    const feedbackForm = reactive({
      accuracy: 3,
      diagnosis: '',
      treatmentPlan: '',
      lifestyleAdvice: '',
      followUpDate: '',
      notes: ''
    });
    
    const feedbackRules = {
      diagnosis: [
        { required: true, message: '请输入医生诊断', trigger: 'blur' }
      ],
      treatmentPlan: [
        { required: true, message: '请输入治疗方案', trigger: 'blur' }
      ]
    };
    
    const rateColors = ['#F56C6C', '#E6A23C', '#6F7AD3', '#5CB87A', '#1F9F85'];
    
    const diseaseTypes = [
      '高血压', '糖尿病', '冠心病', '脑卒中', '慢性肾病', '慢性阻塞性肺疾病'
    ];
    
    const riskLevels = [
      { value: '低风险', label: '低风险' },
      { value: '中风险', label: '中风险' },
      { value: '高风险', label: '高风险' },
      { value: '极高风险', label: '极高风险' }
    ];
    
    // 模拟数据
    const predictions = ref([
      {
        id: 1,
        patientId: 'P001',
        patientName: '张三',
        patientAge: 58,
        patientGender: '男',
        patientContact: '13800138001',
        diseaseType: '高血压',
        date: '2023-05-10T08:30:00',
        riskScore: 75,
        riskLevel: '高风险',
        status: '待处理',
        indicators: [
          { name: '收缩压', value: '150mmHg', normalRange: '90-120mmHg', status: '异常' },
          { name: '舒张压', value: '95mmHg', normalRange: '60-80mmHg', status: '异常' },
          { name: '心率', value: '78次/分', normalRange: '60-100次/分', status: '正常' },
          { name: 'BMI', value: '28.5', normalRange: '18.5-24.9', status: '异常' }
        ],
        riskFactors: '年龄、家族史、肥胖、高盐饮食、缺乏运动',
        recommendations: '控制饮食，减少盐的摄入，增加有氧运动，控制体重，定期监测血压'
      },
      {
        id: 2,
        patientId: 'P002',
        patientName: '李四',
        patientAge: 62,
        patientGender: '男',
        patientContact: '13800138002',
        diseaseType: '糖尿病',
        date: '2023-05-09T10:15:00',
        riskScore: 85,
        riskLevel: '极高风险',
        status: '已反馈',
        indicators: [
          { name: '空腹血糖', value: '8.2mmol/L', normalRange: '3.9-6.1mmol/L', status: '异常' },
          { name: 'HbA1c', value: '7.8%', normalRange: '4.0-6.0%', status: '异常' },
          { name: '尿微量白蛋白', value: '35mg/24h', normalRange: '<30mg/24h', status: '异常' }
        ],
        riskFactors: '年龄、家族史、肥胖、高碳水饮食、久坐不动',
        recommendations: '控制饮食，减少碳水化合物和糖的摄入，增加运动，定期监测血糖',
        doctorFeedback: '患者情况符合2型糖尿病诊断，建议立即开始生活方式干预，并考虑口服降糖药物治疗。'
      },
      {
        id: 3,
        patientId: 'P003',
        patientName: '王五',
        patientAge: 45,
        patientGender: '女',
        patientContact: '13800138003',
        diseaseType: '高血压',
        date: '2023-05-11T14:20:00',
        riskScore: 35,
        riskLevel: '低风险',
        status: '待处理',
        indicators: [
          { name: '收缩压', value: '125mmHg', normalRange: '90-120mmHg', status: '异常' },
          { name: '舒张压', value: '82mmHg', normalRange: '60-80mmHg', status: '异常' },
          { name: '心率', value: '72次/分', normalRange: '60-100次/分', status: '正常' },
          { name: 'BMI', value: '23.5', normalRange: '18.5-24.9', status: '正常' }
        ],
        riskFactors: '工作压力大、轻度肥胖',
        recommendations: '保持健康的生活方式，定期监测血压，减轻工作压力'
      },
      {
        id: 4,
        patientId: 'P004',
        patientName: '赵六',
        patientAge: 70,
        patientGender: '男',
        patientContact: '13800138004',
        diseaseType: '冠心病',
        date: '2023-05-08T09:45:00',
        riskScore: 65,
        riskLevel: '中风险',
        status: '已反馈',
        indicators: [
          { name: '总胆固醇', value: '5.8mmol/L', normalRange: '<5.2mmol/L', status: '异常' },
          { name: 'LDL胆固醇', value: '3.5mmol/L', normalRange: '<3.4mmol/L', status: '异常' },
          { name: 'HDL胆固醇', value: '1.0mmol/L', normalRange: '>1.0mmol/L', status: '正常' },
          { name: '血压', value: '145/90mmHg', normalRange: '<140/90mmHg', status: '异常' }
        ],
        riskFactors: '年龄、高血压、高胆固醇、吸烟史',
        recommendations: '戒烟，低脂饮食，规律运动，控制血压，服用他汀类药物降脂',
        doctorFeedback: '患者有明显的冠心病风险因素，已开具他汀类药物处方，并建议每周进行3次有氧运动，每次30分钟。'
      },
      {
        id: 5,
        patientId: 'P005',
        patientName: '孙七',
        patientAge: 55,
        patientGender: '女',
        patientContact: '13800138005',
        diseaseType: '慢性阻塞性肺疾病',
        date: '2023-05-07T15:30:00',
        riskScore: 55,
        riskLevel: '中风险',
        status: '待处理',
        indicators: [
          { name: 'FEV1', value: '65%预计值', normalRange: '>80%预计值', status: '异常' },
          { name: 'FEV1/FVC', value: '0.65', normalRange: '>0.7', status: '异常' },
          { name: '血氧饱和度', value: '94%', normalRange: '95-100%', status: '异常' }
        ],
        riskFactors: '长期吸烟、职业暴露、反复呼吸道感染',
        recommendations: '立即戒烟，避免空气污染，接种流感和肺炎疫苗，考虑支气管扩张剂治疗'
      }
    ]);
    
    const filteredPredictions = computed(() => {
      let result = predictions.value;
      
      if (search.value) {
        const searchLower = search.value.toLowerCase();
        result = result.filter(p => 
          p.patientName.toLowerCase().includes(searchLower) ||
          p.patientId.toLowerCase().includes(searchLower)
        );
      }
      
      if (selectedDisease.value) {
        result = result.filter(p => p.diseaseType === selectedDisease.value);
      }
      
      if (riskLevel.value) {
        result = result.filter(p => p.riskLevel === riskLevel.value);
      }
      
      return result;
    });
    
    const paginatedPredictions = computed(() => {
      const start = (currentPage.value - 1) * pageSize.value;
      const end = start + pageSize.value;
      return filteredPredictions.value.slice(start, end);
    });
    
    const formatDate = (dateString) => {
      const date = new Date(dateString);
      return date.toLocaleString();
    };
    
    const getRiskColor = (score) => {
      if (score < 30) return '#67C23A';
      if (score < 60) return '#E6A23C';
      if (score < 80) return '#F56C6C';
      return '#F56C6C';
    };
    
    const getRiskTagType = (level) => {
      switch (level) {
        case '低风险': return 'success';
        case '中风险': return 'warning';
        case '高风险': return 'danger';
        case '极高风险': return 'danger';
        default: return '';
      }
    };
    
    const getStatusTagType = (status) => {
      switch (status) {
        case '待处理': return 'info';
        case '已反馈': return 'success';
        default: return '';
      }
    };
    
    const viewDetails = (prediction) => {
      selectedPrediction.value = prediction;
      detailsDialog.value = true;
    };
    
    const provideFeedback = (prediction) => {
      selectedPrediction.value = prediction;
      // 重置表单
      Object.assign(feedbackForm, {
        accuracy: 3,
        diagnosis: '',
        treatmentPlan: '',
        lifestyleAdvice: '',
        followUpDate: new Date(new Date().setDate(new Date().getDate() + 30)), // 默认30天后随访
        notes: ''
      });
      feedbackDialog.value = true;
    };
    
    const submitFeedback = () => {
      feedbackFormRef.value.validate((valid) => {
        if (valid) {
          // 在实际应用中，这里应该调用API提交反馈
          const index = predictions.value.findIndex(p => p.id === selectedPrediction.value.id);
          if (index !== -1) {
            // 更新状态
            predictions.value[index].status = '已反馈';
            // 保存反馈内容
            predictions.value[index].doctorFeedback = `诊断: ${feedbackForm.diagnosis}\n治疗方案: ${feedbackForm.treatmentPlan}\n生活建议: ${feedbackForm.lifestyleAdvice}\n随访日期: ${feedbackForm.followUpDate ? formatDate(feedbackForm.followUpDate) : '未设置'}\n备注: ${feedbackForm.notes}`;
            
            ElMessage.success('反馈提交成功');
            feedbackDialog.value = false;
          }
        }
      });
    };
    
    const contactPatient = (prediction) => {
      ElMessageBox.confirm(
        `是否联系患者 ${prediction.patientName}？联系电话: ${prediction.patientContact}`,
        '联系患者',
        {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'info'
        }
      ).then(() => {
        ElMessage.success(`已发起与患者 ${prediction.patientName} 的通话`);
      }).catch(() => {});
    };
    
    const handleSizeChange = (size) => {
      pageSize.value = size;
      currentPage.value = 1;
    };
    
    const handleCurrentChange = (page) => {
      currentPage.value = page;
    };
    
    return {
      search,
      selectedDisease,
      riskLevel,
      currentPage,
      pageSize,
      detailsDialog,
      feedbackDialog,
      selectedPrediction,
      feedbackForm,
      feedbackFormRef,
      feedbackRules,
      rateColors,
      diseaseTypes,
      riskLevels,
      predictions,
      filteredPredictions,
      paginatedPredictions,
      formatDate,
      getRiskColor,
      getRiskTagType,
      getStatusTagType,
      viewDetails,
      provideFeedback,
      submitFeedback,
      contactPatient,
      handleSizeChange,
      handleCurrentChange
    };
  }
};
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  align-items: center;
}

.mr-2 {
  margin-right: 8px;
}

.mr-3 {
  margin-right: 12px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style> 