package com.example.chronicdisease.service;

import com.example.chronicdisease.dto.patient.PatientHealthMetricsDto;
import com.example.chronicdisease.model.Patient;

import java.util.List;
import java.util.Map;

public interface PatientService {
    Patient createPatient(Patient patient);
    Patient updatePatient(String patientId, Patient patient);
    Patient getPatientById(String patientId);
    Patient getPatientByUserId(String userId);
    List<Patient> getAllPatients();
    List<Patient> getPatientsByDoctorId(String doctorId);
    Patient updateHealthMetrics(String patientId, PatientHealthMetricsDto healthMetrics);
    Map<String, Object> getPatientDashboardData(String patientId);
    void deletePatient(String patientId);
} 