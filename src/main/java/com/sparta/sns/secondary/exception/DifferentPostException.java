package com.sparta.sns.secondary.exception;

public class DifferentPostException extends RuntimeException {

    public DifferentPostException() {
    }

    public DifferentPostException(String message) {
        super(message);
    }

}
