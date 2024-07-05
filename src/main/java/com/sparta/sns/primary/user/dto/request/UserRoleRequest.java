package com.sparta.sns.primary.user.dto.request;

import com.sparta.sns.primary.user.entity.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRoleRequest {

    @NotNull
    private UserRole role;

}
