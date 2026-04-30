package com.example.chronicdisease.controller;

import com.example.chronicdisease.dto.doctor.DoctorRequest;
import com.example.chronicdisease.dto.doctor.DoctorResponse;
import com.example.chronicdisease.dto.doctor.UpdateProfileRequest;
import com.example.chronicdisease.model.Doctor;
import com.example.chronicdisease.model.Patient;
import com.example.chronicdisease.model.PredictionRecord;
import com.example.chronicdisease.model.User;
import com.example.chronicdisease.repository.*;
import com.example.chronicdisease.service.DoctorService;
import com.example.chronicdisease.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
@Slf4j
public class DoctorController {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final PredictionRecordRepository predictionRecordRepository;
    private final MessageRepository messageRepository;

    /**
     * 获取所有医生（分页）
     */
    @GetMapping
    public ResponseEntity<?> getAllDoctors(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction) {
        
        // 如果没有分页参数，返回所有医生（保持向后兼容）
        if (page == null && size == null) {
        List<Doctor> doctors = doctorRepository.findAll();
        List<DoctorResponse> doctorResponses = doctors.stream()
                .map(this::convertToDoctorResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(doctorResponses);
        }
        
        // 使用分页参数
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 10;
        String sortField = sortBy != null ? sortBy : "createdAt";
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortField));
        Page<Doctor> doctorPage = doctorRepository.findAll(pageable);
        
