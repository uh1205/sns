package com.sparta.sns.util;

import com.sparta.sns.base.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Slf4j
public final class ControllerUtil {

    public static ResponseEntity<CommonResponse<?>> getFieldErrorResponseEntity(BindingResult bindingResult, String msg) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            log.error("{} field : {}", fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest()
                .body(CommonResponse.<List<FieldError>>builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .msg(msg)
                        .data(fieldErrors)
                        .build());
    }

    public static ResponseEntity<CommonResponse<?>> getResponseEntity(Object response, String msg) {
        return ResponseEntity.ok().body(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .msg(msg)
                .data(response)
                .build());
    }

    public static void validatePathIdWithBody(Long pathId, Long bodyId) {
        if (!pathId.equals(bodyId)) {
            throw new IllegalArgumentException("PathVariable의 id가 RequestBody의 id와 일치하지 않습니다.");
        }
    }

}
