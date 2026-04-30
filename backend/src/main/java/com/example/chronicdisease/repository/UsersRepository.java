package com.example.chronicdisease.repository;

import com.example.chronicdisease.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Integer> {
    // 用户名唯一校验（如果还需要）
    boolean existsByUsername(String username);



    Optional<User> findByPhone(String phone);
    boolean existsByPhone(String phone);
    // 手机号唯一校验

    // 根据手机号查用户（register 中需要用到）

    Optional<User> findByUsername(String username);
    // 如果登录也是手机号，可再加：
    // Optional<User> findByPhone(String phone);
}
