package com.sparta.sns.exception;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException() {
        super("해당 댓글을 찾을 수 없습니다.");
    }

    public CommentNotFoundException(String message) {
        super(message);
    }

}
