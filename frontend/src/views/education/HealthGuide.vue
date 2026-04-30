<template>
  <el-container>
    <el-main>
      <h1 class="text-h4 mb-6">健康生活指南</h1>

      <el-row :gutter="20">
        <el-col :xs="24" :md="16">
          <p class="text-body-1 mb-4">
            健康的生活方式是预防和管理慢性疾病的关键。以下指南包含了有关营养、体育活动、睡眠和压力管理的实用建议，
            帮助您改善健康状况，降低慢性疾病风险。
          </p>
        </el-col>
        <el-col :xs="24" :md="8">
          <el-image
            src="/images/default.jpg"
            style="height: 200px; width: 100%"
            fit="cover"
            class="rounded-lg"
          ></el-image>
        </el-col>
      </el-row>

      <el-divider class="my-6"></el-divider>

      <el-tabs v-model="tab" type="card" class="text-center">
        <el-tab-pane label="营养饮食" name="nutrition" />
        <el-tab-pane label="体育活动" name="exercise" />
        <el-tab-pane label="睡眠健康" name="sleep" />
        <el-tab-pane label="压力管理" name="stress" />
        <el-tab-pane label="健康习惯" name="habits" />
      </el-tabs>

      <div class="mt-5">
        <!-- 1. 营养饮食 -->
        <div v-if="tab === 'nutrition'">
          <el-row :gutter="20">
            <el-col :xs="24" :md="12">
              <h2 class="text-h5 mb-4">均衡饮食的原则</h2>
              <el-card>
                <el-collapse>
                  <el-collapse-item
                    v-for="(item, i) in nutritionPrinciples"
                    :key="i"
                    :title="item.title"
                  >
                    <div class="item-content">
                      <el-avatar :size="36" class="primary-bg">
                        <el-icon>{{ item.icon }}</el-icon>
                      </el-avatar>
                      <span>{{ item.description }}</span>
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </el-card>
            </el-col>
            <el-col :xs="24" :md="12">
              <h2 class="text-h5 mb-4">营养摄入建议</h2>
              <el-card>
                <div class="card-content">
                  <el-table :data="nutritionList" border style="width:100%">
                    <el-table-column prop="title" label="标题" />
                    <el-table-column prop="category" label="分类" />
                    <el-table-column prop="content" label="描述" />
                  </el-table>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>

        <!-- 2. 体育活动 -->
        <div v-if="tab === 'exercise'">
          <el-row :gutter="20">
            <el-col :xs="24" :md="12">
              <h2 class="text-h5 mb-4">体育活动的益处</h2>
              <el-card>
                <el-collapse>
                  <el-collapse-item
                    v-for="(benefit, i) in exerciseBenefits"
                    :key="i"
                    :title="benefit.title"
                  >
                    <div class="item-content">
                      <el-icon class="success-text">{{ benefit.icon }}</el-icon>
                      <span>{{ benefit.description }}</span>
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </el-card>
            </el-col>
            <el-col :xs="24" :md="12">
              <h2 class="text-h5 mb-4">适合糖尿病患者的活动</h2>
              <el-row :gutter="20">
                <el-col v-for="(activity, i) in exerciseActivities" :key="i" :xs="24" :sm="12">
                  <el-card class="mb-3" shadow="hover">
                    <h3 class="card-title">{{ activity.name }}</h3>
                    <div class="card-content">
                      <p>{{ activity.description }}</p>
                      <div class="mt-2">
                        <el-tag size="small" type="primary">{{ activity.intensity }}</el-tag>
                        <el-tag size="small" class="ml-2" type="primary">{{ activity.frequency }}</el-tag>
                      </div>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </el-col>
          </el-row>
        </div>

        <!-- 3. 睡眠健康 -->
        <div v-if="tab === 'sleep'">
          <el-row :gutter="20">
            <el-col :xs="24" :md="12">
              <h2 class="text-h5 mb-4">良好睡眠的重要性</h2>
              <p class="text-body-1 mb-3">
                充足的高质量睡眠对身体健康至关重要。睡眠不足会增加慢性疾病的风险，包括肥胖、糖尿病、心脏病和高血压。
                科学研究表明，成年人需要每晚7-9小时的睡眠才能维持最佳健康。
              </p>
              <h3 class="text-subtitle-1 mt-4 mb-2">睡眠对慢性疾病的影响</h3>
              <el-card>
                <el-collapse>
                  <el-collapse-item v-for="(impact,i) in sleepImpacts" :key="i" :title="impact.disease">
                    <div class="item-content">
                      <el-icon class="blue-text"><Moon /></el-icon>
                      <span>{{ impact.effect }}</span>
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </el-card>
            </el-col>
            <el-col :xs="24" :md="12">
              <h2 class="text-h5 mb-4">改善睡眠的策略</h2>
              <el-timeline>
                <el-timeline-item v-for="(tip,i) in sleepTips" :key="i" :type="tip.type" :timestamp="tip.title">
                  <el-card>
                    <div class="timeline-content">
                      <el-icon>{{ tip.icon }}</el-icon>
                      <p>{{ tip.description }}</p>
                    </div>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </el-col>
          </el-row>
        </div>

        <!-- 4. 压力管理 -->
        <div v-if="tab === 'stress'">
          <el-row :gutter="20">
            <el-col :xs="24" :md="12">
              <h2 class="text-h5 mb-4">慢性压力的健康影响</h2>
              <p class="text-body-1 mb-4">
                长期压力会导致身体持续处于"战斗或逃跑"模式，释放有害激素并增加慢性疾病风险。
                有效的压力管理是预防和控制慢性疾病的重要组成部分。
              </p>
              <el-card class="mb-4">
                <template #header>
                  <div class="card-header bg-red-lighten-4">
                    <el-icon class="mr-2"><StarFilled /></el-icon>
                    压力与慢性疾病的关系
                  </div>
                </template>
                <el-collapse>
                  <el-collapse-item v-for="(effect,i) in stressEffects" :key="i" :title="effect.disease">
                    <div class="item-content">
                      <el-icon class="red-text"><ArrowRight /></el-icon>
                      <span>{{ effect.impact }}</span>
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </el-card>
            </el-col>
            <el-col :xs="24" :md="12">
              <h2 class="text-h5 mb-4">压力管理技巧</h2>
              <el-row :gutter="20">
                <el-col v-for="(technique,i) in stressTechniques" :key="i" :xs="24" :sm="12">
                  <el-card shadow="hover" class="mb-3">
                    <template #header>
                      <div class="card-header">
                        <el-icon class="mr-2 indigo-text">{{ technique.icon }}</el-icon>
                        <span>{{ technique.name }}</span>
                      </div>
                    </template>
                    <div class="card-content">
                      <p>{{ technique.description }}</p>
                      <el-tag class="mt-2" size="small" type="info">{{ technique.timeRequired }}</el-tag>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </el-col>
          </el-row>
        </div>

        <!-- 5. 健康习惯 -->
        <div v-if="tab === 'habits'">
          <el-row :gutter="20">
            <el-col :xs="24" :md="12">
              <h2 class="text-h5 mb-4">养成健康习惯的步骤</h2>
              <el-steps direction="vertical" :active="4">
                <el-step title="设定目标" description="设定具体、可测量、可实现、相关、有时限的健康目标" />
                <el-step title="小步开始" description="从小的、可管理的改变开始，逐步建立新习惯" />
                <el-step title="跟踪进度" description="使用日记、应用程序或其他工具记录和监测您的进展" />
                <el-step title="坚持不懈" description="培养新习惯通常需要至少66天，保持耐心和持续性" />
              </el-steps>
              <el-card class="mt-4 mb-4">
                <template #header>
                  <div class="card-header bg-green-lighten-4">
                    <el-icon class="mr-2"><SuccessFilled /></el-icon>
                    健康习惯的长期效益
                  </div>
                </template>
                <el-collapse>
                  <el-collapse-item v-for="(benefit,i) in habitBenefits" :key="i" :title="benefit.title">
                    <div class="item-content">
                      <el-icon class="green-text"><Check /></el-icon>
                      <span>{{ benefit.description }}</span>
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </el-card>
            </el-col>
            <el-col :xs="24" :md="12">
              <h2 class="text-h5 mb-4">关键健康习惯</h2>
              <el-row :gutter="20">
                <el-col v-for="(habit,i) in keyHabits" :key="i" :xs="24" :sm="12">
                  <el-card class="mb-3" shadow="hover">
                    <template #header>
                      <div class="card-header">
                        <el-icon class="mr-2 teal-text">{{ habit.icon }}</el-icon>
                        <span>{{ habit.name }}</span>
                      </div>
                    </template>
                    <div class="card-content">
                      <p>{{ habit.description }}</p>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </el-col>
          </el-row>
        </div>
      </div>
    </el-main>
  </el-container>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { listNutrition } from '../../api/Knowledge';
