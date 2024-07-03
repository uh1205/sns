package com.sparta.sns.like.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum ContentType {

    POST,
    COMMENT;

    @JsonCreator
    public static ContentType parsing(String inputValue) {
        return Stream.of(ContentType.values())
                .filter(contentType -> contentType.toString().equals(inputValue.toUpperCase()))
                .findFirst()
                .orElse(null);
    }

}
