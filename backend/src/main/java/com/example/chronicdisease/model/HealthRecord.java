package com.example.chronicdisease.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "health_records")
public class HealthRecord {
    @Id
    private String id;
    private String patientId;
    private String doctorId;
    private Date recordDate;
    private String recordType; // 例如: "常规检查", "专科检查", "实验室检查"
    private Map<String, Object> vitalSigns = new HashMap<>(); // 血压, 体温, 脉搏, 呼吸率等
    private Map<String, Object> labResults = new HashMap<>(); // 血液检查, 尿检等
    private String diagnosis;
    private String symptoms;
    private String treatment;
    private String medication;
    private String notes;
    private boolean followUpRequired;
    private Date followUpDate;
    private String createdBy; // 记录创建者ID (医生或系统)
} 