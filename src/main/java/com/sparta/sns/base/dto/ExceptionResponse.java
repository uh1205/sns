package com.sparta.sns.base.dto;

import lombok.Builder;

@Builder
public class ExceptionResponse {

    private int statusCode;
    private String msg;

}
