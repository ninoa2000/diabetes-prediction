package com.example.chronicdisease.dto.prediction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionRequest {
    @NotNull(message = "健康数据不能为空")
    private Map<String, Object> healthData;
} 