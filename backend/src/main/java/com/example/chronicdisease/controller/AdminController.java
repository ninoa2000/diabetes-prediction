package com.example.chronicdisease.controller;

import com.example.chronicdisease.dto.user.UserRequest;
import com.example.chronicdisease.dto.user.UpdateProfileRequest;
import com.example.chronicdisease.dto.user.UserResponse;
import com.example.chronicdisease.entity.User;                    // MySQL 实体
import com.example.chronicdisease.repository.UsersRepository;   // MySQL 仓库
import com.example.chronicdisease.repository.UserRepository;    // ★ Mongo 仓库
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UsersRepository usersRepository;   // MySQL
    private final UserRepository  userRepository;    // ★ Mongo
    private final PasswordEncoder passwordEncoder;

    /* ---------- 1. 列表 ---------- */
    @GetMapping
    public ResponseEntity<List<UserResponse>> listAll() {
        List<UserResponse> list = usersRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    /* ---------- 2. 新增 ---------- */
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest req) {
        if (usersRepository.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setName(req.getFullName());
        u.setPhone(req.getPhone());
        u.setEmail(req.getEmail());
        u.setActive(req.isActive());
        u.setCreatedAt(LocalDateTime.now());
        u.setUpdatedAt(LocalDateTime.now());
        User saved = usersRepository.save(u);
        return ResponseEntity.status(201).body(toDto(saved));
    }

    /* ---------- 3. 修改 ---------- */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateProfileRequest req) {

        return usersRepository.findById(id).map(u -> {
            if (req.getName()  != null) u.setName(req.getName());
            if (req.getPhone() != null) u.setPhone(req.getPhone());
            if (req.getEmail() != null) u.setEmail(req.getEmail());
            u.setUpdatedAt(LocalDateTime.now());
            usersRepository.save(u);
            return ResponseEntity.ok(toDto(u));
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).<UserResponse>build());
    }

    /* ---------- 4. 启用 / 禁用 ---------- */
    @PutMapping("/{id}/status")
    public ResponseEntity<UserResponse> toggleActive(
            @PathVariable Integer id,
            @RequestParam boolean active) {

        return usersRepository.findById(id).map(u -> {
            u.setActive(active);
            u.setUpdatedAt(LocalDateTime.now());
            usersRepository.save(u);
            return ResponseEntity.ok(toDto(u));
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).<UserResponse>build());
    }

    /* ---------- 5. 删除（同步删 MySQL + Mongo） ---------- */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {

        // ① 先查 MySQL
        com.example.chronicdisease.entity.User sqlUser = usersRepository.findById(id)
                .orElse(null);

        if (sqlUser == null) {
            // ---- 404 Not Found ----
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String username = sqlUser.getUsername();   // 也可以用 phone

        // ② 删 MySQL
        usersRepository.deleteById(id);

        // ③ 删 Mongo（按 username 找到 ObjectId 再删）
        userRepository.findByUsername(username)
                .ifPresent(m -> userRepository.deleteById(m.getId()));

        // ④ 204 No Content
        return ResponseEntity.noContent().build();
    }


    /* ---------- DTO 映射 ---------- */
    private UserResponse toDto(User u) {
        return UserResponse.builder()
                .id(u.getId().toString())
                .mongoId(null)              // 前端需要可自行填充
                .username(u.getUsername())
                .fullName(u.getName())
                .phone(u.getPhone())
                .email(u.getEmail())
                .active(u.getActive())
                .createdAt(u.getCreatedAt())
                .updatedAt(u.getUpdatedAt())
                .build();
    }
}