import { Check, ArrowRight, SuccessFilled, StarFilled, Moon } from '@element-plus/icons-vue';

// 导入静态数据
const tab = ref('nutrition');
const nutritionList = ref([]);

const nutritionPrinciples = [
  { title: '多样化饮食', description: '每天食用各种食物，包括蔬菜、水果、全谷物、蛋白质和健康脂肪', icon: Check },
  { title: '控制份量', description: '注意食物份量，避免过量进食，尤其是高热量食品', icon: ArrowRight },
  { title: '增加膳食纤维', description: '选择富含膳食纤维的食物，如豆类、全谷物、蔬菜和水果', icon: SuccessFilled },
  { title: '减少饱和脂肪', description: '限制饱和脂肪和反式脂肪的摄入，选择健康脂肪如橄榄油和坚果', icon: StarFilled },
  { title: '限制盐和糖', description: '减少添加糖和盐的摄入，选择天然调味的食物', icon: Moon }
];

const exerciseBenefits = [
    {
          title: '改善心血管健康',
          description: '定期运动可降低血压，提高心脏功能，减少心脏病和中风风险'
        },
        {
          title: '控制血糖',
          description: '体育活动帮助肌肉更有效地使用胰岛素，改善糖尿病管理'
        },
        {
          title: '维持健康体重',
          description: '运动增加能量消耗，帮助控制体重，减少肥胖相关疾病风险'
        },
        {
          title: '增强认知功能',
          description: '体育活动与改善记忆力、注意力和整体脑健康相关'
        },
        {
          title: '提升心理健康',
          description: '运动释放内啡肽，减轻压力和焦虑，改善整体情绪'
        }
];
const exerciseActivities = [
     {
          name: '散步',
          description: '低冲击有氧运动，适合几乎所有健康状况的人',
          image: '/images/default.jpg',
          intensity: '低至中等强度',
          frequency: '每天30-60分钟'
        },
        {
          name: '游泳',
          description: '全身运动，关节友好，特别适合关节炎患者',
          image: '/images/default.jpg',
          intensity: '低至高强度',
          frequency: '每周2-3次'
        },
        {
          name: '太极',
          description: '结合运动和冥想，改善平衡和心肺功能',
          image: '/images/default.jpg',
          intensity: '低强度',
          frequency: '每周2-3次'
        },
        {
          name: '骑自行车',
          description: '低冲击有氧运动，强化下肢肌肉',
          image: '/images/default.jpg',
          intensity: '中等强度',
          frequency: '每周3-5次'
        }
];
const sleepImpacts = [
     {
          disease: '糖尿病',
          effect: '睡眠不足会影响胰岛素敏感性，增加2型糖尿病风险'
        },
        {
          disease: '心血管疾病',
          effect: '慢性睡眠不足与高血压、心脏病和中风风险增加相关'
        },
        {
          disease: '肥胖',
          effect: '睡眠不足会影响调节饥饿的激素，增加食欲和体重增加风险'
        },
        {
          disease: '免疫功能',
          effect: '充足的睡眠对维持健康的免疫系统至关重要，有助于预防感染'
        }
];
const sleepTips = [  {
          title: '保持规律的睡眠时间表',
          description: '每天同一时间睡觉和起床，包括周末',
          type: 'primary',
          color: 'blue'
        },
        {
          title: '创造理想的睡眠环境',
          description: '保持卧室凉爽、安静和黑暗，使用舒适的床垫和枕头',
          type: 'info',
          color: 'indigo'
        },
        {
          title: '限制屏幕时间',
          description: '睡前至少一小时避免电子设备，减少蓝光暴露',
          type: 'warning',
          color: 'deep-purple'
        },
        {
          title: '避免睡前刺激物',
          description: '下午避免咖啡因，晚上避免大量进食和酒精',
          type: 'danger',
          color: 'purple'
        },
        {
          title: '建立放松的睡前习惯',
          description: '尝试冥想、深呼吸、阅读或温水浴',
          type: 'success',
          color: 'teal'
        }];
