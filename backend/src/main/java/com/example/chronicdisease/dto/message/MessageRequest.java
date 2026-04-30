package com.example.chronicdisease.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    
    @NotBlank(message = "消息内容不能为空")
    @Size(min = 1, max = 1000, message = "消息长度必须在1到1000个字符之间")
    private String content;
} 