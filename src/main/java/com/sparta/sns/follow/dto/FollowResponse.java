package com.sparta.sns.follow.dto;

import com.sparta.sns.follow.entity.Follow;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowResponse {

    private Long id;
    private Long followerId;
    private Long followingId;
    private LocalDateTime followedAt;

    private FollowResponse(Follow follow) {
        this.id = follow.getId();
        this.followerId = follow.getFollower().getId();
        this.followingId = follow.getFollowing().getId();
        this.followedAt = follow.getFollowedAt();
    }

    public static FollowResponse of(Follow follow) {
        return new FollowResponse(follow);
    }

}
