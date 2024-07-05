package com.sparta.sns.primary.follow.controller;

import com.sparta.sns.primary.follow.dto.FollowRequest;
import com.sparta.sns.primary.follow.dto.FollowResponse;
import com.sparta.sns.primary.follow.entity.Follow;
import com.sparta.sns.primary.follow.service.FollowService;
import com.sparta.sns.secondary.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.sparta.sns.secondary.util.ControllerUtil.getFieldErrorResponseEntity;
import static com.sparta.sns.secondary.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FollowController {

    private final FollowService followService;

    @PostMapping
    public ResponseEntity<?> followUser(
            @Valid @RequestBody FollowRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "팔로우 실패");
        }
        Follow follow = followService.followUser(request, userDetails.getUser());

        return getResponseEntity(FollowResponse.of(follow), "팔로우 성공");
    }

    @DeleteMapping
    public ResponseEntity<?> unfollowUser(
            @Valid @RequestBody FollowRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "언팔로우 실패");
        }
        Long response = followService.unfollowUser(request, userDetails.getUser());

        return getResponseEntity(response, "언팔로우 성공");
    }

}