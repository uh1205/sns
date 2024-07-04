package com.sparta.sns.exception;

public class SelfFollowException extends RuntimeException {

    public SelfFollowException() {
        super("본인을 팔로우 할 수 없습니다.");
    }

}
