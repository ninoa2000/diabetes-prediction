package com.example.chronicdisease.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
public class Message {
    
    @Id
    private String id;
    
    @NotBlank
    private String fromUserId;
    
    private String fromUserName;
    
    @NotBlank
    private String toUserId;
    
    private String toUserName;
    
    @NotBlank
    private String content;
    
    private String replyToMessageId;
    
    private boolean isRead = false;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    public enum MessageType {
        PATIENT_TO_DOCTOR,
        DOCTOR_TO_PATIENT
    }
    
    private MessageType type;
    
    private String replyContent;
} 