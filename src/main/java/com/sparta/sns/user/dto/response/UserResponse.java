package com.sparta.sns.user.dto.response;

import com.sparta.sns.user.entity.User;
import com.sparta.sns.user.entity.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {

    private Long id;
    private String username;
    private String name;
    private String bio;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.bio = user.getBio();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

    public static UserResponse of(User user) {
        return new UserResponse(user);
    }

}