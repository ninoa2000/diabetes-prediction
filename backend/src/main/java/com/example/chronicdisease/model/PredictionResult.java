package com.example.chronicdisease.model;

import lombok.Data;

@Data
public class PredictionResult {
    private double probability;
    private double percentage;
    private String disease;
    private String suggestion;
} 