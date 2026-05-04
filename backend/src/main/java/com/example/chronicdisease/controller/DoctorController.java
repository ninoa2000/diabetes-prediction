package com.example.chronicdisease.controller;

import com.example.chronicdisease.dto.doctor.DoctorRequest;
import com.example.chronicdisease.dto.doctor.DoctorResponse;
import com.example.chronicdisease.model.Doctor;
import com.example.chronicdisease.model.User;
import com.example.chronicdisease.repository.DoctorRepository;
import com.example.chronicdisease.repository.UserRepository;
import com.example.chronicdisease.service.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doctor")
@Slf4j
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<DoctorResponse> responses = doctors.stream()
                .map(this::convertToDoctorResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/my-doctor")
    public ResponseEntity<DoctorResponse> getMyDoctor() {
        User user = getCurrentUser();
        if (user == null || user.getBoundDoctorId() == null) {
            return ResponseEntity.ok(null);
        }
        
        Doctor doctor = doctorRepository.findById(user.getBoundDoctorId()).orElse(null);
        return ResponseEntity.ok(doctor != null ? convertToDoctorResponse(doctor) : null);
    }

    @PostMapping("/bind/{doctorId}")
    public ResponseEntity<Void> bindDoctor(@PathVariable String doctorId) {
        User user = getCurrentUser();
        if (user == null) throw new RuntimeException("Not authenticated");
        
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
                
        // Bind user to doctor
        user.setBoundDoctorId(doctorId);
        userRepository.save(user);
        
        // Add user to doctor's patient list
        if (doctor.getPatientIds() == null) {
            doctor.setPatientIds(new java.util.ArrayList<>());
        }
        if (!doctor.getPatientIds().contains(user.getId())) {
            doctor.getPatientIds().add(user.getId());
            doctorRepository.save(doctor);
        }
        
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-patients")
    public ResponseEntity<List<User>> getMyPatients() {
        Doctor doctor = getOrCreateDoctor();
        List<User> patients = doctor.getPatientIds().stream()
                .map(id -> userRepository.findById(id).orElse(null))
                .filter(p -> p != null)
                .collect(Collectors.toList());
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/profile")
    public ResponseEntity<DoctorResponse> getProfile() {
        Doctor doctor = getOrCreateDoctor();
        return ResponseEntity.ok(convertToDoctorResponse(doctor));
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        String principal = auth.getName();
        if (principal == null || "anonymousUser".equals(principal)) {
            return null;
        }
        User user = userRepository.findByUsername(principal).orElse(null);
        if (user == null) {
            user = userRepository.findById(principal).orElse(null);
        }
        return user;
    }

    private Doctor getOrCreateDoctor() {
        User user = getCurrentUser();
        if (user == null) throw new RuntimeException("Not authenticated");
        
        Doctor doctor = doctorRepository.findByUserId(user.getId()).orElse(null);
        if (doctor == null && user.getRoles().contains(User.Role.ROLE_DOCTOR)) {
            doctor = new Doctor();
            doctor.setUserId(user.getId());
            doctor.setName(user.getName());
            doctor.setAvailable(true);
            doctor.setCreatedAt(LocalDateTime.now());
            doctor = doctorRepository.save(doctor);
        } else if (doctor == null) {
            throw new RuntimeException("Doctor profile not found");
        }
        return doctor;
    }

    private DoctorResponse convertToDoctorResponse(Doctor doctor) {
        User user = doctor.getUserId() != null ? userRepository.findById(doctor.getUserId()).orElse(null) : null;
        return DoctorResponse.builder()
                .id(doctor.getId())
                .userId(doctor.getUserId())
                .name(doctor.getName())
                .username(user != null ? user.getUsername() : "N/A")
                .department(doctor.getDepartment())
                .specialty(doctor.getSpecialty())
                .title(doctor.getTitle())
                .available(doctor.isAvailable())
                .createdAt(doctor.getCreatedAt())
                .updatedAt(doctor.getUpdatedAt())
                .build();
    }
}