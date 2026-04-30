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
        // Get userId from request
        Object userIdObj = requestData.get("userId");
        String userId = userIdObj != null ? userIdObj.toString() : null;
        
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body("User ID cannot be empty");
        }
        
        // Extract additional metadata
        Object fileNameObj = requestData.get("fileName");
        String fileName = fileNameObj != null ? fileNameObj.toString() : null;
        Object algorithmObj = requestData.get("algorithm");
        String algorithm = algorithmObj != null ? algorithmObj.toString() : null;
        Object diseaseObj = requestData.get("disease");
        String disease = diseaseObj != null ? diseaseObj.toString() : null;
        Object probabilityObj = requestData.get("probability");
        Double probability = probabilityObj != null ? Double.parseDouble(probabilityObj.toString()) : null;
        Object suggestionObj = requestData.get("suggestion");
        String suggestion = suggestionObj != null ? suggestionObj.toString() : null;
        
        // Remove metadata from health data to keep it clean
        requestData.remove("userId");
        requestData.remove("fileName");
        requestData.remove("algorithm");
        requestData.remove("disease");
        requestData.remove("probability");
        requestData.remove("suggestion");
        
        log.debug("Saving health data for user: {}", userId);
        
        // Create prediction record
        PredictionRecord record = new PredictionRecord();
        record.setHealthData(requestData);
        record.setUserId(userId);
        record.setFileName(fileName);
        record.setAlgorithm(algorithm);
        record.setDisease(disease);
        record.setProbability(probability);
        record.setSuggestion(suggestion);
        
        // Save record
        PredictionRecord savedRecord = predictionRecordRepository.save(record);

        return ResponseEntity.ok(savedRecord);
    }

    @GetMapping("/cases")
    public ResponseEntity<?> getHealthCases(@RequestHeader("X-User-ID") String userId) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body("User ID cannot be empty");
        }
        
        log.debug("Getting health cases for user: {}", userId);
        
        // Get user's records
        var cases = predictionRecordRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(cases);
    }

    @DeleteMapping("/cases/{caseId}")
    public ResponseEntity<?> deleteCase(@PathVariable String caseId, @RequestHeader("X-User-ID") String userId) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body("User ID cannot be empty");
        }
        
        // Check if record exists and belongs to user
        PredictionRecord record = predictionRecordRepository.findById(caseId)
            .orElseThrow(() -> new RuntimeException("Record does not exist"));
            
        if (!userId.equals(record.getUserId())) {
            return ResponseEntity.status(403).body("No permission to delete this record");
        }
        
        // Delete record
        predictionRecordRepository.deleteById(caseId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{recordId}/predict")
    public ResponseEntity<?> predictDisease(@PathVariable String recordId, @RequestHeader("X-User-ID") String userId) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body("User ID cannot be empty");
        }
        
        // Get record and verify ownership
        PredictionRecord record = predictionRecordRepository.findById(recordId)
            .orElseThrow(() -> new RuntimeException("Record does not exist"));
            
        if (!userId.equals(record.getUserId())) {
            return ResponseEntity.status(403).body("No permission to access this record");
        }

        // Call Python API
        PredictionResult result = predictionService.predict(recordId);
        
        // Update results
        record.setProbability(result.getProbability());
        record.setSuggestion(result.getSuggestion());
        record.setDisease(result.getDisease());

        // Save results
        predictionRecordRepository.save(record);

        // Return results
        return ResponseEntity.ok(Map.of(
            "probability", result.getProbability(),
            "suggestion", result.getSuggestion(),
            "disease", result.getDisease()
        ));
    }

    @PostMapping("/cases/{caseId}/results")
    public ResponseEntity<?> updatePredictionResults(
            @PathVariable String caseId, 
            @RequestHeader("X-User-ID") String userId,
            @RequestBody Map<String, Object> requestData) {
            
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body("User ID cannot be empty");
        }
        
        PredictionRecord record = predictionRecordRepository.findById(caseId)
            .orElseThrow(() -> new RuntimeException("Record does not exist"));
            
        if (!userId.equals(record.getUserId())) {
            return ResponseEntity.status(403).body("No permission to update this record");
        }
        
        if (requestData.containsKey("algorithm")) record.setAlgorithm(requestData.get("algorithm").toString());
        if (requestData.containsKey("disease")) record.setDisease(requestData.get("disease").toString());
        if (requestData.containsKey("probability")) record.setProbability(Double.parseDouble(requestData.get("probability").toString()));
        if (requestData.containsKey("suggestion")) record.setSuggestion(requestData.get("suggestion").toString());
        
        predictionRecordRepository.save(record);
        return ResponseEntity.ok(record);
    }
} 