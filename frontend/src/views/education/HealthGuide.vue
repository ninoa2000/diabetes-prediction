<template>
  <el-container>
    <el-main>
      <h1 class="text-h4 mb-6">Health Guide</h1>

      <el-row :gutter="20">
        <el-col :xs="24" :md="16">
          <p class="text-body-1 mb-4">
            A healthy lifestyle is key to preventing and managing chronic diseases. The following guide contains practical advice on nutrition, physical activity, sleep, and stress management to help you improve your health and reduce the risk of chronic diseases.
          </p>
        </el-col>
        <el-col :xs="24" :md="8">
          <el-image
            src="https://images.unsplash.com/photo-1490645935967-10de6ba17061?auto=format&fit=crop&w=800&q=80"
            style="height: 200px; width: 100%"
            fit="cover"
            class="rounded-lg shadow-sm"
          ></el-image>
        </el-col>
      </el-row>

      <el-divider class="my-6"></el-divider>

      <el-tabs v-model="tab" type="card" class="text-center">
        <el-tab-pane label="Nutrition" name="nutrition" />
        <el-tab-pane label="Physical Activity" name="exercise" />
        <el-tab-pane label="Sleep Health" name="sleep" />
        <el-tab-pane label="Stress Management" name="stress" />
        <el-tab-pane label="Healthy Habits" name="habits" />
        <el-tab-pane label="Health Tips" name="tips" />
      </el-tabs>

      <div class="mt-5">
        <!-- 1. 营养饮食 -->
        <div v-if="tab === 'nutrition'">
          <el-row :gutter="20">
            <el-col :xs="24" :md="12">
              <h2 class="text-h5 mb-4">Principles of Balanced Diet</h2>
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
              <h2 class="text-h5 mb-4">Nutritional Intake Recommendations</h2>
              <el-card>
                <div class="card-content">
                  <el-table :data="nutritionList" border style="width:100%">
                    <el-table-column prop="title" label="Title" />
                    <el-table-column prop="category" label="Category" />
                    <el-table-column prop="content" label="Description" />
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
              <h2 class="text-h5 mb-4">Benefits of Physical Activity</h2>
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
              <h2 class="text-h5 mb-4">Activities Suitable for Diabetic Patients</h2>
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
              <h2 class="text-h5 mb-4">Importance of Good Sleep</h2>
              <p class="text-body-1 mb-3">
                Sufficient high-quality sleep is crucial for physical health. Lack of sleep increases the risk of chronic diseases, including obesity, diabetes, heart disease, and hypertension.
                Scientific research shows that adults need 7-9 hours of sleep per night to maintain optimal health.
              </p>
              <h3 class="text-subtitle-1 mt-4 mb-2">Impact of Sleep on Chronic Diseases</h3>
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
              <h2 class="text-h5 mb-4">Strategies for Improving Sleep</h2>
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
              <h2 class="text-h5 mb-4">Health Impact of Chronic Stress</h2>
              <p class="text-body-1 mb-4">
                Long-term stress keeps the body in a constant "fight or flight" mode, releasing harmful hormones and increasing the risk of chronic diseases.
                Effective stress management is an important component of preventing and controlling chronic diseases.
              </p>
              <el-card class="mb-4">
                <template #header>
                  <div class="card-header bg-red-lighten-4">
                    <el-icon class="mr-2"><StarFilled /></el-icon>
                    Relationship between Stress and Chronic Diseases
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
              <h2 class="text-h5 mb-4">Stress Management Techniques</h2>
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
              <h2 class="text-h5 mb-4">Steps to Develop Healthy Habits</h2>
              <el-steps direction="vertical" :active="4">
                <el-step title="Set Goals" description="Set specific, measurable, achievable, relevant, and time-bound health goals" />
                <el-step title="Start Small" description="Start with small, manageable changes and gradually build new habits" />
                <el-step title="Track Progress" description="Use journals, apps, or other tools to record and monitor your progress" />
                <el-step title="Persist" description="Developing a new habit usually takes at least 66 days, maintain patience and consistency" />
              </el-steps>
              <el-card class="mt-4 mb-4">
                <template #header>
                  <div class="card-header bg-green-lighten-4">
                    <el-icon class="mr-2"><SuccessFilled /></el-icon>
                    Long-term Benefits of Healthy Habits
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
              <h2 class="text-h5 mb-4">Key Healthy Habits</h2>
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

        <!-- 6. 健康小贴士 (Health Tips) -->
        <div v-if="tab === 'tips'">
          <el-row :gutter="20">
            <el-col :xs="24" :md="12">
              <h2 class="text-h5 mb-4">Principles of Daily Care</h2>
              <el-card>
                <el-collapse>
                  <el-collapse-item
                    v-for="(item, i) in tipsPrinciples"
                    :key="i"
                    :title="item.title"
                  >
                    <div class="item-content">
                      <el-avatar :size="36" class="primary-bg">
                        <el-icon><Check /></el-icon>
                      </el-avatar>
                      <span>{{ item.description }}</span>
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </el-card>
            </el-col>
            <el-col :xs="24" :md="12">
              <h2 class="text-h5 mb-4">Practical Health Advice</h2>
              <el-card>
                <div class="card-content">
                  <el-table :data="habitsList" border style="width:100%">
                    <el-table-column prop="title" label="Title" />
                    <el-table-column prop="category" label="Category" width="120" />
                    <el-table-column prop="content" label="Tip Content" />
                  </el-table>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </div>
    </el-main>
  </el-container>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { listNutrition, listHabit } from '../../api/Knowledge';
