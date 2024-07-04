package com.sparta.sns.follow.controller;

import com.sparta.sns.follow.dto.FollowRequest;
import com.sparta.sns.follow.dto.FollowResponse;
import com.sparta.sns.follow.entity.Follow;
import com.sparta.sns.follow.service.FollowService;
import com.sparta.sns.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.sparta.sns.util.ControllerUtil.getFieldErrorResponseEntity;
import static com.sparta.sns.util.ControllerUtil.getResponseEntity;

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