package com.example.chronicdisease.service.impl;

import com.example.chronicdisease.exception.ResourceNotFoundException;
import com.example.chronicdisease.model.User;
import com.example.chronicdisease.repository.UserRepository;
import com.example.chronicdisease.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    
    @Override
    public List<User> findAllNormalUsers() {
        // 从数据库中查找所有用户，并过滤出只具有ROLE_USER角色的用户
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().size() == 1 && user.getRoles().contains(User.Role.ROLE_USER))
                .collect(Collectors.toList());
    }
    
    @Override
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + id));
    }
    
    @Override
    public User updateUserStatus(String id, boolean active) {
        User user = findById(id);
        user.setActive(active);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

} 