package com.example.chronicdisease.repository;

import com.example.chronicdisease.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Boolean existsByUsername(String username);

    Optional<User> findByPhone(String phone);

    boolean existsByPhone(String phone);
    Boolean existsByEmail(String email);
    
    Set<User> findByRolesContaining(User.Role role);
    
    Set<User> findByBoundDoctorId(String doctorId);
} 