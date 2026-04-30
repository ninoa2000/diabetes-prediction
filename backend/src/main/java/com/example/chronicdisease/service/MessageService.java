package com.example.chronicdisease.service;

import com.example.chronicdisease.dto.message.MessageResponse;
import com.example.chronicdisease.model.Message;

import java.util.List;

public interface MessageService {
    Message createMessage(Message message);
    Message getMessageById(String messageId);
    List<Message> getMessagesBetweenUsers(String senderId, String receiverId);
    List<Message> getMessagesByUserId(String userId);
    void markMessageAsRead(String messageId);
    void deleteMessage(String messageId);
    Message updateMessage(Message message);
    MessageResponse replyToMessage(String messageId, String replyContent);
} 