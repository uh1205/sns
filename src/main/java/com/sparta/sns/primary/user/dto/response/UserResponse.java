package com.sparta.sns.primary.user.dto.response;

import com.sparta.sns.primary.user.entity.User;
import com.sparta.sns.primary.user.entity.UserStatus;
import com.sparta.sns.primary.user.entity.UserRole;
import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String username;
    private String name;
    private String bio;
    private UserRole role;
    private UserStatus status;
    private int followersCount; // 팔로워 수
    private int followingCount; // 팔로잉 수
    private int likedPostsCount; // 좋아요한 게시물 수
    private int likedCommentsCount; // 좋아요한 댓글 수

    private UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.bio = user.getBio();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.followingCount = user.getFollowingCount();
        this.followersCount = user.getFollowersCount();
        this.likedPostsCount = user.getLikedPostsCount();
        this.likedCommentsCount = user.getLikedCommentsCount();
    }

    public static UserResponse of(User user) {
        return new UserResponse(user);
    }

}