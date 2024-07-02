package com.sparta.sns.exception;

public class DifferentPostException extends RuntimeException {

    public DifferentPostException() {
    }

    public DifferentPostException(String message) {
        super(message);
    }

}
