package com.example.chronicdisease.controller;

import com.example.chronicdisease.model.PredictionRecord;
import com.example.chronicdisease.repository.PredictionRecordRepository;
import com.example.chronicdisease.model.PredictionResult;
import com.example.chronicdisease.service.PredictionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/predictions")
@RequiredArgsConstructor
@Slf4j
public class PredictionController {

    private final PredictionRecordRepository predictionRecordRepository;
    private final PredictionService predictionService;

    @PostMapping
    public ResponseEntity<?> saveHealthData(@RequestBody Map<String, Object> requestData) {
        // 从请求数据中获取用户ID
        Object userIdObj = requestData.get("userId");
        String userId = userIdObj != null ? userIdObj.toString() : null;
        
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body("用户ID不能为空");
        }
        
        // 从请求数据中移除userId，保留其他健康数据
        requestData.remove("userId");
        
        log.debug("Saving health data for user: {}", userId);
        
        // 创建预测记录
        PredictionRecord record = new PredictionRecord();
        record.setHealthData(requestData);
        record.setUserId(userId);
        
        // 保存病例记录
        PredictionRecord savedRecord = predictionRecordRepository.save(record);

        return ResponseEntity.ok(savedRecord);
    }

    @GetMapping("/cases")
    public ResponseEntity<?> getHealthCases(@RequestHeader("X-User-ID") String userId) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body("用户ID不能为空");
        }
        
        log.debug("Getting health cases for user: {}", userId);
        
        // 只获取当前用户的病例记录
        var cases = predictionRecordRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(cases);
    }

    @DeleteMapping("/cases/{caseId}")
    public ResponseEntity<?> deleteCase(@PathVariable String caseId, @RequestHeader("X-User-ID") String userId) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body("用户ID不能为空");
        }
        
        // 检查病例是否存在且属于当前用户
        PredictionRecord record = predictionRecordRepository.findById(caseId)
            .orElseThrow(() -> new RuntimeException("病例不存在"));
            
        if (!userId.equals(record.getUserId())) {
            return ResponseEntity.status(403).body("无权删除此病例");
        }
        
        // 删除病例
        predictionRecordRepository.deleteById(caseId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{recordId}/predict")
    public ResponseEntity<?> predictDisease(@PathVariable String recordId, @RequestHeader("X-User-ID") String userId) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body("用户ID不能为空");
        }
        
        // 获取病例记录并验证所有权
        PredictionRecord record = predictionRecordRepository.findById(recordId)
            .orElseThrow(() -> new RuntimeException("病例不存在"));
            
        if (!userId.equals(record.getUserId())) {
            return ResponseEntity.status(403).body("无权访问此病例");
        }

        // 调用Python API进行预测
        PredictionResult result = predictionService.predict(recordId);
        
        // 更新预测结果
        record.setProbability(result.getProbability());
        record.setSuggestion(result.getSuggestion());
        record.setDisease(result.getDisease());

        // 保存预测结果
        predictionRecordRepository.save(record);

        // 返回预测结果
        return ResponseEntity.ok(Map.of(
            "probability", result.getProbability(),
            "suggestion", result.getSuggestion(),
            "disease", result.getDisease()
        ));
    }

    @PostMapping("/{caseId}")
    public ResponseEntity<PredictionResult> predict(@PathVariable String caseId) {
        PredictionResult result = predictionService.predict(caseId);
        return ResponseEntity.ok(result);
    }
} 