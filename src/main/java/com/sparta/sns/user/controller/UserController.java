package com.sparta.sns.user.controller;

import com.sparta.sns.base.dto.CommonResponse;
import com.sparta.sns.security.UserDetailsImpl;
import com.sparta.sns.user.dto.response.UserResponse;
import com.sparta.sns.user.dto.request.RoleRequest;
import com.sparta.sns.user.dto.request.SignupRequest;
import com.sparta.sns.user.dto.request.UpdatePasswordRequest;
import com.sparta.sns.user.dto.request.UpdateProfileRequest;
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

import static com.sparta.sns.util.ControllerUtil.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<?>> signup(
            @Valid @RequestBody SignupRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "회원가입 실패");
        }
        User user = userService.signup(request);

        return getResponseEntity(UserResponse.from(user), "회원가입 성공");
    }

    /**
     * 로그아웃
     */
    @GetMapping("/logout")
    public ResponseEntity<CommonResponse<?>> logout(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long response = userService.logout(userDetails.getUser());

        return getResponseEntity(response, "로그아웃 성공");
    }

    /**
     * 전체 회원 조회 (관리자 전용)
     */
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<CommonResponse<?>> findAllUsers(
            @PageableDefault(
                    sort = "createdAt",
                    size = 5,
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        Page<User> page = userService.findAllUsers(pageable);
        Page<UserResponse> response = page.map(UserResponse::from);

        return getResponseEntity(response, "전체 회원 조회 성공");
    }

    /**
     * 회원 프로필 조회
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<CommonResponse<?>> findUser(
            @PathVariable Long userId
    ) {
        User user = userService.findUser(userId);

        return getResponseEntity(UserResponse.from(user), "프로필 조회 성공");
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

        return getResponseEntity(UserResponse.from(user), "프로필 수정 성공");
    }

    /**
     * 회원 비밀번호 수정
     */
    @PatchMapping("/users/{userId}/password")
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

        return getResponseEntity(UserResponse.from(user), "비빌번호 변경 성공");
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

        User user = userService.updateRole(userId, request);

        return getResponseEntity(UserResponse.from(user), "회원 권한 수정 성공");
    }

}
