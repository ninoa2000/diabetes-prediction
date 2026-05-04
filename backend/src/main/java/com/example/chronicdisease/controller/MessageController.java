package com.example.chronicdisease.controller;

import com.example.chronicdisease.dto.message.MessageRequest;
import com.example.chronicdisease.dto.message.MessageResponse;
import com.example.chronicdisease.model.Message;
import com.example.chronicdisease.model.User;
import com.example.chronicdisease.model.Doctor;
import com.example.chronicdisease.repository.UserRepository;
import com.example.chronicdisease.repository.DoctorRepository;
import com.example.chronicdisease.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
            return null;
        }
        String principal = auth.getName();
        if (principal == null || "anonymousUser".equals(principal)) {
            return null;
        }
        User user = userRepository.findByUsername(principal).orElse(null);
        if (user == null) {
            user = userRepository.findById(principal).orElse(null);
        }
        return user;
    }

    @PostMapping("/send/{receiverId}")
    public ResponseEntity<MessageResponse> sendMessage(
            @PathVariable String receiverId,
            @Valid @RequestBody MessageRequest messageRequest) {
        
        User currentUser = getCurrentUser();
        if (currentUser == null) throw new RuntimeException("Current user not found");
        
        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setImageUrl(messageRequest.getImageUrl());
        
        // 如果发送者是医生
        if (currentUser.getRoles().contains(User.Role.ROLE_DOCTOR)) {
            Doctor doctor = doctorRepository.findByUserId(currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            
            message.setFromUserId(doctor.getId());
            message.setFromUserName(doctor.getName());
            
            // 接收者是患者 (User)
            User patient = userRepository.findById(receiverId)
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            message.setToUserId(patient.getId());
            message.setToUserName(patient.getName());
            message.setType(Message.MessageType.DOCTOR_TO_PATIENT);
        } else {
            // 发送者是患者 (User)
            message.setFromUserId(currentUser.getId());
            message.setFromUserName(currentUser.getName());
            
            // 接收者是医生
            Doctor doctor = doctorRepository.findById(receiverId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            message.setToUserId(doctor.getId());
            message.setToUserName(doctor.getName());
            message.setType(Message.MessageType.PATIENT_TO_DOCTOR);
        }
        
        Message savedMessage = messageService.createMessage(message);
        return new ResponseEntity<>(MessageResponse.fromMessage(savedMessage), HttpStatus.CREATED);
    }

    @PostMapping("/{messageId}/reply")
    public ResponseEntity<MessageResponse> replyToMessage(
            @PathVariable String messageId,
            @Valid @RequestBody MessageRequest messageRequest) {
        
        MessageResponse response = messageService.replyToMessage(messageId, messageRequest.getContent());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/between/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MessageResponse>> getMessagesBetweenUsers(@PathVariable String userId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) throw new RuntimeException("Current user not found");
        
        List<Message> messages = messageService.getMessagesBetweenUsers(currentUser.getId(), userId);
        
        List<MessageResponse> responses = messages.stream()
                .map(MessageResponse::fromMessage)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/my-messages")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MessageResponse>> getMyMessages() {
        try {
            User currentUser = getCurrentUser();
            if (currentUser == null) throw new RuntimeException("Current user not found");
            
            String lookupId = currentUser.getId();
            
            // 如果是医生，使用医生ID查找消息
            if (currentUser.getRoles().contains(User.Role.ROLE_DOCTOR)) {
                Doctor doctor = doctorRepository.findByUserId(currentUser.getId())
                        .orElseThrow(() -> new RuntimeException("Doctor not found"));
                lookupId = doctor.getId();
            }
        
            List<Message> messages = messageService.getMessagesByUserId(lookupId);
            
            List<MessageResponse> responses = messages.stream()
                    .map(MessageResponse::fromMessage)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting messages: {}", e.getMessage());
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PutMapping("/{messageId}/read")
    public ResponseEntity<Void> markMessageAsRead(@PathVariable String messageId) {
        messageService.markMessageAsRead(messageId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{messageId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteMessage(@PathVariable String messageId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) throw new RuntimeException("Current user not found");
        String currentUserId = currentUser.getId();
        
        Message message = messageService.getMessageById(messageId);
        
        // Only the sender or receiver can delete a message
        if (!message.getFromUserId().equals(currentUserId) && !message.getToUserId().equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unread-count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> getUnreadMessageCount() {
        User currentUser = getCurrentUser();
        if (currentUser == null) throw new RuntimeException("Current user not found");
        String currentUserId = currentUser.getId();
        
        long unreadCount = messageService.getMessagesByUserId(currentUserId).stream()
                .filter(message -> message.getToUserId().equals(currentUserId) && !message.isRead())
                .count();
        
        return ResponseEntity.ok(unreadCount);
    }
}