import { Check, ArrowRight, SuccessFilled, StarFilled, Moon } from '@element-plus/icons-vue';

// 导入静态数据
const tab = ref('nutrition');
const nutritionList = ref([]);
const habitsList = ref([]);

const tipsPrinciples = [
  { title: 'Consistency is Key', description: 'Small, consistent daily actions lead to the most significant long-term health benefits.' },
  { title: 'Listen to Your Body', description: 'Pay attention to physical signals. Rest when tired, and eat when genuinely hungry.' },
  { title: 'Stay Positive', description: 'A positive mindset can reduce stress hormones and improve your immune system function.' },
  { title: 'Regular Check-ups', description: 'Do not wait for symptoms. Regular screenings help catch potential issues early.' }
];

const nutritionPrinciples = [
  { title: 'Diversified Diet', description: 'Eat a variety of foods every day, including vegetables, fruits, whole grains, protein, and healthy fats', icon: Check },
  { title: 'Portion Control', description: 'Be mindful of food portions to avoid overeating, especially high-calorie foods', icon: ArrowRight },
  { title: 'Increase Dietary Fiber', description: 'Choose foods rich in dietary fiber, such as beans, whole grains, vegetables, and fruits', icon: SuccessFilled },
  { title: 'Reduce Saturated Fat', description: 'Limit intake of saturated and trans fats, choose healthy fats like olive oil and nuts', icon: StarFilled },
  { title: 'Limit Salt and Sugar', description: 'Reduce intake of added sugars and salt, choose naturally flavored foods', icon: Moon }
];

const exerciseBenefits = [
    {
          title: 'Improve Cardiovascular Health',
          description: 'Regular exercise can lower blood pressure, improve heart function, and reduce the risk of heart disease and stroke'
        },
        {
          title: 'Control Blood Sugar',
          description: 'Physical activity helps muscles use insulin more effectively and improves diabetes management'
        },
        {
          title: 'Maintain Healthy Weight',
          description: 'Exercise increases energy expenditure, helps control weight, and reduces the risk of obesity-related diseases'
        },
        {
          title: 'Enhance Cognitive Function',
          description: 'Physical activity is associated with improved memory, attention, and overall brain health'
        },
        {
          title: 'Boost Mental Health',
          description: 'Exercise releases endorphins, reduces stress and anxiety, and improves overall mood'
        }
];
const exerciseActivities = [
     {
          name: 'Walking',
          description: 'Low-impact aerobic exercise, suitable for almost everyone of all health conditions',
          image: '/images/default.jpg',
          intensity: 'Low to moderate intensity',
          frequency: '30-60 minutes daily'
        },
        {
          name: 'Swimming',
          description: 'Full-body workout, joint-friendly, especially suitable for patients with arthritis',
          image: '/images/default.jpg',
          intensity: 'Low to high intensity',
          frequency: '2-3 times per week'
        },
        {
          name: 'Tai Chi',
          description: 'Combines movement and meditation, improves balance and cardiopulmonary function',
          image: '/images/default.jpg',
          intensity: 'Low intensity',
          frequency: '2-3 times per week'
        },
        {
          name: 'Cycling',
          description: 'Low-impact aerobic exercise, strengthens lower limb muscles',
          image: '/images/default.jpg',
          intensity: 'Moderate intensity',
          frequency: '3-5 times per week'
        }
];
const sleepImpacts = [
     {
          disease: 'Diabetes',
          effect: 'Lack of sleep affects insulin sensitivity and increases the risk of type 2 diabetes'
        },
        {
          disease: 'Cardiovascular Disease',
          effect: 'Chronic lack of sleep is associated with an increased risk of hypertension, heart disease, and stroke'
        },
        {
          disease: 'Obesity',
          effect: 'Lack of sleep affects hormones that regulate hunger, increasing appetite and the risk of weight gain'
        },
        {
          disease: 'Immune Function',
          effect: 'Adequate sleep is crucial for maintaining a healthy immune system and helps prevent infection'
        }
];
const sleepTips = [  {
          title: 'Maintain a Regular Sleep Schedule',
          description: 'Go to bed and wake up at the same time every day, including weekends',
          type: 'primary',
          color: 'blue'
        },
        {
          title: 'Create an Ideal Sleep Environment',
          description: 'Keep the bedroom cool, quiet, and dark, use a comfortable mattress and pillows',
          type: 'info',
          color: 'indigo'
        },
        {
          title: 'Limit Screen Time',
          description: 'Avoid electronic devices at least one hour before bed to reduce blue light exposure',
          type: 'warning',
          color: 'deep-purple'
        },
        {
          title: 'Avoid Stimulants Before Bed',
          description: 'Avoid caffeine in the afternoon and large meals and alcohol in the evening',
          type: 'danger',
          color: 'purple'
        },
        {
          title: 'Establish a Relaxing Bedtime Routine',
          description: 'Try meditation, deep breathing, reading, or a warm bath',
          type: 'success',
          color: 'teal'
        }];
