package com.sparta.sns.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Long invalidId) {
        super("Post Not Found With Id : " + invalidId);
    }

}
