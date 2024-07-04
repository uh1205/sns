package com.sparta.sns.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long invalidId) {
        super("User Not Found With Id : " + invalidId);
    }

}
