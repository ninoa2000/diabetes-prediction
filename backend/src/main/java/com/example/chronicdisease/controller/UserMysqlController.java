package com.example.chronicdisease.controller;

import com.example.chronicdisease.entity.User;
import com.example.chronicdisease.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/mysql/user")
@RequiredArgsConstructor
public class UserMysqlController {

    private final UsersRepository usersRepository;

    /**
     * 注册用户
     */
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return usersRepository.save(user);
    }

    /**
     * 查询所有用户（支持分页）
     */
    @GetMapping
    public Map<String, Object> getAllUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction) {

        if (page == null && size == null) {
            List<User> users = usersRepository.findAll();
            return Map.of("users", users);
        }

        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 10;
        String sortField = sortBy != null ? sortBy : "createdAt";
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortField));
        Page<User> userPage = usersRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("users", userPage.getContent());
        response.put("currentPage", userPage.getNumber());
        response.put("totalItems", userPage.getTotalElements());
        response.put("totalPages", userPage.getTotalPages());

        return response;
    }

    /**
     * 获取指定 ID 的用户信息
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody User updateData) {

        User user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (updateData.getName() != null) user.setName(updateData.getName());
        if (updateData.getPhone() != null) user.setPhone(updateData.getPhone());
        if (updateData.getEmail() != null) user.setEmail(updateData.getEmail());
        if (updateData.getUsername() != null) user.setUsername(updateData.getUsername());
        if (updateData.getPassword() != null) user.setPassword(updateData.getPassword());
        if (updateData.getRoles() != null) user.setRoles(updateData.getRoles());
        if (updateData.getActive() != null) user.setActive(updateData.getActive());

        user.setUpdatedAt(LocalDateTime.now());

        return usersRepository.save(user);
    }
}
