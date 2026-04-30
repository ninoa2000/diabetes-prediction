import { createRouter, createWebHashHistory } from 'vue-router';

// Layout components
const Layout = () => import('@/views/layout/Layout.vue');

// Route components
const Login = () => import('@/views/login/Login.vue');
const Register = () => import('@/views/login/Register.vue');
const Home = () => import('@/views/home/Home.vue');
const DoctorHome = () => import('@/views/doctor/DoctorHome.vue');
const PredictionForm = () => import('@/views/prediction/PredictionForm.vue');
const PredictionResult = () => import('@/views/prediction/PredictionResult.vue');
const DoctorBinding = () => import('@/views/user/DoctorBinding.vue');
const Message = () => import('@/views/message/Message.vue');
const UserProfile = () => import('@/views/user/UserProfile.vue');
const ChangePassword = () => import('@/views/user/ChangePassword.vue');
const AdminDoctorManage = () => import('@/views/admin/DoctorManage.vue');
const AdminUserManage = () => import('@/views/admin/UserManage.vue');
const DoctorPatient = () => import('@/views/doctor/PatientList.vue');
const DoctorMessage = () => import('@/views/doctor/MessageList.vue');
const ChronicDiseaseInfo = () => import('@/views/education/DiseaseInfo.vue');
const HealthGuide = () => import('@/views/education/HealthGuide.vue');
const LatestResearch = () => import('@/views/education/LatestResearch.vue');
const SeasonalTips = () => import('@/views/education/SeasonalTips.vue');
const NotFound = () => import('@/views/error/NotFound.vue');
const KnowledgeManage=() => import('@/views/admin/KnowledgeManage.vue');

// 角色常量定义
const ROLE_ADMIN = 'ROLE_ADMIN';
const ROLE_DOCTOR = 'ROLE_DOCTOR';
const ROLE_USER = 'ROLE_USER';

// Routes configuration
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresAuth: false, title: '登录' },
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: { requiresAuth: false, title: '注册' },
  },
  {
    path: '/',
    component: Layout,
    redirect: (to) => {
      const userRole = localStorage.getItem('userRole');
      if (userRole === ROLE_ADMIN) {
        return '/admin/doctors';
      } else if (userRole === ROLE_DOCTOR) {
        return '/doctor/home';
      } else {
        return '/home';
      }
    },
    children: [
      {
        path: 'home',
        name: 'Home',
        component: Home,
        meta: { requiresAuth: true, title: '首页', role: [ROLE_USER] },
      },
      {
        path: 'doctor/home',
        name: 'DoctorHome',
        component: DoctorHome,
        meta: { requiresAuth: true, title: '首页', role: [ROLE_DOCTOR] },
      },
      {
        path: 'prediction',
        name: 'Prediction',
        component: PredictionForm,
        meta: { requiresAuth: true, title: '健康预测', role: [ROLE_USER] },
      },
      {
        path: 'prediction/result',
        name: 'PredictionResult',
        component: PredictionResult,
        meta: { requiresAuth: true, title: '预测结果', role: [ROLE_USER] },
      },
      {
        path: 'doctor-binding',
        name: 'DoctorBinding',
        component: DoctorBinding,
        meta: { requiresAuth: true, title: '绑定医生', role: [ROLE_USER] },
      },
      {
        path: 'message',
        name: 'Message',
        component: Message,
        meta: { requiresAuth: true, title: '我的留言', role: [ROLE_USER] },
      },
      {
        path: 'profile',
        name: 'UserProfile',
        component: UserProfile,
        meta: { requiresAuth: true, title: '个人信息', role: [ROLE_ADMIN, ROLE_DOCTOR, ROLE_USER] },
      },
      {
         path: 'user/change-password',
        name: 'ChangePassword',
        component: ChangePassword,
        meta: { requiresAuth: true, title: '修改密码', role: [ROLE_USER, ROLE_DOCTOR] },
      },
      // Admin routes
      {
        path: 'admin/doctors',
        name: 'AdminDoctorManage',
        component: AdminDoctorManage,
        meta: { requiresAuth: true, title: '医生管理', role: [ROLE_ADMIN] },
      },
      {
        path: 'admin/users',
        name: 'AdminUserManage',
        component: AdminUserManage,
        meta: { requiresAuth: true, title: '用户管理', role: [ROLE_ADMIN] },
      },
      {
        path: 'admin/KnowledgeManage',
        name: 'KnowledgeManage',
        component: KnowledgeManage,
        meta: { requiresAuth: false, title: '健康科普知识管理', role: [ROLE_ADMIN ] },
      },
      // Doctor routes
      {
        path: 'doctor/patients',
        name: 'DoctorPatient',
        component: DoctorPatient,
        meta: { requiresAuth: true, title: '我的患者', role: [ROLE_DOCTOR] },
      },
      {
        path: 'doctor/messages',
        name: 'DoctorMessage',
        component: DoctorMessage,
        meta: { requiresAuth: true, title: '患者留言', role: [ROLE_DOCTOR] },
      },
      {
        path: 'doctor/patients/:patientId/cases',
        name: 'PatientCases',
        component: () => import('@/views/doctor/PatientCases.vue'),
        meta: { requiresAuth: true, title: '患者病例', role: [ROLE_DOCTOR] }
      },
      
      // Education routes
      {
        path: 'education/disease-info',
        name: 'ChronicDiseaseInfo',
        component: ChronicDiseaseInfo,
        meta: { requiresAuth: false, title: '糖尿病知识', role: [ROLE_ADMIN, ROLE_DOCTOR, ROLE_USER] },
      },
      {
        path: 'education/health-guide',
        name: 'HealthGuide',
        component: HealthGuide,
        meta: { requiresAuth: false, title: '健康生活指南', role: [ROLE_ADMIN, ROLE_DOCTOR, ROLE_USER] },
      },
      {
        path: 'education/latest-research',
        name: 'LatestResearch',
        component: LatestResearch,
        meta: { requiresAuth: false, title: '最新研究资讯', role: [ROLE_ADMIN, ROLE_DOCTOR, ROLE_USER] },
      },
      {
        path: 'education/seasonal-tips',
        name: 'SeasonalTips',
        component: SeasonalTips,
        meta: { requiresAuth: false, title: '健康爱好建议', role: [ROLE_ADMIN, ROLE_DOCTOR, ROLE_USER] },
      },
    ],
  },
  {
    path: '/:catchAll(.*)',
    name: 'NotFound',
    component: NotFound,
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

// Navigation guard
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  const userRole = localStorage.getItem('userRole');
  
  console.log('当前用户角色:', userRole);
  console.log('目标路由需要的角色:', to.meta.role);
  
  // Set page title
  if (to.meta.title) {
    document.title = `${to.meta.title} - 糖尿病预测系统`;
  }
  
  // Check auth requirements
  if (to.meta.requiresAuth && !token) {
    console.log('需要认证但没有token，重定向到登录页');
    next({ name: 'Login' });
  } 
  // Check role requirements if specified
  else if (to.meta.role && token && userRole) {
    if (to.meta.role.includes(userRole)) {
      console.log('用户有权访问该路由');
      next();
    } else {
      console.log('用户无权访问该路由，重定向到对应首页');
      if (userRole === ROLE_ADMIN) {
        next({ path: '/admin/doctors' });
      } else if (userRole === ROLE_DOCTOR) {
        next({ path: '/doctor/home' });
      } else {
        next({ path: '/home' });
      }
    }
  } else {
    next();
  }
});

export default router; 