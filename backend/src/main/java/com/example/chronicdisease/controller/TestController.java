package com.example.chronicdisease.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Slf4j
public class TestController {

    @GetMapping("/public")
    public ResponseEntity<Map<String, String>> publicEndpoint() {
        log.info("访问公开测试端点");
        Map<String, String> response = new HashMap<>();
        response.put("message", "这是一个公开的测试端点，无需认证");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/secured")
    public ResponseEntity<Map<String, String>> securedEndpoint() {
        log.info("访问受保护测试端点");
        Map<String, String> response = new HashMap<>();
        response.put("message", "这是一个受保护的测试端点，需要认证");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }
} 