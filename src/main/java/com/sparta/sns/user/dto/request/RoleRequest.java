package com.sparta.sns.user.dto.request;

import com.sparta.sns.user.entity.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleRequest {

    @NotNull
    private UserRole role;

}
