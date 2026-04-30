package com.example.chronicdisease.service.impl;

import com.example.chronicdisease.model.PredictionResult;
import com.example.chronicdisease.repository.PredictionRecordRepository;
import com.example.chronicdisease.service.PredictionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionServiceImpl implements PredictionService {

    @Value("${prediction.api.url}")
    private String predictionApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final PredictionRecordRepository predictionRecordRepository;

    @Override
    public PredictionResult predict(String caseId) {
        // 获取病例数据
        Map<String, Object> healthData = predictionRecordRepository.findById(caseId)
            .orElseThrow(() -> new RuntimeException("病例不存在"))
            .getHealthData();
            
        log.debug("Sending health data to Python API: {}", healthData);
        
        // 调用Python API进行预测
        Map<String, Object> response = restTemplate.postForObject(
            predictionApiUrl + "/api/predict",
            healthData,
            Map.class
        );
        
        log.debug("Received prediction result: {}", response);
        
        // 创建预测结果对象
        PredictionResult result = new PredictionResult();
        result.setProbability((Double) response.get("probability"));
        result.setPercentage((Double) response.get("probability") * 100);
        result.setDisease("糖尿病");
        
        // 根据概率生成详细建议
        double probability = result.getProbability();
        String suggestion = generateSuggestion(probability);
        result.setSuggestion(suggestion);
        
        return result;
    }
    
    private String generateSuggestion(double probability) {
        StringBuilder suggestion = new StringBuilder();
        
        if (probability < 0.33) {
            // 低风险建议
            suggestion.append("当前糖尿病风险较低，建议：\n");
            suggestion.append("1. 继续保持健康的生活方式\n");
            suggestion.append("2. 均衡饮食，控制糖分摄入\n");
            suggestion.append("3. 保持适量运动，每周至少150分钟\n");
            suggestion.append("4. 定期监测血糖，建议每年体检一次\n");
            suggestion.append("5. 保持健康体重，BMI控制在18.5-24之间");
        } else if (probability < 0.66) {
            // 中风险建议
            suggestion.append("当前糖尿病风险中等，建议：\n");
            suggestion.append("1. 调整饮食习惯，减少高糖高脂食物\n");
            suggestion.append("2. 增加运动量，建议每周运动3-5次\n");
            suggestion.append("3. 控制体重，BMI建议控制在18.5-24之间\n");
            suggestion.append("4. 定期监测血糖，建议每半年体检一次\n");
            suggestion.append("5. 戒烟限酒，保持良好的作息习惯\n");
            suggestion.append("6. 如有家族史，建议咨询专业医生");
        } else {
            // 高风险建议
            suggestion.append("当前糖尿病风险较高，建议：\n");
            suggestion.append("1. 立即就医，进行专业评估\n");
            suggestion.append("2. 严格控制饮食，遵循医生建议的饮食计划\n");
            suggestion.append("3. 增加运动量，建议每天运动30-60分钟\n");
            suggestion.append("4. 密切监测血糖，建议每3个月体检一次\n");
            suggestion.append("5. 控制体重，BMI建议控制在18.5-24之间\n");
            suggestion.append("6. 戒烟戒酒，保持良好的作息习惯\n");
            suggestion.append("7. 如有不适症状，及时就医");
        }
        
        return suggestion.toString();
    }
} 