package com.sparta.sns.exception;

public class AlreadyLikeException extends RuntimeException {

    public AlreadyLikeException() {
    }

    public AlreadyLikeException(String message) {
        super(message);
    }

}
