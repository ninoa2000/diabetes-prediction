package com.example.chronicdisease.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "predictions")
public class Prediction {
    
    @Id
    private String id;
    
    @NotBlank
    private String userId;
    
    private Map<String, Object> healthData;
    
    private double riskPercentage;
    
    private String riskLevel;
    
    private String[] recommendations;
    
    private String doctorRecommendation;
    
    private String doctorId;
    
    @CreatedDate
    private LocalDateTime createdAt;
} 