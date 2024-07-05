package com.sparta.sns.primary.like.controller;

import com.sparta.sns.secondary.base.dto.CommonResponse;
import com.sparta.sns.primary.like.dto.LikeRequest;
import com.sparta.sns.primary.like.dto.LikeResponse;
import com.sparta.sns.primary.like.entity.Like;
import com.sparta.sns.primary.like.service.LikeService;
import com.sparta.sns.secondary.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.sparta.sns.secondary.util.ControllerUtil.*;

/**
 * 한 가지 동작에 대해서는 한 가지 API만을 가지고 있고,
 * 프론트에서 좋아요 버튼의 상태에 따라 다른 요청을 보내는 것이 더 RESTful한 설계라고 한다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    /**
     * 좋아요
     */
    @PostMapping
    public ResponseEntity<CommonResponse<?>> like(
            @Valid @RequestBody LikeRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "좋아요 실패");
        }
        Like like = likeService.like(request, userDetails.getUser());

        return getResponseEntity(LikeResponse.of(like), "좋아요 성공");
    }

    /**
     * 좋아요 취소
     */
    @DeleteMapping
    public ResponseEntity<CommonResponse<?>> unlike(
            @Valid @RequestBody LikeRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "좋아요 취소 실패");
        }
        Long response = likeService.unlike(request, userDetails.getUser());

        return getResponseEntity(response, "좋아요 취소 성공");
    }

}