const stressEffects = [   {
          disease: '高血压',
          impact: '压力激素可导致血压升高，长期压力可能导致持续性高血压'
        },
        {
          disease: '心脏病',
          impact: '长期压力与炎症增加、血压升高和不健康的应对行为相关，这些都是心脏病的风险因素'
        },
        {
          disease: '糖尿病',
          impact: '压力激素会升高血糖，慢性压力会干扰胰岛素的产生和功能'
        },
        {
          disease: '免疫功能',
          impact: '长期压力会抑制免疫系统功能，增加感染和疾病的风险'
        },
        {
          disease: '消化系统问题',
          impact: '压力可加剧肠易激综合征、胃溃疡和其他消化系统问题的症状'
        }];
const stressTechniques = [ {
          name: '深呼吸练习',
          description: '慢深呼吸可激活副交感神经系统，减缓心率和降低血压',
          timeRequired: '5-10分钟'
        },
        {
          name: '渐进性肌肉放松',
          description: '依次绷紧和放松身体各组肌肉，减轻身体紧张',
          timeRequired: '15分钟'
        },
        {
          name: '正念冥想',
          description: '专注于当下，觉察但不评判自己的思想和感受',
          timeRequired: '10-20分钟'
        },
        {
          name: '身体活动',
          description: '定期运动释放内啡肽，自然缓解压力和提升情绪',
          timeRequired: '30分钟'
        },
        {
          name: '社交联系',
          description: '与朋友和家人交流可以提供情感支持，减轻压力',
          timeRequired: '不定'
        },
        {
          name: '写日记',
          description: '记录想法和感受有助于处理情绪和发现压力模式',
          timeRequired: '15分钟'
        }];
