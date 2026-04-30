package com.example.chronicdisease.controller;

import com.example.chronicdisease.dto.auth.JwtResponse;
import com.example.chronicdisease.dto.auth.LoginRequest;
import com.example.chronicdisease.dto.auth.RegisterRequest;
import com.example.chronicdisease.dto.user.ChangePasswordRequest;
import com.example.chronicdisease.model.User;
import com.example.chronicdisease.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            log.info("收到登录请求，用户名: {}", loginRequest.getUsername());
            log.debug("请求路径: {}, context-path: {}", request.getRequestURI(), request.getContextPath());
            log.debug("请求方法: {}", request.getMethod());
            
            // 打印请求头，帮助排查CORS问题
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                log.debug("请求头 {}: {}", headerName, request.getHeader(headerName));
            }
            
            JwtResponse jwtResponse = authService.login(loginRequest);
            log.info("登录成功，用户名: {}", loginRequest.getUsername());
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            // 记录异常并返回更具体的错误信息
            log.error("登录失败，异常: ", e);
            throw e;
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("收到注册请求，用户名: {}", registerRequest.getUsername());
        
        // 检查是否尝试注册为医生角色，如果是则禁止
        if (registerRequest.getRole() != null && registerRequest.getRole().equals("ROLE_DOCTOR")) {
            log.warn("尝试注册医生账户被拒绝，用户名: {}", registerRequest.getUsername());
            throw new RuntimeException("不允许注册医生账户，医生账户只能由管理员创建");
        }
        
        User user = authService.register(registerRequest);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "注册成功");
        response.put("user", user);
        
        log.info("注册成功，用户名: {}", registerRequest.getUsername());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    // 用于测试的简单健康检查接口
    @GetMapping("/auth/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        log.info("收到健康检查请求");
        Map<String, String> response = new HashMap<>();
        response.put("status", "up");
        response.put("message", "Auth service is running");
        return ResponseEntity.ok(response);
    }
    @PostMapping("/user/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        log.info(changePasswordRequest.toString());
        boolean result = authService.changePassword(changePasswordRequest);
        Map<String, String> response = new HashMap<>();
        response.put("status", String.valueOf(result));
        response.put("message", result?"修改成功":"修改失败");
        return ResponseEntity.ok(response);
    }
} 