package com.example.chronicdisease.controller;

import com.example.chronicdisease.dto.user.UserResponse;
import com.example.chronicdisease.dto.user.UpdateProfileRequest;
import com.example.chronicdisease.model.User;
import com.example.chronicdisease.repository.UserRepository;
import com.example.chronicdisease.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 获取所有用户（分页）
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction) {

        // 如果没有分页参数，返回所有用户（保持向后兼容）
        if (page == null && size == null) {
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        }

        // 使用分页参数
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 10;
        String sortField = sortBy != null ? sortBy : "createdAt";
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortField));
        Page<User> userPage = userRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("users", userPage.getContent());
        response.put("currentPage", userPage.getNumber());
        response.put("totalItems", userPage.getTotalElements());
        response.put("totalPages", userPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(HttpServletRequest request) {
        // 获取当前用户ID
        String currentUserId = null;

        // 1. 尝试从SecurityContext获取
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("SecurityContext authentication: {}", authentication);
        if (authentication != null && !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
            currentUserId = authentication.getName();
            log.debug("Got userId from SecurityContext: {}", currentUserId);
        }

        // 2. 如果从SecurityContext获取失败，从请求头获取
        if (currentUserId == null || currentUserId.isEmpty()) {
            currentUserId = request.getHeader("X-User-ID");
            log.debug("Got userId from request header: {}", currentUserId);
        }

        // 如果还是获取不到用户ID，或者用户ID是anonymous，返回空
        if (currentUserId == null || currentUserId.isEmpty() || "anonymous".equals(currentUserId)) {
            log.debug("No valid userId found, returning null");
            return ResponseEntity.ok(null);
        }

        // 获取用户信息
        log.debug("Looking up user with ID: {}", currentUserId);
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        return ResponseEntity.ok(convertToUserResponse(user));
    }

    /**
     * 更新当前用户信息
     */
    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest userData,
            HttpServletRequest request) {

        // 获取当前用户ID
        String currentUserId = request.getHeader("X-User-ID");
        if (currentUserId == null || currentUserId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // 获取用户信息
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 更新用户信息
        if (userData.getName() != null) {
            user.setName(userData.getName());
        }
        if (userData.getGender() != null) {
            user.setGender(userData.getGender());
        }
        if (userData.getPhone() != null) {
            user.setPhone(userData.getPhone());
        }
        if (userData.getEmail() != null) {
            user.setEmail(userData.getEmail());
        }
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(convertToUserResponse(updatedUser));
    }

    /**
     * 将用户实体转换为UserResponse DTO
     */
    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .age(user.getAge())
                .gender(user.getGender())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}