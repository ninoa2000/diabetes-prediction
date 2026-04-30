package com.example.chronicdisease.repository;

import com.example.chronicdisease.model.PredictionRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionRecordRepository extends MongoRepository<PredictionRecord, String> {
    List<PredictionRecord> findByUserIdOrderByCreatedAtDesc(String userId);
    List<PredictionRecord> findByPatientId(String patientId);
    long countByUserIdIn(List<String> userIds);
} 