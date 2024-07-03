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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sparta.sns.util.ControllerUtil.getFieldErrorResponseEntity;
import static com.sparta.sns.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    /**
     * 좋아요 토글
     */
    @PostMapping
    public ResponseEntity<CommonResponse<?>> toggleLike(
            @Valid @RequestBody LikeRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "좋아요 토글 실패");
        }
        Like like = likeService.toggleLike(request, userDetails.getUser());

        return getResponseEntity(LikeResponse.of(like), "좋아요 토글 성공");
    }

}
