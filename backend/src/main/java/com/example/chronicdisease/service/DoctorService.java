package com.example.chronicdisease.service;

import com.example.chronicdisease.dto.doctor.DoctorDto;
import com.example.chronicdisease.model.Doctor;

import java.util.List;

public interface DoctorService {
    
    /**
     * 获取所有医生
     */
    List<Doctor> findAllDoctors();
    
    /**
     * 根据ID查找医生
     */
    Doctor findById(String id);
    
    /**
     * 更新医生状态
     */
    Doctor updateDoctorStatus(String id, boolean available);
    
    /**
     * 获取可用的医生
     */
    List<Doctor> findAvailableDoctors();
    
    /**
     * 根据用户ID查找医生
     */
    Doctor findByUserId(String userId);

    Doctor createDoctor(Doctor doctor);
    Doctor updateDoctor(String doctorId, Doctor doctor);
    Doctor getDoctorByUserId(String userId);
    List<Doctor> getAllDoctors();
    List<Doctor> getAvailableDoctors();
    Doctor bindPatient(String doctorId, String patientId);
    Doctor unbindPatient(String doctorId, String patientId);
    List<DoctorDto> getRecommendedDoctors(String diseaseType);
    void deleteDoctor(String doctorId);
} 