const habitBenefits = [   {
          title: '明确具体的目标',
          description: '设定明确、可测量的健康目标，如"每天步行30分钟"而不是"更多运动"'
        },
        {
          title: '创建提示和线索',
          description: '将新习惯与现有例行活动联系起来，如饭后散步或早餐前服药'
        },
        {
          title: '移除障碍',
          description: '识别并消除阻碍健康习惯的因素，如提前准备健康餐食'
        },
        {
          title: '保持问责性',
          description: '与朋友分享目标或使用应用程序跟踪进度，增强责任感'
        },
        {
          title: '庆祝进步',
          description: '认可并奖励自己的成就，无论大小，以保持动力'
        }];
const keyHabits = [   {
          name: '健康早餐',
          description: '每天吃营养丰富的早餐，包括蛋白质、全谷物和水果'
        },
        {
          name: '水分补充',
          description: '全天定期饮水，保持身体水分平衡'
        },
        {
          name: '活动提醒',
          description: '久坐时每小时起身活动几分钟'
        },
        {
          name: '服药提醒',
          description: '按时服用处方药，不要漏服'
        },
        {
          name: '血糖监测',
          description: '按医生建议定期检测血糖水平'
        },
        {
          name: '放松时间',
          description: '每天安排时间进行放松活动，如阅读或冥想'
        }
      ]
  


async function loadNutrition() {
  try {
    const res = await listNutrition();
    nutritionList.value = Array.isArray(res) ? res : [];
  } catch (e) {
    console.error('加载营养指南失败', e);
  }
}

watch(tab, (val) => {
  if (val === 'nutrition') loadNutrition();
});
onMounted(loadNutrition);
</script>

<style scoped>
.text-center { text-align: center; }
.text-h4 { font-size: 2.125rem; font-weight: 500; margin-bottom: 24px; }
.text-h5 { font-size: 1.5rem; font-weight: 500; margin-bottom: 16px; }
.mb-6 { margin-bottom: 24px; }
.mb-4 { margin-bottom: 16px; }
.mt-5 { margin-top: 20px; }
.my-6 { margin: 24px 0; }
.item-content { display: flex; align-items: center; gap: 8px; padding: 8px 0; }
.card-content { padding: 12px; }
</style>
