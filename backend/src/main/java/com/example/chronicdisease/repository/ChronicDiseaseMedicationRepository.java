package com.example.chronicdisease.repository;

import com.example.chronicdisease.model.ChronicDiseaseMedication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChronicDiseaseMedicationRepository extends MongoRepository<ChronicDiseaseMedication, String> {
    List<ChronicDiseaseMedication> findByDiseaseOrderByOrderAsc(String disease);
} 