package com.sparta.sns.follow.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FollowRequest {

    @NotNull
    private Long followingId;

}
