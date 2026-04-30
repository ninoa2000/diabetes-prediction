package com.example.chronicdisease.service;

import com.example.chronicdisease.model.PredictionResult;
 
public interface PredictionService {
    PredictionResult predict(String caseId);
} 