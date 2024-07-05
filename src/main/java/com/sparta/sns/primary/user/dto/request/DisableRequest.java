package com.sparta.sns.primary.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DisableRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String password;

}
