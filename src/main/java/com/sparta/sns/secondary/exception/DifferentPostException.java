package com.sparta.sns.secondary.exception;

public class DifferentPostException extends RuntimeException {

    public DifferentPostException() {
        super("게시물이 일치하지 않습니다.");
    }

    public DifferentPostException(String message) {
        super(message);
    }

}
