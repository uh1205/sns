package com.sparta.sns.aop;

import com.sparta.sns.base.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ExceptionResponse> handleException(IllegalArgumentException e) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .msg(e.getMessage())
                .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