const stressEffects = [   {
          disease: 'Hypertension',
          impact: 'Stress hormones can cause blood pressure to rise, and long-term stress may lead to persistent hypertension'
        },
        {
          disease: 'Heart Disease',
          impact: 'Long-term stress is associated with increased inflammation, higher blood pressure, and unhealthy coping behaviors, which are all risk factors for heart disease'
        },
        {
          disease: 'Diabetes',
          impact: 'Stress hormones can raise blood sugar, and chronic stress can interfere with insulin production and function'
        },
        {
          disease: 'Immune Function',
          impact: 'Long-term stress suppresses immune system function, increasing the risk of infection and disease'
        },
        {
          disease: 'Digestive Issues',
          impact: 'Stress can exacerbate symptoms of irritable bowel syndrome, gastric ulcers, and other digestive problems'
        }];
const stressTechniques = [ {
          name: 'Deep Breathing Exercises',
          description: 'Slow deep breathing can activate the parasympathetic nervous system, slow heart rate, and lower blood pressure',
          timeRequired: '5-10 minutes'
        },
        {
          name: 'Progressive Muscle Relaxation',
          description: 'Sequentially tighten and relax each muscle group in the body to reduce physical tension',
          timeRequired: '15 minutes'
        },
        {
          name: 'Mindfulness Meditation',
          description: 'Focus on the present moment, notice but do not judge your thoughts and feelings',
          timeRequired: '10-20 minutes'
        },
        {
          name: 'Physical Activity',
          description: 'Regular exercise releases endorphins, naturally relieving stress and boosting mood',
          timeRequired: '30 minutes'
        },
        {
          name: 'Social Connection',
          description: 'Talking with friends and family can provide emotional support and reduce stress',
          timeRequired: 'Varies'
        },
        {
          name: 'Journaling',
          description: 'Recording thoughts and feelings helps process emotions and identify stress patterns',
          timeRequired: '15 minutes'
        }];
const habitBenefits = [   {
          title: 'Clear and Specific Goals',
          description: 'Set clear, measurable health goals, such as "walk for 30 minutes every day" instead of "exercise more"'
        },
        {
          title: 'Create Cues and Signs',
          description: 'Link new habits to existing routines, such as walking after a meal or taking medicine before breakfast'
        },
        {
          title: 'Remove Barriers',
          description: 'Identify and eliminate factors that hinder healthy habits, such as preparing healthy meals in advance'
        },
        {
          title: 'Maintain Accountability',
          description: 'Share goals with friends or use an app to track progress, increasing accountability'
        },
        {
          title: 'Celebrate Progress',
          description: 'Recognize and reward your achievements, no matter how small, to maintain motivation'
        }];
const keyHabits = [   {
          name: 'Healthy Breakfast',
          description: 'Eat a nutritious breakfast every day, including protein, whole grains, and fruit'
        },
        {
          name: 'Hydration',
          description: 'Drink water regularly throughout the day to maintain body fluid balance'
        },
        {
          name: 'Activity Reminders',
          description: 'Get up and move for a few minutes every hour when sitting for long periods'
        },
        {
          name: 'Medication Reminders',
          description: 'Take prescription medications on time, do not skip doses'
        },
        {
          name: 'Blood Sugar Monitoring',
          description: 'Monitor blood sugar levels regularly as recommended by your doctor'
        },
        {
          name: 'Relaxation Time',
          description: 'Schedule time every day for relaxation activities, such as reading or meditation'
        }
      ]
  


async function loadNutrition() {
  try {
    const res = await listNutrition();
    nutritionList.value = Array.isArray(res) ? res : [];
  } catch (e) {
    console.error('Failed to load nutrition guide', e);
  }
}

async function loadHabits() {
  try {
    const res = await listHabit();
    habitsList.value = Array.isArray(res) ? res : [];
  } catch (e) {
    console.error('Failed to load health tips', e);
  }
}

watch(tab, (val) => {
  if (val === 'nutrition') loadNutrition();
  if (val === 'tips') loadHabits();
});
onMounted(() => {
  loadNutrition();
});
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
