package com.example.chronicdisease.repository;

import com.example.chronicdisease.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
    
    List<Patient> findByDoctorId(String doctorId);
    
    Patient findByUserId(String userId);
} 