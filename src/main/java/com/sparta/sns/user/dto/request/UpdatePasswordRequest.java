package com.sparta.sns.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordRequest {

    @NotBlank
    private String currentPassword; // 현재 비밀번호

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,15}$",
            message = "비밀번호는 최소 8자 이상, 15자 이하이어야 하며, 영문 대소문자, 숫자, 특수문자를 최소 1글자씩 포함해야 합니다."
    )
    @Size(min = 8, max = 15, message = "비밀번호는 최소 8자 이상, 15자 이하이어야 합니다.")
    private String newPassword; // 새 비밀번호

    @NotBlank
    private String retypedNewPassword; // 새 비밀번호 재입력

}
