package com.example.chronicdisease.service.impl;

import com.example.chronicdisease.dto.message.MessageResponse;
import com.example.chronicdisease.exception.ResourceNotFoundException;
import com.example.chronicdisease.model.Message;
import com.example.chronicdisease.repository.MessageRepository;
import com.example.chronicdisease.repository.UserRepository;
import com.example.chronicdisease.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public Message createMessage(Message message) {
        message.setCreatedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    public Message getMessageById(String id) {
        return messageRepository.findById(id).orElse(null);
    }

    @Override
    public List<Message> getMessagesByUserId(String userId) {
        List<Message> sentMessages = messageRepository.findByFromUserIdOrderByCreatedAtDesc(userId);
        List<Message> receivedMessages = messageRepository.findByToUserIdOrderByCreatedAtDesc(userId);
        
        return Stream.concat(sentMessages.stream(), receivedMessages.stream())
                .sorted((m1, m2) -> m2.getCreatedAt().compareTo(m1.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessagesBetweenUsers(String userId1, String userId2) {
        List<Message> sentMessages = messageRepository.findByFromUserIdAndToUserIdOrderByCreatedAtDesc(userId1, userId2);
        List<Message> receivedMessages = messageRepository.findByFromUserIdAndToUserIdOrderByCreatedAtDesc(userId2, userId1);
        
        return Stream.concat(sentMessages.stream(), receivedMessages.stream())
                .sorted((m1, m2) -> m2.getCreatedAt().compareTo(m1.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public void markMessageAsRead(String messageId) {
        Message message = getMessageById(messageId);
        if (message != null) {
            message.setRead(true);
            message.setUpdatedAt(LocalDateTime.now());
            messageRepository.save(message);
        }
    }

    @Override
    public void deleteMessage(String messageId) {
        messageRepository.deleteById(messageId);
    }

    @Override
    public Message updateMessage(Message message) {
        message.setUpdatedAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    public MessageResponse replyToMessage(String messageId, String replyContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        
        if (message.isRead()) {
            throw new RuntimeException("Message already replied");
        }
        
        message.setRead(true);
        message.setReplyContent(replyContent);
        message.setUpdatedAt(LocalDateTime.now());
        
        Message savedMessage = messageRepository.save(message);
        return MessageResponse.fromMessage(savedMessage);
    }
} 