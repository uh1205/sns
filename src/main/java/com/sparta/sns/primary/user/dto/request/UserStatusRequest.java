package com.sparta.sns.primary.user.dto.request;

import com.sparta.sns.primary.user.entity.UserStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserStatusRequest {

    @NotBlank
    private UserStatus status;

    @NotBlank
    private String password;

}
