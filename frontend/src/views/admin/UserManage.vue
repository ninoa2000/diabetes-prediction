<template>
  <el-container>
    <el-main>
      <el-card>
        <template #header>
          <div class="card-header">
            <span>User Management</span>
            <div class="search-box">
              <el-input
                v-model="search"
                placeholder="Search users..."
                style="width: 300px"
                class="mr-3"
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
              <el-button type="primary" @click="openAddDialog">
                <el-icon class="mr-1"><Plus /></el-icon>Add User
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
          <el-table-column prop="username" label="Username" min-width="150" show-overflow-tooltip></el-table-column>
          <el-table-column prop="fullName" label="Name" min-width="150" show-overflow-tooltip></el-table-column>
          <el-table-column prop="phone" label="Phone" min-width="150" show-overflow-tooltip></el-table-column>
          <el-table-column label="Bound Doctor" min-width="180">
            <template #default="scope">
              <el-tag v-if="scope.row.boundDoctorName" type="info" effect="plain">
                {{ scope.row.boundDoctorName }}
              </el-tag>
              <span v-else style="color: #909399; font-size: 12px;">Not Bound</span>
            </template>
          </el-table-column>
          <el-table-column prop="active" label="Status" width="120">
            <template #default="scope">
              <el-tag :type="scope.row.active ? 'success' : 'danger'">
                {{ scope.row.active ? 'Active' : 'Disabled' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="Actions" fixed="right" width="220">
            <template #default="scope">
              <el-button
                size="small"
                :type="scope.row.active ? 'danger' : 'success'"
                @click="toggleUserStatus(scope.row)"
                class="mr-2"
              >
                {{ scope.row.active ? 'Disable' : 'Enable' }}
              </el-button>
              <el-button
                size="small"
                type="danger"
                @click="confirmDelete(scope.row)"
              >
                Delete
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
            prev-text="Previous"
             next-text="Next"
            page-size-suffix="records/page"
            background
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
      
      <!-- Add/Edit User Dialog -->
      <el-dialog
        v-model="userDialog"
        :title="isEditing ? 'Edit User' : 'Add User'"
        width="600px"
      >
        <el-form
          ref="userFormRef"
          :model="userForm"
          :rules="userRules"
          label-width="120px"
        >
          <el-form-item label="Username" prop="username">
            <el-input v-model="userForm.username"></el-input>
          </el-form-item>
          <el-form-item label="Name" prop="fullName">
            <el-input v-model="userForm.fullName"></el-input>
          </el-form-item>
          <el-form-item label="Phone" prop="phone">
            <el-input v-model="userForm.phone"></el-input>
          </el-form-item>
          <el-form-item label="Password" prop="password">
            <el-input v-model="userForm.password" type="password" show-password></el-input>
          </el-form-item>
          <el-form-item label="Confirm Password" prop="confirmPassword">
            <el-input v-model="userForm.confirmPassword" type="password" show-password></el-input>
          </el-form-item>
          <el-form-item label="Status" prop="active">
            <el-switch
              v-model="userForm.active"
              active-text="Active"
              inactive-text="Disabled"
            ></el-switch>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="userDialog = false">Cancel</el-button>
            <el-button type="primary" @click="saveUser">Save</el-button>
          </div>
        </template>
      </el-dialog>
      
      <!-- Delete Confirmation Dialog -->
      <el-dialog
        v-model="deleteDialog"
        title="Delete User"
        width="400px"
      >
        <p>Are you sure you want to delete user "{{ selectedUser ? selectedUser.username : '' }}"? This action cannot be undone.</p>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="deleteDialog = false">Cancel</el-button>
            <el-button type="danger" @click="deleteUser">Delete</el-button>
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
        { required: true, message: 'Please enter username', trigger: 'blur' },
        { min: 3, max: 20, message: 'Length should be 3 to 20 characters', trigger: 'blur' }
      ],
      fullName: [
        { required: true, message: 'Please enter name', trigger: 'blur' }
      ],
      phone: [
        { required: true, message: 'Please enter phone number', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: 'Please enter a valid phone number', trigger: 'blur' }
      ],
      password: [
        { required: true, message: 'Please enter password', trigger: 'blur' },
        { min: 6, message: 'Password must be at least 6 characters', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: 'Please confirm your password', trigger: 'blur' },
        {
          validator: (rule, value, callback) => {
            if (value !== userForm.password) {
              callback(new Error('Passwords do not match'));
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
    
    // Get all regular users
    const fetchUsers = async () => {
      try {
        loading.value = true;
        const response = await adminService.getAllUsers();
        users.value = response.data;
      } catch (error) {
        ElMessage.error(error.response?.data?.message || 'Failed to fetch user list');
        console.error('Fetch users error:', error);
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
            ElMessage.success('User created successfully');
            userDialog.value = false;
            fetchUsers(); // Reload user list
          } catch (error) {
            ElMessage.error(error.response?.data?.message || 'Failed to create user');
            console.error('Create user error:', error);
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
        ElMessage.success('User deleted successfully');
        deleteDialog.value = false;
        fetchUsers(); // Reload user list
      } catch (error) {
        ElMessage.error(error.response?.data?.message || 'Failed to delete user');
        console.error('Delete user error:', error);
      }
    };
    
    const toggleUserStatus = (user) => {
      ElMessageBox.confirm(
        `Are you sure you want to ${user.active ? 'disable' : 'enable'} user "${user.username}"?`,
        user.active ? 'Disable User' : 'Enable User',
        {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: user.active ? 'warning' : 'info'
        }
      ).then(async () => {
        try {
          await adminService.updateUserStatus(user.id, !user.active);
          ElMessage.success(`User ${!user.active ? 'enabled' : 'disabled'} successfully`);
          fetchUsers(); // Reload user list
        } catch (error) {
          ElMessage.error(error.response?.data?.message || `Failed to update user status`);
          console.error('Update user status error:', error);
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
    
    // Fetch user list on mount
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