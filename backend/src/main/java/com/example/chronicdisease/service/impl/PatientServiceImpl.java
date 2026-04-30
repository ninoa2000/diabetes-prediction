package com.example.chronicdisease.service.impl;

import com.example.chronicdisease.dto.patient.PatientHealthMetricsDto;
import com.example.chronicdisease.model.Patient;
import com.example.chronicdisease.repository.PatientRepository;
import com.example.chronicdisease.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(String patientId, Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient getPatientById(String patientId) {
        return patientRepository.findById(patientId).orElse(null);
    }

    @Override
    public Patient getPatientByUserId(String userId) {
        return patientRepository.findByUserId(userId);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public List<Patient> getPatientsByDoctorId(String doctorId) {
        return patientRepository.findByDoctorId(doctorId);
    }

    @Override
    public Patient updateHealthMetrics(String patientId, PatientHealthMetricsDto healthMetrics) {
        // 暂时返回null，需要实现健康指标更新逻辑
        return null;
    }

    @Override
    public Map<String, Object> getPatientDashboardData(String patientId) {
        // 暂时返回空map，需要实现仪表盘数据获取逻辑
        return new HashMap<>();
    }

    @Override
    public void deletePatient(String patientId) {
        patientRepository.deleteById(patientId);
    }
} 