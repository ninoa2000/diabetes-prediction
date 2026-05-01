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
    meta: { requiresAuth: false, title: 'Login' },
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: { requiresAuth: false, title: 'Register' },
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
        meta: { requiresAuth: true, title: 'Home', role: [ROLE_USER] },
      },
      {
        path: 'doctor/home',
        name: 'DoctorHome',
        component: DoctorHome,
        meta: { requiresAuth: true, title: 'Home', role: [ROLE_DOCTOR] },
      },
      {
        path: 'prediction',
        name: 'Prediction',
        component: PredictionForm,
        meta: { requiresAuth: true, title: 'Health Prediction', role: [ROLE_USER] },
      },
      {
        path: 'prediction/result',
        name: 'PredictionResult',
        component: PredictionResult,
        meta: { requiresAuth: true, title: 'Prediction Result', role: [ROLE_USER] },
      },
      {
        path: 'doctor-binding',
        name: 'DoctorBinding',
        component: DoctorBinding,
        meta: { requiresAuth: true, title: 'Bind Doctor', role: [ROLE_USER] },
      },
      {
        path: 'message',
        name: 'Message',
        component: Message,
        meta: { requiresAuth: true, title: 'My Messages', role: [ROLE_USER] },
      },
      {
        path: 'profile',
        name: 'UserProfile',
        component: UserProfile,
        meta: { requiresAuth: true, title: 'Profile', role: [ROLE_ADMIN, ROLE_DOCTOR, ROLE_USER] },
      },
      {
         path: 'user/change-password',
        name: 'ChangePassword',
        component: ChangePassword,
        meta: { requiresAuth: true, title: 'Change Password', role: [ROLE_USER, ROLE_DOCTOR] },
      },
      // Admin routes
      {
        path: 'admin/doctors',
        name: 'AdminDoctorManage',
        component: AdminDoctorManage,
        meta: { requiresAuth: true, title: 'Doctor Management', role: [ROLE_ADMIN] },
      },
      {
        path: 'admin/users',
        name: 'AdminUserManage',
        component: AdminUserManage,
        meta: { requiresAuth: true, title: 'User Management', role: [ROLE_ADMIN] },
      },
      {
        path: 'admin/KnowledgeManage',
        name: 'KnowledgeManage',
        component: KnowledgeManage,
        meta: { requiresAuth: false, title: 'Knowledge Management', role: [ROLE_ADMIN ] },
      },
      // Doctor routes
      {
        path: 'doctor/patients',
        name: 'DoctorPatient',
        component: DoctorPatient,
        meta: { requiresAuth: true, title: 'My Patients', role: [ROLE_DOCTOR] },
      },
      {
        path: 'doctor/messages',
        name: 'DoctorMessage',
        component: DoctorMessage,
        meta: { requiresAuth: true, title: 'Patient Messages', role: [ROLE_DOCTOR] },
      },
      {
        path: 'doctor/patients/:patientId/cases',
        name: 'PatientCases',
        component: () => import('@/views/doctor/PatientCases.vue'),
        meta: { requiresAuth: true, title: 'Patient Cases', role: [ROLE_DOCTOR] }
      },
      
      // Education routes
      {
        path: 'education/disease-info',
        name: 'ChronicDiseaseInfo',
        component: ChronicDiseaseInfo,
        meta: { requiresAuth: false, title: 'Diabetes Info', role: [ROLE_ADMIN, ROLE_DOCTOR, ROLE_USER] },
      },
      {
        path: 'education/health-guide',
        name: 'HealthGuide',
        component: HealthGuide,
        meta: { requiresAuth: false, title: 'Health Guide', role: [ROLE_ADMIN, ROLE_DOCTOR, ROLE_USER] },
      },
      {
        path: 'education/latest-research',
        name: 'LatestResearch',
        component: LatestResearch,
        meta: { requiresAuth: false, title: 'Latest Research', role: [ROLE_ADMIN, ROLE_DOCTOR, ROLE_USER] },
      },
      {
        path: 'education/seasonal-tips',
        name: 'SeasonalTips',
        component: SeasonalTips,
        meta: { requiresAuth: false, title: 'Health Tips', role: [ROLE_ADMIN, ROLE_DOCTOR, ROLE_USER] },
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
    document.title = `${to.meta.title} - Diabetes Prediction System`;
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