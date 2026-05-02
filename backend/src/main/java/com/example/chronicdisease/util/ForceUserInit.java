package com.example.chronicdisease.util;

import com.example.chronicdisease.model.User;
import com.example.chronicdisease.repository.UserRepository;
import com.example.chronicdisease.repository.UsersRepository;
import com.example.chronicdisease.model.Doctor;
import com.example.chronicdisease.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ForceUserInit implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        System.out.println(">>> STARTING FORCE USER INITIALIZATION <<<");
        
        setupUser("admin", "admin123", "System Administrator", "admin@diabetes-system.com", "0000000000", User.Role.ROLE_ADMIN);
        setupUser("doctor3", "doctor123", "Dr. Nino", "nino.doctor@clinic.com", "1122334455", User.Role.ROLE_DOCTOR);
        
        System.out.println(">>> FORCE USER INITIALIZATION COMPLETE <<<");
    }

    private void setupUser(String username, String rawPassword, String name, String email, String phone, User.Role role) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        // 1. Setup MongoDB
        User mongoUser = userRepository.findByUsername(username).orElse(new User());
        mongoUser.setUsername(username);
        mongoUser.setPassword(encodedPassword);
        mongoUser.setName(name);
        mongoUser.setEmail(email);
        mongoUser.setPhone(phone);
        mongoUser.setActive(true);
        mongoUser.setCreatedAt(LocalDateTime.now());
        
        Set<User.Role> roles = new HashSet<>();
        roles.add(role);
        mongoUser.setRoles(roles);
        
        mongoUser = userRepository.save(mongoUser);
        System.out.println("Saved Mongo User: " + username);

        // 2. Setup MySQL
        com.example.chronicdisease.entity.User sqlUser = usersRepository.findByUsername(username).orElse(new com.example.chronicdisease.entity.User());
        sqlUser.setUsername(username);
        sqlUser.setPassword(encodedPassword);
        sqlUser.setName(name);
        sqlUser.setEmail(email);
        sqlUser.setPhone(phone);
        sqlUser.setActive(true);
        sqlUser.setCreatedAt(LocalDateTime.now());
        sqlUser.setUpdatedAt(LocalDateTime.now());
        
        // Roles in MySQL are stored as a JSON string
        sqlUser.setRoles("[\"" + role.name() + "\"]");
        
        usersRepository.save(sqlUser);
        System.out.println("Saved MySQL User: " + username);

        // 3. If it's a doctor, ensure doctor profile exists
        if (role == User.Role.ROLE_DOCTOR) {
            Doctor doctor = doctorRepository.findByUserId(mongoUser.getId()).orElse(new Doctor());
            doctor.setUserId(mongoUser.getId());
            doctor.setName(name);
            doctor.setDepartment("Internal Medicine");
            doctor.setSpecialty("Chronic Disease Prediction");
            doctor.setTitle("Associate Chief Physician");
            doctor.setHospitalName("University Hospital");
            doctor.setAvailable(true);
            doctor.setCreatedAt(LocalDateTime.now());
            doctorRepository.save(doctor);
            System.out.println("Saved Doctor Profile for: " + username);
        }
    }
}
