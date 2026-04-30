package com.example.chronicdisease.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document(collection = "prediction_records")
public class PredictionRecord {
    @Id
    private String id;
    
    private String userId;
    private String patientId;
    private String fileName;
    private String algorithm;
    private Map<String, Object> healthData;
    private String disease;
    private Double probability;
    private String suggestion;
    private LocalDateTime createdAt;
    
    public PredictionRecord() {
        this.createdAt = LocalDateTime.now();
    }
} 