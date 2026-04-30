package com.example.chronicdisease.repository;

import com.example.chronicdisease.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    
    List<Message> findByFromUserIdOrderByCreatedAtDesc(String fromUserId);
    
    List<Message> findByToUserIdOrderByCreatedAtDesc(String toUserId);
    
    List<Message> findByFromUserIdAndToUserIdOrderByCreatedAtDesc(String fromUserId, String toUserId);
    
    List<Message> findByToUserIdAndIsReadFalseOrderByCreatedAtDesc(String toUserId);
    
    long countByToUserIdAndIsReadFalse(String toUserId);
} 