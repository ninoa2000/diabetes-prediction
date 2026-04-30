package com.example.chronicdisease.service.impl;

import com.example.chronicdisease.dto.auth.JwtResponse;
import com.example.chronicdisease.dto.auth.LoginRequest;
import com.example.chronicdisease.dto.auth.RegisterRequest;
import com.example.chronicdisease.dto.user.ChangePasswordRequest;
import com.example.chronicdisease.dto.user.UserResponse;
import com.example.chronicdisease.exception.UnauthorizedException;
import com.example.chronicdisease.model.User;
import com.example.chronicdisease.repository.UserRepository;
import com.example.chronicdisease.repository.UsersRepository;    // MySQL 仓库
import com.example.chronicdisease.security.JwtTokenProvider;
import com.example.chronicdisease.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UsersRepository usersRepository;       // MySQL

    @Autowired
    private UserRepository userRepository;         // Mongo

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 注册：写 MySQL
     */
    @Override
    public User register(RegisterRequest req) {
        // 1) MySQL 唯一校验
        if (usersRepository.existsByPhone(req.getPhone())) {
            throw new IllegalArgumentException("手机号已被注册");
        }
        // 2) 写入 MySQL
        com.example.chronicdisease.entity.User sql = new com.example.chronicdisease.entity.User();
        sql.setUsername(req.getUsername());
        sql.setPassword(passwordEncoder.encode(req.getPassword()));
        sql.setPhone(req.getPhone());
        sql.setEmail(req.getEmail());
        sql.setName(req.getName());
        sql.setActive(true);
        sql.setRoles("[\"ROLE_USER\"]");
        LocalDateTime now = LocalDateTime.now();
        sql.setCreatedAt(now);
        sql.setUpdatedAt(now);
        usersRepository.save(sql);

        // 3) 写入
        User mongo = new User();
        mongo.setUsername(req.getUsername());
        mongo.setPassword(sql.getPassword());
        mongo.setPhone(req.getPhone());
        mongo.setEmail(req.getEmail());
        mongo.setName(req.getName());
        mongo.setActive(true);
        mongo.setRoles(Collections.singleton(User.Role.ROLE_USER));
        mongo.setCreatedAt(now);
        mongo.setUpdatedAt(now);
        // 保存并拿到真正的 ID
        mongo = userRepository.save(mongo);

        // 4) 返回带 ID 的模型
        return mongo;
    }

    @Override
    public boolean changePassword(ChangePasswordRequest changePasswordRequest) {
        // —— 1) 试从 MySQL 查 —— //
        com.example.chronicdisease.entity.User sqlUser =
                usersRepository.findByUsername(changePasswordRequest.getUserName())
                        .orElseThrow();
        sqlUser.setPassword(new BCryptPasswordEncoder().encode(changePasswordRequest.getNewPassword()));
        com.example.chronicdisease.entity.User savedUser = usersRepository.save(sqlUser);
        return Objects.equals(savedUser.getUsername(),changePasswordRequest.getNewPassword());
    }

    /**
     * 登录：先读 MySQL 验证，拿 ID
     */
    @Override
    public JwtResponse login(LoginRequest req) {
        // —— 1) 试从 MySQL 查 —— //
        com.example.chronicdisease.entity.User sqlUser =
                usersRepository.findByUsername(req.getUsername())
                        .orElseGet(() -> {
                            // 如果 MySQL 没有，去 Mongo 查，写入 MySQL
                            User mongo = userRepository.findByUsername(req.getUsername())
                                    .orElseThrow(() -> new UnauthorizedException("账号或密码错误"));
                            var e = new com.example.chronicdisease.entity.User();
                            e.setUsername(mongo.getUsername());
                            e.setPassword(mongo.getPassword());
                            e.setPhone(mongo.getPhone());
                            e.setEmail(mongo.getEmail());
                            e.setName(mongo.getName());
                            e.setActive(mongo.isActive());
                            e.setRoles("[\"ROLE_USER\"]");
                            e.setCreatedAt(mongo.getCreatedAt());
                            e.setUpdatedAt(mongo.getUpdatedAt());
                            return usersRepository.save(e);
                        });

        // —— 2) 用 sqlUser 验证密码 —— //
        if (!sqlUser.getActive() ||
                !passwordEncoder.matches(req.getPassword(), sqlUser.getPassword())) {
            throw new UnauthorizedException("账号或密码错误");
        }

        // —— 3) 再去 Mongo 拿原 ID —— //
        User mongoUser = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new UnauthorizedException("账号或密码错误"));

        // —— 4) 生成 JWT 并返回 —— //
        Authentication auth = new UsernamePasswordAuthenticationToken(
                mongoUser.getUsername(), null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        String token = jwtTokenProvider.generateToken(auth);
        return JwtResponse.fromUserAndToken(mongoUser, token);
    }
}

