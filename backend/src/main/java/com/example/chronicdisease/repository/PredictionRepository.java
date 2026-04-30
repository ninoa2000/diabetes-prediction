package com.example.chronicdisease.repository;

import com.example.chronicdisease.model.Prediction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionRepository extends MongoRepository<Prediction, String> {
    
    List<Prediction> findByUserIdOrderByCreatedAtDesc(String userId);
    
    List<Prediction> findByDoctorIdOrderByCreatedAtDesc(String doctorId);
} 