package com.example.chronicdisease.dto.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto {
    
    private String id;
    private String name;
    private String department;
    private String specialty;
    private String title;
    private int patientCount;
} 