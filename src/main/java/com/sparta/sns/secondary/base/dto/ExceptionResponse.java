package com.sparta.sns.secondary.base.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionResponse {

    private int statusCode;
    private String msg;

}
