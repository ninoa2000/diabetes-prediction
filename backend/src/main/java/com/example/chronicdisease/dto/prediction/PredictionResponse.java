package com.example.chronicdisease.dto.prediction;

import com.example.chronicdisease.model.Prediction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResponse {
    private String id;
    private String userId;
    private Map<String, Object> healthData;
    private double riskPercentage;
    private String riskLevel;
    private String[] recommendations;
    private String doctorRecommendation;
    private String doctorId;
    private LocalDateTime createdAt;
    
    public static PredictionResponse fromPrediction(Prediction prediction) {
        return PredictionResponse.builder()
                .id(prediction.getId())
                .userId(prediction.getUserId())
                .healthData(prediction.getHealthData())
                .riskPercentage(prediction.getRiskPercentage())
                .riskLevel(prediction.getRiskLevel())
                .recommendations(prediction.getRecommendations())
                .doctorRecommendation(prediction.getDoctorRecommendation())
                .doctorId(prediction.getDoctorId())
                .createdAt(prediction.getCreatedAt())
                .build();
    }
} 