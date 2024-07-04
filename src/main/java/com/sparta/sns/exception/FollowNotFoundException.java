package com.sparta.sns.exception;

public class FollowNotFoundException extends RuntimeException {

    public FollowNotFoundException(Long followerId, Long followingId) {
        super("Follow Not Found With FollowerId : " + followerId + " , FollowingId : " + followingId);
    }

}
