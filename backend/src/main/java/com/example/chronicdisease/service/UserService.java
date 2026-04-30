package com.example.chronicdisease.service;

import com.example.chronicdisease.dto.user.ChangePasswordRequest;
import com.example.chronicdisease.model.User;

import java.util.List;

public interface UserService {
    
    /**
     * 查找所有普通用户（非医生、非管理员）
     */
    List<User> findAllNormalUsers();
    
    /**
     * 根据ID查找用户
     */
    User findById(String id);
    
    /**
     * 更新用户状态
     */
    User updateUserStatus(String id, boolean active);

} 