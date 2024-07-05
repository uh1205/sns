package com.sparta.sns.secondary.exception;

public class DifferentUserException extends RuntimeException {

    public DifferentUserException() {
        super("회원이 일치하지 않습니다.");
    }

    public DifferentUserException(String message) {
        super(message);
    }

}
