package com.sparta.sns.exception;

public class DifferentUserException extends RuntimeException {

    public DifferentUserException() {
    }

    public DifferentUserException(String message) {
        super(message);
    }

}
