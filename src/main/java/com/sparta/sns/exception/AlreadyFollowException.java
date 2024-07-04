package com.sparta.sns.exception;

public class AlreadyFollowException extends RuntimeException {

    public AlreadyFollowException() {
    }

    public AlreadyFollowException(String message) {
        super(message);
    }

}
