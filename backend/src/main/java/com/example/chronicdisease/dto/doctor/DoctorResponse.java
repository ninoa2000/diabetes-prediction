package com.example.chronicdisease.dto.doctor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {
    
    private String id;
    private String userId;
    private String username;
    private String name;
    private String gender;
    private String phone;
    private String email;
    private String department;
    private String specialty;
    private String title;
    private boolean available;
    private int patientCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 