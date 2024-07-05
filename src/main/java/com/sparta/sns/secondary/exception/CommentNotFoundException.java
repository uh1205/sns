package com.sparta.sns.secondary.exception;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(Long invalidId) {
        super("Comment Not Found With Id : " + invalidId);
    }

}