        List<DoctorResponse> doctorResponses = doctorPage.getContent().stream()
                .map(this::convertToDoctorResponse)
                .collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctorResponses);
        response.put("currentPage", doctorPage.getNumber());
        response.put("totalItems", doctorPage.getTotalElements());
        response.put("totalPages", doctorPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 管理员添加新医生
     */
    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(@Valid @RequestBody DoctorRequest doctorRequest) {
        // 验证用户名是否已存在
        if (userRepository.existsByUsername(doctorRequest.getUsername())) {
            return ResponseEntity.badRequest().build();
        }

        // 创建用户
        User user = new User();
        user.setUsername(doctorRequest.getUsername());
        user.setPassword(passwordEncoder.encode("doctor123")); // 设置默认密码
        user.setName(doctorRequest.getName());
        user.setPhone(doctorRequest.getPhone());
        user.setEmail(doctorRequest.getEmail());
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        // 设置医生角色
        HashSet<User.Role> roles = new HashSet<>();
        roles.add(User.Role.ROLE_DOCTOR);
        user.setRoles(roles);

        // 保存用户
        User savedUser = userRepository.save(user);

        // 创建医生信息
        Doctor doctor = new Doctor();
        doctor.setUserId(savedUser.getId());
        doctor.setName(doctorRequest.getName());
        doctor.setDepartment(doctorRequest.getDepartment());
        doctor.setSpecialty(doctorRequest.getSpecialty());
        doctor.setTitle(doctorRequest.getTitle());
        doctor.setAvailable(true);
        doctor.setCreatedAt(LocalDateTime.now());

        // 保存医生信息
        Doctor savedDoctor = doctorRepository.save(doctor);
        
        return new ResponseEntity<>(convertToDoctorResponse(savedDoctor), HttpStatus.CREATED);
    }

    /**
     * 获取单个医生信息
     */
    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable String doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("医生不存在"));
        return ResponseEntity.ok(convertToDoctorResponse(doctor));
    }

    /**
     * 更新医生可用状态
     */
    @PutMapping("/{doctorId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponse> updateDoctorStatus(
            @PathVariable String doctorId,
            @RequestParam boolean available) {
        
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("医生不存在"));
        
        doctor.setAvailable(available);
        doctor.setUpdatedAt(LocalDateTime.now());
        
        // 同时更新用户状态
        User user = userRepository.findById(doctor.getUserId())
                .orElseThrow(() -> new RuntimeException("医生用户不存在"));
        
        user.setActive(available);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        Doctor updatedDoctor = doctorRepository.save(doctor);
        return ResponseEntity.ok(convertToDoctorResponse(updatedDoctor));
    }

    /**
     * 删除医生
     */
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable String doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("医生不存在"));
        
        // 删除医生关联的用户
        userRepository.deleteById(doctor.getUserId());
        
        // 删除医生记录
        doctorRepository.delete(doctor);

        return ResponseEntity.ok().build();
    }

    /**
     * 重置医生密码
     */
    @PostMapping("/{doctorId}/reset-password")
    public ResponseEntity<Void> resetPassword(@PathVariable String doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("医生不存在"));

        // 获取用户信息
        User user = userRepository.findById(doctor.getUserId())
                .orElseThrow(() -> new RuntimeException("医生用户不存在"));
        com.example.chronicdisease.entity.User userM = usersRepository.findByUsername(user.getUsername()).orElse(null);
        String pwd = new BCryptPasswordEncoder().encode("doctor123");
        System.out.println("========================");
        System.out.println(pwd);
        System.out.println("========================");
        // 重置密码为 doctor123
        userM.setPassword(pwd);
        userM.setUpdatedAt(LocalDateTime.now());
        usersRepository.save(userM);

        return ResponseEntity.ok().build();
    }

    /**
     * 更新医生信息
     */
    @PutMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable String doctorId,
            @Valid @RequestBody DoctorRequest doctorRequest) {
        
        // 获取医生信息
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("医生不存在"));
        
        // 获取用户信息
        User user = userRepository.findById(doctor.getUserId())
                .orElseThrow(() -> new RuntimeException("医生用户不存在"));
        
        // 更新用户信息
        user.setName(doctorRequest.getName());
        user.setPhone(doctorRequest.getPhone());
        user.setEmail(doctorRequest.getEmail());
        if (doctorRequest.getPassword() != null && !doctorRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(doctorRequest.getPassword()));
        }
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        // 更新医生信息
        doctor.setName(doctorRequest.getName());
        doctor.setDepartment(doctorRequest.getDepartment());
        doctor.setSpecialty(doctorRequest.getSpecialty());
        doctor.setTitle(doctorRequest.getTitle());
        doctor.setUpdatedAt(LocalDateTime.now());
        
        Doctor updatedDoctor = doctorRepository.save(doctor);
        return ResponseEntity.ok(convertToDoctorResponse(updatedDoctor));
    }

    /**
     * 获取当前用户绑定的医生信息
     */
    @GetMapping("/my-doctor")
    public ResponseEntity<DoctorResponse> getMyDoctor(HttpServletRequest request) {
        // 获取当前用户ID
        String currentUserId = null;
        
        // 1. 尝试从SecurityContext获取
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("SecurityContext authentication: {}", authentication);
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserId = authentication.getName();
            log.debug("Got userId from SecurityContext: {}", currentUserId);
        }
        
        // 2. 如果从SecurityContext获取失败，从请求头获取
        if (currentUserId == null || currentUserId.isEmpty()) {
            currentUserId = request.getHeader("X-User-ID");
            log.debug("Got userId from request header: {}", currentUserId);
            
            // 打印所有请求头，用于调试
            java.util.Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                log.debug("Request header - {}: {}", headerName, request.getHeader(headerName));
            }
        }
        
        // 如果还是获取不到用户ID，或者用户ID是anonymous，返回空
        if (currentUserId == null || currentUserId.isEmpty() || "anonymous".equals(currentUserId)) {
            log.debug("No valid userId found, returning null");
            return ResponseEntity.ok(null);
        }
        
        // 获取用户信息
        log.debug("Looking up user with ID: {}", currentUserId);
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
                
        // 如果用户没有绑定医生，返回空
        if (user.getBoundDoctorId() == null) {
            log.debug("User {} has no bound doctor", currentUserId);
            return ResponseEntity.ok(null);
        }
        
        // 获取绑定的医生信息
        log.debug("Looking up bound doctor with ID: {}", user.getBoundDoctorId());
        Doctor doctor = doctorRepository.findById(user.getBoundDoctorId())
                .orElseThrow(() -> new RuntimeException("绑定的医生不存在"));
                
        return ResponseEntity.ok(convertToDoctorResponse(doctor));
    }

    /**
     * 获取当前医生的患者列表
     */
    @GetMapping("/my-patients")
    public ResponseEntity<List<User>> getMyPatients() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();
        
        log.debug("Getting patients for doctor with userId: {}", currentUserId);
        
        Doctor doctor = doctorRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
                
        log.debug("Found doctor: {}", doctor);
        log.debug("Doctor's patientIds: {}", doctor.getPatientIds());
        
        // 如果医生没有患者，直接返回空列表
        if (doctor.getPatientIds() == null || doctor.getPatientIds().isEmpty()) {
            log.debug("No patients found for doctor");
            return ResponseEntity.ok(new ArrayList<>());
        }
        
        // 创建患者ID列表的副本用于遍历
        List<String> patientIds = new ArrayList<>(doctor.getPatientIds());
        List<String> invalidPatientIds = new ArrayList<>();
        
        // 获取所有患者信息（用户信息）
        List<User> patients = new ArrayList<>();
        for (String patientId : patientIds) {
            log.debug("Looking for user with ID: {}", patientId);
            User user = userRepository.findById(patientId).orElse(null);
            if (user != null) {
                // 验证患者是否确实绑定了当前医生
                if (doctor.getId().equals(user.getBoundDoctorId())) {
                    log.debug("Found valid patient: {}", user);
                patients.add(user);
                } else {
                    log.warn("User {} is not actually bound to doctor {}", patientId, doctor.getId());
                    invalidPatientIds.add(patientId);
                }
            } else {
                log.warn("User not found with ID: {}", patientId);
                invalidPatientIds.add(patientId);
            }
        }
        
        // 如果有无效的患者ID，更新医生的患者列表
        if (!invalidPatientIds.isEmpty()) {
            doctor.getPatientIds().removeAll(invalidPatientIds);
            doctorRepository.save(doctor);
            log.debug("Removed {} invalid patient IDs from doctor's list", invalidPatientIds.size());
        }
        
        log.debug("Found {} valid patients", patients.size());
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/profile")
    public ResponseEntity<DoctorResponse> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();
        
        Doctor doctor = doctorRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
                
        return ResponseEntity.ok(convertToDoctorResponse(doctor));
    }

    /**
     * 绑定医生
     */
    @PostMapping("/bind/{doctorId}")
    public ResponseEntity<Void> bindDoctor(@PathVariable String doctorId) {
        // 获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();
        
        // 获取医生信息
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("医生不存在"));
                
        if (!doctor.isAvailable()) {
            throw new RuntimeException("医生不可用");
        }
        
        // 更新用户的绑定医生ID
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
                
        user.setBoundDoctorId(doctorId);
        userRepository.save(user);
        
        // 更新医生的患者列表
        List<String> patientIds = doctor.getPatientIds();
        if (patientIds == null) {
            patientIds = new ArrayList<>();
        }
        if (!patientIds.contains(currentUserId)) {
            patientIds.add(currentUserId);
            doctor.setPatientIds(patientIds);
            doctorRepository.save(doctor);
        }
        
        return ResponseEntity.ok().build();
    }

    /**
     * 解除绑定医生
     */
    @PostMapping("/unbind")
    public ResponseEntity<?> unbindDoctor() {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (currentUserId == null || currentUserId.isEmpty() || "anonymous".equals(currentUserId)) {
            return ResponseEntity.ok(null);
        }
        
        // 查找当前用户
        User currentUser = userRepository.findById(currentUserId)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        // 清除医生的绑定关系
        currentUser.setBoundDoctorId(null);
        userRepository.save(currentUser);
        
        return ResponseEntity.ok().build();
    }

    /**
     * 医生更新自己的信息
     */
    @PutMapping("/profile")
    public ResponseEntity<DoctorResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest doctorRequest) {
        
        // 获取当前用户ID
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        
        // 获取医生信息
        Doctor doctor = doctorRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new RuntimeException("医生不存在"));
        
        // 获取用户信息
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 更新用户信息
        if (doctorRequest.getName() != null) {
            user.setName(doctorRequest.getName());
            doctor.setName(doctorRequest.getName());
        }
        if (doctorRequest.getPhone() != null) {
            user.setPhone(doctorRequest.getPhone());
        }
        if (doctorRequest.getEmail() != null) {
            user.setEmail(doctorRequest.getEmail());
        }
        if (doctorRequest.getGender() != null) {
            user.setGender(doctorRequest.getGender());
            doctor.setGender(doctorRequest.getGender());
        }
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        // 更新医生信息
        if (doctorRequest.getDepartment() != null) {
            doctor.setDepartment(doctorRequest.getDepartment());
        }
        if (doctorRequest.getTitle() != null) {
            doctor.setTitle(doctorRequest.getTitle());
        }
        doctor.setUpdatedAt(LocalDateTime.now());
        
        Doctor updatedDoctor = doctorRepository.save(doctor);
        return ResponseEntity.ok(convertToDoctorResponse(updatedDoctor));
    }

    /**
     * 将医生实体转换为DoctorResponse DTO
     */
    private DoctorResponse convertToDoctorResponse(Doctor doctor) {
        User user = null;
        if (doctor.getUserId() != null) {
            user = userRepository.findById(doctor.getUserId()).orElse(null);
        }
        
        return DoctorResponse.builder()
                .id(doctor.getId())
                .userId(doctor.getUserId())
                .name(doctor.getName())
                .gender(doctor.getGender())
                .department(doctor.getDepartment())
                .specialty(doctor.getSpecialty())
                .title(doctor.getTitle())
                .available(doctor.isAvailable())
                .username(user != null ? user.getUsername() : null)
                .phone(user != null ? user.getPhone() : null)
                .email(user != null ? user.getEmail() : null)
                .patientCount(doctor.getPatientIds() != null ? doctor.getPatientIds().size() : 0)
                .createdAt(doctor.getCreatedAt())
                .updatedAt(doctor.getUpdatedAt())
                .build();
    }

    @GetMapping("/dashboard-stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();
        
        Doctor doctor = doctorRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
                
        Map<String, Object> stats = new HashMap<>();
        stats.put("patientCount", doctor.getPatientIds() != null ? doctor.getPatientIds().size() : 0);
        
        // 统计预测记录
        long totalPredictions = predictionRecordRepository.countByUserIdIn(doctor.getPatientIds());
        stats.put("totalPredictions", totalPredictions);
        
        // 统计未读消息
        long unreadMessages = messageRepository.countByToUserIdAndIsReadFalse(doctor.getId());
        stats.put("unreadMessages", unreadMessages);
        
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/patients/{patientId}/cases")
    public ResponseEntity<List<PredictionRecord>> getPatientCases(@PathVariable String patientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();
        
        log.debug("Getting cases for patient {} by doctor {}", patientId, currentUserId);
        
        // 验证当前医生是否有权限访问该患者的病例
        Doctor doctor = doctorRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
                
        if (!doctor.getPatientIds().contains(patientId)) {
            log.warn("Doctor {} does not have access to patient {}", currentUserId, patientId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // 获取患者的病例记录
        List<PredictionRecord> cases = predictionRecordRepository.findByUserIdOrderByCreatedAtDesc(patientId);
        return ResponseEntity.ok(cases);
    }

    /**
     * 获取患者详细信息
     */
    @GetMapping("/patients/{patientId}")
    public ResponseEntity<User> getPatientDetail(@PathVariable String patientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();
        
        log.debug("Getting patient details for {} by doctor {}", patientId, currentUserId);
        
        // 验证当前医生是否有权限访问该患者
        Doctor doctor = doctorRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
                
        if (!doctor.getPatientIds().contains(patientId)) {
            log.warn("Doctor {} does not have access to patient {}", currentUserId, patientId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // 获取患者信息
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
                
        return ResponseEntity.ok(patient);
    }
} 