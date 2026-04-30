package com.example.chronicdisease.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "medical_records")
public class MedicalRecord {
    @Id
    private String id;
    private String patientId;
    private String doctorId;
    private LocalDateTime recordDate;
    private String diagnosis;
    private String treatment;
    private String notes;
    private Map<String, Object> vitalSigns = new HashMap<>();
    private Map<String, Object> labResults = new HashMap<>();
    private String recordType; // Regular checkup, Emergency, Follow-up, etc.
} 