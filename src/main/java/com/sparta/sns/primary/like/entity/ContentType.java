package com.sparta.sns.primary.like.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum ContentType {

    POST,
    COMMENT;

    @JsonCreator
    public static ContentType of(String inputValue) {
        return Stream.of(ContentType.values())
                .filter(contentType -> contentType.toString().equals(inputValue.toUpperCase()))
                .findFirst()
                .orElse(null);
    }

}
