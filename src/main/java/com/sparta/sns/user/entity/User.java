package com.sparta.sns.user.entity;

import com.sparta.sns.base.entity.Timestamped;
import com.sparta.sns.user.dto.request.UpdateProfileRequest;
import com.sparta.sns.user.dto.request.SignupRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 사용자 이름

    @Column(nullable = false)
    private String password; // 비밀번호

    private String name; // 이름

    private String bio; // 소개

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role; // 사용자 권한 [USER, ADMIN]

    @ElementCollection
    @CollectionTable(name = "passwords", joinColumns = @JoinColumn(name = "user_id"))
    private final List<String> passwordHistory = new ArrayList<>(); // 최근 변경한 비밀번호 히스토리

    @Value("${password.history.limit}")
    private static int PASSWORD_HISTORY_LIMIT;

    /**
     * 생성자
     */
    public User(SignupRequest request, String encodedPassword, UserRole role) {
        this.username = request.getUsername();
        this.password = encodedPassword;
        this.name = request.getName();
        this.role = role;
    }

    /**
     * 회원 프로필 수정
     */
    public void updateProfile(UpdateProfileRequest request) {
        this.name = request.getName();
        this.bio = request.getBio();
    }

    /**
     * 회원 비밀번호 수정
     */
    public void updatePassword(String newPassword) {
        if (isPasswordInHistory(newPassword)) {
            throw new IllegalArgumentException("최근에 사용한 비밀번호로는 변경할 수 없습니다.");
        }
        this.password = newPassword;
        addPasswordToHistory(newPassword);
    }

    /**
     * 최근에 변경한 비밀번호인지 확인
     */
    public boolean isPasswordInHistory(String password) {
        return passwordHistory.contains(password);
    }

    /**
     * 새로운 비밀번호 히스토리에 저장
     */
    public void addPasswordToHistory(String newPassword) {
        if (passwordHistory.size() == PASSWORD_HISTORY_LIMIT) { // 히스토리가 가득 찬 경우
            passwordHistory.remove(0); // 가장 오래된 비밀번호 제거
        }
        passwordHistory.add(newPassword);
    }

    /**
     * 회원 권한 수정
     */
    public void updateRole(UserRole role) {
        this.role = role;
    }

    /**
     * 해당 userId 에 대한 액세스 권한 검증
     */
    public void verifyAccessAuthority(Long userId) {
        if (!this.id.equals(userId) && !this.role.equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("액세스 권한이 없는 회원입니다.");
        }
    }

}