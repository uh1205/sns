package com.sparta.sns.user.controller;

import com.sparta.sns.base.dto.CommonResponse;
import com.sparta.sns.comment.dto.CommentResponse;
import com.sparta.sns.comment.entity.Comment;
import com.sparta.sns.comment.service.CommentService;
import com.sparta.sns.post.dto.PostResponse;
import com.sparta.sns.post.entity.Post;
import com.sparta.sns.post.service.PostService;
import com.sparta.sns.security.UserDetailsImpl;
import com.sparta.sns.user.dto.request.*;
import com.sparta.sns.user.dto.response.UserResponse;
import com.sparta.sns.user.entity.User;
import com.sparta.sns.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.sns.util.ControllerUtil.getFieldErrorResponseEntity;
import static com.sparta.sns.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    /**
     * 회원가입
     */
    @PostMapping("/user/signup")
    public ResponseEntity<CommonResponse<?>> signup(
            @Valid @RequestBody SignupRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "회원가입 실패");
        }
        User user = userService.signup(request);

        return getResponseEntity(UserResponse.of(user), "회원가입 성공");
    }

    /**
     * 회원 비활성화
     */
    @PostMapping("/user/disable")
    public ResponseEntity<CommonResponse<?>> disable(
            @Valid @RequestBody DisableRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "회원 비활성화 실패");
        }
        Long response = userService.disable(request, userDetails.getUser());

        return getResponseEntity(response, "회원 비활성화 성공");
    }

    /**
     * 로그아웃
     */
    @PostMapping("/user/logout")
    public ResponseEntity<CommonResponse<?>> logout(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long response = userService.logout(userDetails.getUser());

        return getResponseEntity(response, "로그아웃 성공");
    }

    /**
     * 좋아요한 전체 게시물 조회
     */
    @GetMapping("/user/likes/posts")
    public ResponseEntity<CommonResponse<?>> getLikedPosts(
            @PageableDefault(
                    size = 5
            ) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Page<Post> page = postService.getLikedPosts(userDetails.getUser(), pageable);
        Page<PostResponse> response = page.map(PostResponse::of);

        return getResponseEntity(response, "좋아요한 전체 게시물 조회 성공");
    }

    /**
     * 좋아요한 전체 댓글 조회
     */
    @GetMapping("/user/likes/comments")
    public ResponseEntity<CommonResponse<?>> getLikedComments(
            @PageableDefault(
                    size = 5
            ) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Page<Comment> page = commentService.getLikedComments(userDetails.getUser(), pageable);
        Page<CommentResponse> response = page.map(CommentResponse::of);

        return getResponseEntity(response, "좋아요한 전체 댓글 조회 성공");
    }

    /**
     * 전체 회원 조회 (관리자 전용)
     */
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<CommonResponse<?>> getAllUsers(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        Page<User> page = userService.getAllUsers(pageable);
        Page<UserResponse> response = page.map(UserResponse::of);

        return getResponseEntity(response, "전체 회원 조회 성공");
    }

    /**
     * 회원 프로필 조회
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<CommonResponse<?>> getUser(
            @PathVariable Long userId
    ) {
        User user = userService.getUser(userId);

        return getResponseEntity(UserResponse.of(user), "프로필 조회 성공");
    }

    /**
     * 회원 프로필 수정
     */
    @PutMapping("/users/{userId}")
    public ResponseEntity<CommonResponse<?>> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateProfileRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "프로필 수정 실패");
        }
        User user = userService.updateProfile(userId, request, userDetails.getUser());

        return getResponseEntity(UserResponse.of(user), "프로필 수정 성공");
    }

    /**
     * 회원 비밀번호 수정
     */
    @PatchMapping("/users/{userId}/")
    public ResponseEntity<CommonResponse<?>> updatePassword(
            @PathVariable Long userId,
            @Valid @RequestBody UpdatePasswordRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "비밀번호 변경 실패");
        }
        User user = userService.updatePassword(userId, request, userDetails.getUser());

        return getResponseEntity(UserResponse.of(user), "비빌번호 변경 성공");
    }

    /**
     * 회원 권한 수정 (관리자 전용)
     */
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @PatchMapping("/roles/{userId}/")
    public ResponseEntity<CommonResponse<?>> updateRole(
            @PathVariable Long userId,
            @Valid @RequestBody RoleRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "회원 권한 수정 실패");
        }
        User user = userService.updateRole(userId, request.getRole());

        return getResponseEntity(UserResponse.of(user), "회원 권한 수정 성공");
    }

}
