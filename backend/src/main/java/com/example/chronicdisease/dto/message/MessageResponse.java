package com.example.chronicdisease.dto.message;

import com.example.chronicdisease.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private String id;
    private String fromUserId;
    private String fromUserName;
    private String toUserId;
    private String toUserName;
    private String content;
    private boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Message.MessageType type;
    private String replyContent;
    
    public static MessageResponse fromMessage(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .fromUserId(message.getFromUserId())
                .fromUserName(message.getFromUserName())
                .toUserId(message.getToUserId())
                .toUserName(message.getToUserName())
                .content(message.getContent())
                .isRead(message.isRead())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .type(message.getType())
                .replyContent(message.getReplyContent())
                .build();
    }
} 