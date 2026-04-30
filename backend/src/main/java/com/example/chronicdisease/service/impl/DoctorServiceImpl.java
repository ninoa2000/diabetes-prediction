package com.example.chronicdisease.service.impl;

import com.example.chronicdisease.dto.doctor.DoctorDto;
import com.example.chronicdisease.exception.ResourceNotFoundException;
import com.example.chronicdisease.model.Doctor;
import com.example.chronicdisease.repository.DoctorRepository;
import com.example.chronicdisease.repository.UserRepository;
import com.example.chronicdisease.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    @Override
    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor findById(String id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("医生不存在，ID: " + id));
    }

    @Override
    public Doctor updateDoctorStatus(String id, boolean available) {
        Doctor doctor = findById(id);
        doctor.setAvailable(available);
        doctor.setUpdatedAt(LocalDateTime.now());
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> findAvailableDoctors() {
        return doctorRepository.findByAvailableTrue();
    }

    @Override
    public Doctor findByUserId(String userId) {
        return doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("医生不存在，用户ID: " + userId));
    }

    @Override
    public Doctor createDoctor(Doctor doctor) {
        doctor.setCreatedAt(LocalDateTime.now());
        doctor.setUpdatedAt(LocalDateTime.now());
        if (doctor.getPatientIds() == null) {
            doctor.setPatientIds(new ArrayList<>());
        }
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor updateDoctor(String doctorId, Doctor doctor) {
        Doctor existingDoctor = findById(doctorId);
        
        // 更新基本信息
        existingDoctor.setName(doctor.getName());
        existingDoctor.setDepartment(doctor.getDepartment());
        existingDoctor.setSpecialty(doctor.getSpecialty());
        existingDoctor.setTitle(doctor.getTitle());
        existingDoctor.setAvailable(doctor.isAvailable());
        existingDoctor.setUpdatedAt(LocalDateTime.now());
        
        return doctorRepository.save(existingDoctor);
    }

    @Override
    public Doctor getDoctorByUserId(String userId) {
        return doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("未找到医生信息，用户ID: " + userId));
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public List<Doctor> getAvailableDoctors() {
        return doctorRepository.findByAvailableTrue();
    }

    @Override
    public Doctor bindPatient(String doctorId, String patientId) {
        Doctor doctor = findById(doctorId);
        
        if (doctor.getPatientIds() == null) {
            doctor.setPatientIds(new ArrayList<>());
        }
        
        if (!doctor.getPatientIds().contains(patientId)) {
            doctor.getPatientIds().add(patientId);
            doctor.setUpdatedAt(LocalDateTime.now());
            return doctorRepository.save(doctor);
        }
        
        return doctor;
    }

    @Override
    public Doctor unbindPatient(String doctorId, String patientId) {
        Doctor doctor = findById(doctorId);
        
        if (doctor.getPatientIds() != null && doctor.getPatientIds().contains(patientId)) {
            doctor.getPatientIds().remove(patientId);
            doctor.setUpdatedAt(LocalDateTime.now());
            return doctorRepository.save(doctor);
        }
        
        return doctor;
    }

    @Override
    public List<DoctorDto> getRecommendedDoctors(String diseaseType) {
        List<Doctor> doctors = getAvailableDoctors();
        
        // 根据疾病类型筛选医生，这里简单实现，实际应该有更复杂的匹配算法
        List<Doctor> recommendedDoctors = doctors.stream()
                .filter(doctor -> doctor.getSpecialty() != null && doctor.getSpecialty().contains(diseaseType))
                .collect(Collectors.toList());
        
        if (recommendedDoctors.isEmpty()) {
            recommendedDoctors = doctors;
        }
        
        // 转换为DTO
        return recommendedDoctors.stream()
                .map(doctor -> {
                    DoctorDto dto = new DoctorDto();
                    dto.setId(doctor.getId());
                    dto.setName(doctor.getName());
                    dto.setDepartment(doctor.getDepartment());
                    dto.setSpecialty(doctor.getSpecialty());
                    dto.setTitle(doctor.getTitle());
                    dto.setPatientCount(doctor.getPatientIds() != null ? doctor.getPatientIds().size() : 0);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDoctor(String doctorId) {
        doctorRepository.deleteById(doctorId);
    }
} 