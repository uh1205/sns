package com.sparta.sns.secondary.exception;

public class SelfUnfollowException extends RuntimeException {

    public SelfUnfollowException() {
        super("본인을 언팔로우 할 수 없습니다.");
    }

}
