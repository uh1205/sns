package com.sparta.sns.secondary.exception;

import com.sparta.sns.primary.like.entity.ContentType;

public class LikeNotFoundException extends RuntimeException {

    public LikeNotFoundException(ContentType contentType, Long invalidId) {
        super("Like Not Found With ContentType : " + contentType + " , Id : " + invalidId);
    }

}
