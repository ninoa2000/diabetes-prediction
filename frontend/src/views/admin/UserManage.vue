<template>
  <el-container>
    <el-main>
      <el-card>
        <template #header>
          <div class="card-header">
            <span>用户管理</span>
            <div class="search-box">
              <el-input
                v-model="search"
                placeholder="搜索用户..."
                style="width: 300px"
                class="mr-3"
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
              <el-button type="primary" @click="openAddDialog">
                <el-icon class="mr-1"><Plus /></el-icon>添加用户
              </el-button>
            </div>
          </div>
        </template>
        
        <el-table
          :data="paginatedUsers"
          border
          style="width: 100%"
          v-loading="loading"
        >
          <el-table-column prop="username" label="用户名" min-width="25%"></el-table-column>
          <el-table-column prop="fullName" label="姓名" min-width="25%"></el-table-column>
          <el-table-column prop="phone" label="电话" min-width="25%"></el-table-column>
          <el-table-column prop="active" label="状态" width="120">
            <template #default="scope">
              <el-tag :type="scope.row.active ? 'success' : 'danger'">
                {{ scope.row.active ? '正常' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="220">
            <template #default="scope">
              <el-button
                size="small"
                :type="scope.row.active ? 'danger' : 'success'"
                @click="toggleUserStatus(scope.row)"
                class="mr-2"
              >
                {{ scope.row.active ? '禁用' : '启用' }}
              </el-button>
              <el-button
                size="small"
                type="danger"
                @click="confirmDelete(scope.row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="filteredUsers.length"
            layout="total, sizes, prev, pager, next, jumper"
            prev-text="上一页"
             next-text="下一页"
            page-size-suffix="条/页"
            background
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
      
      <!-- 添加/编辑用户对话框 -->
      <el-dialog
        v-model="userDialog"
        :title="isEditing ? '编辑用户' : '添加用户'"
        width="600px"
      >
        <el-form
          ref="userFormRef"
          :model="userForm"
          :rules="userRules"
          label-width="100px"
        >
          <el-form-item label="用户名" prop="username">
            <el-input v-model="userForm.username"></el-input>
          </el-form-item>
          <el-form-item label="姓名" prop="fullName">
            <el-input v-model="userForm.fullName"></el-input>
          </el-form-item>
          <el-form-item label="电话" prop="phone">
            <el-input v-model="userForm.phone"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="userForm.password" type="password" show-password></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="userForm.confirmPassword" type="password" show-password></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="active">
            <el-switch
              v-model="userForm.active"
              active-text="正常"
              inactive-text="禁用"
            ></el-switch>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="userDialog = false">取消</el-button>
            <el-button type="primary" @click="saveUser">保存</el-button>
          </div>
        </template>
      </el-dialog>
      
      <!-- 删除确认对话框 -->
      <el-dialog
        v-model="deleteDialog"
        title="删除用户"
        width="400px"
      >
        <p>确定要删除用户 "{{ selectedUser ? selectedUser.username : '' }}" 吗？此操作不可恢复。</p>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="deleteDialog = false">取消</el-button>
            <el-button type="danger" @click="deleteUser">删除</el-button>
          </div>
        </template>
      </el-dialog>
    </el-main>
  </el-container>
</template>

<script>
import { ref, computed, reactive, onMounted } from 'vue';
import { Search, Plus } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { adminService } from '@/api/admin';

export default {
  name: 'UserManage',
  components: {
    Search,
    Plus
  },
  setup() {
    const search = ref('');
    const currentPage = ref(1);
    const pageSize = ref(10);
    const userDialog = ref(false);
    const deleteDialog = ref(false);
    const isEditing = ref(false);
    const selectedUser = ref(null);
    const userFormRef = ref(null);
    const loading = ref(false);
    const users = ref([]);
    
    const userForm = reactive({
      username: '',
      fullName: '',
      phone: '',
      password: '',
      confirmPassword: '',
      active: true
    });
    
    const userRules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
      ],
      fullName: [
        { required: true, message: '请输入姓名', trigger: 'blur' }
      ],
      phone: [
        { required: true, message: '请输入电话号码', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, message: '密码长度不能小于6个字符', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请再次输入密码', trigger: 'blur' },
        {
          validator: (rule, value, callback) => {
            if (value !== userForm.password) {
              callback(new Error('两次输入密码不一致'));
            } else {
              callback();
            }
          },
          trigger: 'blur'
        }
      ]
    };
    
    const filteredUsers = computed(() => {
      let result = users.value;
      
      if (search.value) {
        const searchLower = search.value.toLowerCase();
        result = result.filter(user => 
          user.username.toLowerCase().includes(searchLower) ||
          (user.email && user.email.toLowerCase().includes(searchLower)) ||
          user.fullName.toLowerCase().includes(searchLower) ||
          (user.phone && user.phone.includes(search.value))
        );
      }
      
      return result;
    });
    
    const paginatedUsers = computed(() => {
      const start = (currentPage.value - 1) * pageSize.value;
      const end = start + pageSize.value;
      return filteredUsers.value.slice(start, end);
    });
    
    // 获取所有普通用户
    const fetchUsers = async () => {
      try {
        loading.value = true;
        const response = await adminService.getAllUsers();
        users.value = response.data;
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '获取用户列表失败');
        console.error('获取用户列表错误:', error);
      } finally {
        loading.value = false;
      }
    };
    
    const resetUserForm = () => {
      if (userFormRef.value) {
        userFormRef.value.resetFields();
      }
      Object.assign(userForm, {
        username: '',
        fullName: '',
        phone: '',
        password: '',
        confirmPassword: '',
        active: true
      });
    };
    
    const openAddDialog = () => {
      resetUserForm();
      isEditing.value = false;
      userDialog.value = true;
    };
    
    const saveUser = () => {
      userFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            const userData = {
              username: userForm.username,
              fullName: userForm.fullName,
              phone: userForm.phone,
              password: userForm.password,
              active: userForm.active
            };
            
            await adminService.createUser(userData);
            ElMessage.success('用户创建成功');
            userDialog.value = false;
            fetchUsers(); // 重新加载用户列表
          } catch (error) {
            ElMessage.error(error.response?.data?.message || '创建用户失败');
            console.error('创建用户错误:', error);
          }
        }
      });
    };
    
    const confirmDelete = (user) => {
      selectedUser.value = user;
      deleteDialog.value = true;
    };
    
    const deleteUser = async () => {
      try {
        await adminService.deleteUser(selectedUser.value.id);
        ElMessage.success('用户删除成功');
        deleteDialog.value = false;
        fetchUsers(); // 重新加载用户列表
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '删除用户失败');
        console.error('删除用户错误:', error);
      }
    };
    
    const toggleUserStatus = (user) => {
      ElMessageBox.confirm(
        `确定要${user.active ? '禁用' : '启用'}用户 "${user.username}" 吗？`,
        user.active ? '禁用用户' : '启用用户',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: user.active ? 'warning' : 'info'
        }
      ).then(async () => {
        try {
          await adminService.updateUserStatus(user.id, !user.active);
          ElMessage.success(`用户${!user.active ? '启用' : '禁用'}成功`);
          fetchUsers(); // 重新加载用户列表
        } catch (error) {
          ElMessage.error(error.response?.data?.message || `更新用户状态失败`);
          console.error('更新用户状态错误:', error);
        }
      }).catch(() => {});
    };
    
    const handleSizeChange = (size) => {
      pageSize.value = size;
      currentPage.value = 1;
    };
    
    const handleCurrentChange = (page) => {
      currentPage.value = page;
    };
    
    // 组件挂载时获取用户列表
    onMounted(() => {
      fetchUsers();
    });
    
    return {
      search,
      currentPage,
      pageSize,
      userDialog,
      deleteDialog,
      isEditing,
      selectedUser,
      userForm,
      userFormRef,
      userRules,
      users,
      filteredUsers,
      paginatedUsers,
      loading,
      openAddDialog,
      saveUser,
      confirmDelete,
      deleteUser,
      toggleUserStatus,
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

.search-box {
  display: flex;
  align-items: center;
}

.mr-1 {
  margin-right: 4px;
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