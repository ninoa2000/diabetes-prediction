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
import java.util.Date;
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

    @PostMapping("/send/{receiverId}")
    public ResponseEntity<MessageResponse> sendMessage(
            @PathVariable String receiverId,
            @Valid @RequestBody MessageRequest messageRequest) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();
        
        User sender = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
        
        Doctor receiver = doctorRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
        
        Message message = new Message();
        message.setFromUserId(currentUserId);
        message.setFromUserName(sender.getName());
        message.setToUserId(receiverId);
        message.setToUserName(receiver.getName());
        message.setContent(messageRequest.getContent());
        
        // Set message type based on user roles
        if (sender.getRoles().contains(User.Role.ROLE_DOCTOR)) {
            message.setType(Message.MessageType.DOCTOR_TO_PATIENT);
        } else {
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();
        
        List<Message> messages = messageService.getMessagesBetweenUsers(currentUserId, userId);
        
        List<MessageResponse> responses = messages.stream()
                .map(MessageResponse::fromMessage)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/my-messages")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MessageResponse>> getMyMessages() {
        try {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();
            
            // 获取当前用户
            User currentUser = userRepository.findById(currentUserId)
                    .orElseThrow(() -> new RuntimeException("Current user not found"));
            
            // 如果是医生，使用医生ID查找消息
            if (currentUser.getRoles().contains(User.Role.ROLE_DOCTOR)) {
                Doctor doctor = doctorRepository.findByUserId(currentUserId)
                        .orElseThrow(() -> new RuntimeException("Doctor not found"));
                currentUserId = doctor.getId();
            }
        
        List<Message> messages = messageService.getMessagesByUserId(currentUserId);
        
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();
        
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();
        
        long unreadCount = messageService.getMessagesByUserId(currentUserId).stream()
                .filter(message -> message.getToUserId().equals(currentUserId) && !message.isRead())
                .count();
        
        return ResponseEntity.ok(unreadCount);
    }
} 