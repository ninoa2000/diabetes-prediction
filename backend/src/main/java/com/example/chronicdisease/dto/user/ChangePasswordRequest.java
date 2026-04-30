package com.example.chronicdisease.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Getter
@Setter
public class ChangePasswordRequest {
    private String userName;
    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 32, message = "新密码长度应在6到32位之间")
    private String newPassword;

    @NotBlank(message = "请确认新密码")
    private String confirmPassword;
}
