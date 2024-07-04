package com.sparta.sns.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DisableRequest {

    @NotBlank
    private String password;

}
