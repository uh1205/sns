package com.sparta.sns.like.controller;

import com.sparta.sns.base.dto.CommonResponse;
import com.sparta.sns.like.dto.LikeRequest;
import com.sparta.sns.like.dto.LikeResponse;
import com.sparta.sns.like.entity.Like;
import com.sparta.sns.like.service.LikeService;
import com.sparta.sns.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.sparta.sns.util.ControllerUtil.*;

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
     * 좋아요 수행
     */
    @PostMapping
    public ResponseEntity<CommonResponse<?>> doLike(
            @Valid @RequestBody LikeRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "좋아요 수행 실패");
        }
        Like like = likeService.doLike(request, userDetails.getUser());

        return getResponseEntity(LikeResponse.of(like), "좋아요 수행 성공");
    }

    /**
     * 좋아요 취소
     */
    @DeleteMapping
    public ResponseEntity<CommonResponse<?>> cancelLike(
            @Valid @RequestBody LikeRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "좋아요 취소 실패");
        }
        Long response = likeService.cancelLike(request, userDetails.getUser());

        return getResponseEntity(response, "좋아요 취소 성공");
    }

}
