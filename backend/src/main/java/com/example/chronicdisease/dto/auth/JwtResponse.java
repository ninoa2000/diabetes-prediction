package com.example.chronicdisease.dto.auth;

import com.example.chronicdisease.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    private String name;
    private String email;
    private Set<String> roles;
    
    public static JwtResponse fromUserAndToken(User user, String token) {
        Set<String> roles = user.getRoles().stream()
                .map(User.Role::name)
                .collect(java.util.stream.Collectors.toSet());
        
        return JwtResponse.builder()
                .token(token)
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }
} 