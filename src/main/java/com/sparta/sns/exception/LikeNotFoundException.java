package com.sparta.sns.exception;

import com.sparta.sns.like.entity.ContentType;

public class LikeNotFoundException extends RuntimeException {

    public LikeNotFoundException(ContentType contentType, Long invalidId) {
        super("Like Not Found With ContentType : " + contentType + " , Id : " + invalidId);
    }

}
