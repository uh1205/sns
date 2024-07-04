package com.sparta.sns.user.service;

import com.sparta.sns.exception.PasswordNotMatchException;
import com.sparta.sns.exception.UserNotFoundException;
import com.sparta.sns.user.dto.request.SignupRequest;
import com.sparta.sns.user.dto.request.UpdatePasswordRequest;
import com.sparta.sns.user.dto.request.UpdateProfileRequest;
import com.sparta.sns.user.dto.request.DisableRequest;
import com.sparta.sns.user.entity.User;
import com.sparta.sns.user.entity.UserRole;
import com.sparta.sns.user.entity.UserStatus;
import com.sparta.sns.user.repository.RefreshTokenRepository;
import com.sparta.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository; // 추후 Redis 사용

    @Value("${admin.token}")
    private String adminToken;

    /**
     * 회원가입
     */
    @Transactional
    public User signup(SignupRequest request) {
        // username 중복 확인
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        // userRole 확인
        UserRole role = UserRole.USER;
        if (request.isAdmin()) {
            if (!request.getAdminToken().equals(adminToken)) {
                throw new IllegalArgumentException("어드민 토큰이 일치하지 않습니다.");
            }
            role = UserRole.ADMIN;
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        return userRepository.save(User.of(request, encodedPassword, role));
    }

    /**
     * 회원 비활성화
     */
    @Transactional
    public Long disable(DisableRequest request, User user) {
        if (user.getStatus() == UserStatus.DISABLED) {
            throw new IllegalArgumentException("이미 비활성화한 사용자입니다.");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }
        user.disable();
        deleteRefreshToken(user);
        return user.getId();
    }

    /**
     * 로그아웃
     */
    @Transactional
    public Long logout(User user) {
        deleteRefreshToken(user);
        return user.getId();
    }

    /**
     * 전체 회원 조회
     */
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * 회원 조회
     */
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    /**
     * 프로필 수정
     */
    @Transactional
    public User updateProfile(Long userId, UpdateProfileRequest request, User requestUser) {
        requestUser.verifyAccessAuthority(userId);
        User user = getUser(userId);
        user.updateProfile(request);
        return user;
    }

    /**
     * 비밀번호 수정
     */
    @Transactional
    public User updatePassword(Long userId, UpdatePasswordRequest request, User requestUser) {
        requestUser.verifyAccessAuthority(userId);
        User user = getUser(userId);

        String currentPassword = user.getPassword();
        if (!passwordEncoder.matches(request.getCurrentPassword(), currentPassword)) {
            throw new PasswordNotMatchException("현재 비밀번호가 일치하지 않습니다.");
        }

        String newPassword = request.getNewPassword();
        if (passwordEncoder.matches(newPassword, currentPassword)) {
            throw new PasswordNotMatchException("현재 비밀번호와 동일한 비밀번호로 수정할 수 없습니다.");
        }

        String retypedNewPassword = request.getRetypedNewPassword();
        if (!newPassword.equals(retypedNewPassword)) {
            throw new PasswordNotMatchException("새 비밀번호가 일치하지 않습니다.");
        }

        user.updatePassword(passwordEncoder.encode(newPassword));
        return user;
    }

    /**
     * 회원 권한 수정
     */
    @Transactional
    public User updateRole(Long userId, UserRole role) {
        User user = getUser(userId);
        user.updateRole(role);
        return user;
    }

    /**
     * 팔로워가 가장 많은 상위 10명의 회원 프로필 조회
     */
    public List<User> getInfluencers() {
        return userRepository.findTop10ByOrderByFollowersCountDesc();
    }

    /**
     * Refresh 토큰 삭제
     */
    private void deleteRefreshToken(User user) {
        refreshTokenRepository.deleteByUsername(user.getUsername());
    }

}
