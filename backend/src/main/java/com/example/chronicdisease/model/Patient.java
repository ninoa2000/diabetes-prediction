package com.example.chronicdisease.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "patients")
public class Patient {
    @Id
    private String id;
    private String userId;
    private String name;
    private int age;
    private String gender;
    private Date dateOfBirth;
    private String phoneNumber;
    private String email;
    private String address;
    private String emergencyContact;
    private String emergencyContactPhone;
    private String bloodType;
    private Double height; // in cm
    private Double weight; // in kg
    private List<String> allergies = new ArrayList<>();
    private List<String> chronicConditions = new ArrayList<>();
    private List<String> medications = new ArrayList<>();
    private List<String> surgicalHistory = new ArrayList<>();
    private List<String> familyMedicalHistory = new ArrayList<>();
    private Map<String, Object> vitalSigns = new HashMap<>(); // 最新的生命体征数据
    private String doctorId; // 绑定的医生ID
    private String insuranceInfo;
    private Date lastCheckupDate;
} 