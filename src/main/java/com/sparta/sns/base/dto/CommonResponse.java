package com.sparta.sns.base.dto;

import lombok.Builder;

@Builder
public class CommonResponse<T> {

    private int statusCode;
    private String msg;
    private T data;

}