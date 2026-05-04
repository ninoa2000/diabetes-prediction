package com.example.chronicdisease.config;

import com.example.chronicdisease.model.Doctor;
import com.example.chronicdisease.model.User;
import com.example.chronicdisease.repository.DoctorRepository;
import com.example.chronicdisease.repository.UserRepository;
import com.example.chronicdisease.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UsersRepository usersRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed Admin
        if (!userRepository.existsByUsername("admin")) {
            log.info("Seeding admin user...");
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setName("System Admin");
            admin.setEmail("admin@example.com");
            admin.setRoles(new HashSet<>(Collections.singletonList(User.Role.ROLE_ADMIN)));
            admin.setCreatedAt(LocalDateTime.now());
            userRepository.save(admin);
            
            // Sync to MySQL
            com.example.chronicdisease.entity.User sqlAdmin = new com.example.chronicdisease.entity.User();
            sqlAdmin.setUsername("admin");
            sqlAdmin.setPassword(admin.getPassword());
            sqlAdmin.setName(admin.getName());
            sqlAdmin.setEmail(admin.getEmail());
            sqlAdmin.setRoles("[\"ROLE_ADMIN\"]");
            sqlAdmin.setActive(true);
            sqlAdmin.setCreatedAt(LocalDateTime.now());
            sqlAdmin.setUpdatedAt(LocalDateTime.now());
            usersRepository.save(sqlAdmin);
        }

        // 2. Seed a Doctor
        if (!userRepository.existsByUsername("doctor1")) {
            log.info("Seeding test doctor...");
            User doctorUser = new User();
            doctorUser.setUsername("doctor1");
            doctorUser.setPassword(passwordEncoder.encode("doctor123"));
            doctorUser.setName("Dr. Smith");
            doctorUser.setEmail("smith@hospital.com");
            doctorUser.setPhone("13800000001");
            doctorUser.setRoles(new HashSet<>(Collections.singletonList(User.Role.ROLE_DOCTOR)));
            doctorUser.setCreatedAt(LocalDateTime.now());
            doctorUser = userRepository.save(doctorUser);
            
            // Sync to MySQL
            com.example.chronicdisease.entity.User sqlDoctor = new com.example.chronicdisease.entity.User();
            sqlDoctor.setUsername("doctor1");
            sqlDoctor.setPassword(doctorUser.getPassword());
            sqlDoctor.setName(doctorUser.getName());
            sqlDoctor.setEmail(doctorUser.getEmail());
            sqlDoctor.setPhone(doctorUser.getPhone());
            sqlDoctor.setRoles("[\"ROLE_DOCTOR\"]");
            sqlDoctor.setActive(true);
            sqlDoctor.setCreatedAt(LocalDateTime.now());
            sqlDoctor.setUpdatedAt(LocalDateTime.now());
            usersRepository.save(sqlDoctor);

            // Create Doctor Entity
            Doctor doctor = new Doctor();
            doctor.setUserId(doctorUser.getId());
            doctor.setName("Dr. Smith");
            doctor.setDepartment("Endocrinology");
            doctor.setSpecialty("Type 2 Diabetes Specialist");
            doctor.setTitle("Chief Physician");
            doctor.setAvailable(true);
            doctor.setCreatedAt(LocalDateTime.now());
            doctorRepository.save(doctor);
        }

        // 3. Seed test accounts and bind them
        seedAndBindTestAccounts();
    }

    private void seedAndBindTestAccounts() {
        String patientUsername = "ninoa2000";
        String doctorUsername = "doctor3";
        String defaultPwd = passwordEncoder.encode("admin123");
        LocalDateTime now = LocalDateTime.now();

        // 1. Ensure Patient exists in both DBs
        User mongoPatient = userRepository.findByUsername(patientUsername).orElse(null);
        if (mongoPatient == null) {
            log.info("Seeding Mongo patient: {}", patientUsername);
            mongoPatient = new User();
            mongoPatient.setUsername(patientUsername);
            mongoPatient.setPassword(defaultPwd);
            mongoPatient.setName("Nino Patient");
            mongoPatient.setRoles(new HashSet<>(Collections.singletonList(User.Role.ROLE_USER)));
            mongoPatient.setCreatedAt(now);
            mongoPatient = userRepository.save(mongoPatient);
        }

        if (!usersRepository.existsByUsername(patientUsername)) {
            log.info("Seeding MySQL patient: {}", patientUsername);
            com.example.chronicdisease.entity.User sqlPatient = new com.example.chronicdisease.entity.User();
            sqlPatient.setUsername(patientUsername);
            sqlPatient.setPassword(defaultPwd);
            sqlPatient.setName(mongoPatient.getName());
            sqlPatient.setRoles("[\"ROLE_USER\"]");
            sqlPatient.setActive(true);
            sqlPatient.setCreatedAt(now);
            sqlPatient.setUpdatedAt(now);
            usersRepository.save(sqlPatient);
        }

        // 2. Ensure Doctor user exists in both DBs
        User mongoDoc = userRepository.findByUsername(doctorUsername).orElse(null);
        if (mongoDoc == null) {
            log.info("Seeding Mongo doctor: {}", doctorUsername);
            mongoDoc = new User();
            mongoDoc.setUsername(doctorUsername);
            mongoDoc.setPassword(defaultPwd);
            mongoDoc.setName("Dr. Nino");
            mongoDoc.setRoles(new HashSet<>(Collections.singletonList(User.Role.ROLE_DOCTOR)));
            mongoDoc.setCreatedAt(now);
            mongoDoc = userRepository.save(mongoDoc);
        }

        if (!usersRepository.existsByUsername(doctorUsername)) {
            log.info("Seeding MySQL doctor: {}", doctorUsername);
            com.example.chronicdisease.entity.User sqlDoc = new com.example.chronicdisease.entity.User();
            sqlDoc.setUsername(doctorUsername);
            sqlDoc.setPassword(defaultPwd);
            sqlDoc.setName(mongoDoc.getName());
            sqlDoc.setRoles("[\"ROLE_DOCTOR\"]");
            sqlDoc.setActive(true);
            sqlDoc.setCreatedAt(now);
            sqlDoc.setUpdatedAt(now);
            usersRepository.save(sqlDoc);
        }

        // 3. Ensure Doctor Profile exists
        Doctor doctor = doctorRepository.findByUserId(mongoDoc.getId()).orElse(null);
        if (doctor == null) {
            log.info("Seeding doctor profile for: {}", doctorUsername);
            doctor = new Doctor();
            doctor.setUserId(mongoDoc.getId());
            doctor.setName(mongoDoc.getName());
            doctor.setDepartment("Cardiology");
            doctor.setAvailable(true);
            doctor.setCreatedAt(now);
            doctor = doctorRepository.save(doctor);
        }

        // 4. Force Binding
        log.info("Ensuring binding between {} and {}", patientUsername, doctorUsername);
        mongoPatient.setBoundDoctorId(doctor.getId());
        userRepository.save(mongoPatient);

        if (doctor.getPatientIds() == null) {
            doctor.setPatientIds(new ArrayList<>());
        }
        if (!doctor.getPatientIds().contains(mongoPatient.getId())) {
            doctor.getPatientIds().add(mongoPatient.getId());
            doctorRepository.save(doctor);
        }
    }
}