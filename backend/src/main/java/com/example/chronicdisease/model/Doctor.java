package com.example.chronicdisease.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "doctors")
public class Doctor {
    @Id
    private String id;
    private String userId;
    private String name;
    private String gender;
    private String department;
    private String title; // 职称: 主任医师、副主任医师、主治医师等
    private String specialty; // 专长
    private String licenseNumber; // 医师执业证号
    private String phoneNumber;
    private String email;
    private String hospitalName;
    private String hospitalAddress;
    private String introduction; // 医生简介
    private String education; // 教育背景
    private List<String> certificates = new ArrayList<>(); // 资格证书
    private List<String> patientIds = new ArrayList<>(); // 负责的患者ID列表
    private int yearsOfExperience;
    private String profileImage; // 医生头像URL
    private boolean available; // 是否接诊
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
} 