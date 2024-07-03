package com.sparta.sns.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("해당 회원을 찾을 수 없습니다.");
    }

    public UserNotFoundException(String s) {
        super(s);
    }

}
