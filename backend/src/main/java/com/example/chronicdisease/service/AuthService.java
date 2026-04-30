package com.example.chronicdisease.service;

import com.example.chronicdisease.dto.auth.JwtResponse;
import com.example.chronicdisease.dto.auth.LoginRequest;
import com.example.chronicdisease.dto.auth.RegisterRequest;
import com.example.chronicdisease.dto.user.ChangePasswordRequest;
import com.example.chronicdisease.model.User;

public interface AuthService {
    JwtResponse login(LoginRequest loginRequest);
    User register(RegisterRequest registerRequest);

    boolean changePassword(ChangePasswordRequest changePasswordRequest);
} 