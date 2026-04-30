package com.example.chronicdisease.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {


    /** MongoDB 的 ObjectId */
    private String mongoId;
    private String id;
    private String username;
    private String fullName;
    private String phone;
    private String email;
    private Integer age;
    private String gender;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 