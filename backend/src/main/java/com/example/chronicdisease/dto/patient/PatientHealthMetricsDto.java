package com.example.chronicdisease.dto.patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientHealthMetricsDto {
    
    @NotNull(message = "身高不能为空")
    @Positive(message = "身高必须为正数")
    @Max(value = 250, message = "身高不能超过250厘米")
    private Double height; // 身高（厘米）
    
    @NotNull(message = "体重不能为空")
    @Positive(message = "体重必须为正数")
    private Double weight; // 体重（千克）
    
    @Min(value = 50, message = "舒张压不能低于50 mmHg")
    @Max(value = 200, message = "舒张压不能高于200 mmHg")
    private Integer diastolicBloodPressure; // 舒张压（mmHg）
    
    @Min(value = 80, message = "收缩压不能低于80 mmHg")
    @Max(value = 250, message = "收缩压不能高于250 mmHg")
    private Integer systolicBloodPressure; // 收缩压（mmHg）
    
    @Min(value = 50, message = "心率不能低于50次/分钟")
    @Max(value = 200, message = "心率不能高于200次/分钟")
    private Integer heartRate; // 心率（次/分钟）
    
    @Min(value = 35, message = "体温不能低于35℃")
    @Max(value = 45, message = "体温不能高于45℃")
    private Double bodyTemperature; // 体温（℃）
    
    @Min(value = 70, message = "血氧饱和度不能低于70%")
    @Max(value = 100, message = "血氧饱和度不能高于100%")
    private Integer bloodOxygen; // 血氧饱和度（%）
    
    @Min(value = 2, message = "血糖不能低于2 mmol/L")
    @Max(value = 30, message = "血糖不能高于30 mmol/L")
    private Double bloodGlucose; // 血糖（mmol/L）
    
    private Map<String, Object> additionalMetrics = new HashMap<>(); // 其他健康指标
} 