package com.sparta.sns.user.entity;

import com.sparta.sns.base.entity.Timestamped;
import com.sparta.sns.comment.entity.Comment;
import com.sparta.sns.post.entity.Post;
import com.sparta.sns.user.dto.request.SignupRequest;
import com.sparta.sns.user.dto.request.UpdateProfileRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends Timestamped {

    private static final int PASSWORD_HISTORY_LIMIT = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String username; // 사용자 아이디

    private String password;

    private String name;

    private String bio; // 소개

    @Enumerated(value = EnumType.STRING)
    private UserRole role; // 사용자 권한 [USER, ADMIN]

    @Enumerated(EnumType.STRING)
    private UserStatus status; // 사용자 상태 [JOINED, WITHDRAWN]

    private LocalDateTime statusUpdatedAt;

    private int followersCount; // 팔로워 수

    private int followingCount; // 팔로잉 수

    private int likedPostsCount; // 좋아요한 게시물 수

    private int likedCommentsCount; // 좋아요한 댓글 수

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> likedPosts = new ArrayList<>(); // 좋아요한 게시물

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> likedComments = new ArrayList<>(); // 좋아요한 댓글

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(joinColumns = @JoinColumn(name = "user_id"))
    private LinkedList<String> passwordHistory = new LinkedList<>(); // 최근 변경한 비밀번호

    /**
     * 생성자
     */
    private User(SignupRequest request, String encodedPassword, UserRole role) {
        this.username = request.getUsername();
        this.password = encodedPassword;
        this.name = request.getName();
        this.role = role;
        this.status = UserStatus.ENABLED;
        this.statusUpdatedAt = LocalDateTime.now();
        this.likedPostsCount = 0;
        this.likedCommentsCount = 0;
        this.followersCount = 0;
        this.followingCount = 0;
        addPasswordToHistory(encodedPassword);
    }

    public static User of(SignupRequest request, String encodedPassword, UserRole role) {
        return new User(request, encodedPassword, role);
    }

    /**
     * 회원 비활성화
     */
    public void disable() {
        this.status = UserStatus.DISABLED;
        this.statusUpdatedAt = LocalDateTime.now();
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
    private void addPasswordToHistory(String newPassword) {
        if (passwordHistory.size() == PASSWORD_HISTORY_LIMIT) { // 히스토리가 가득 찬 경우
            passwordHistory.removeFirst(); // 가장 오래된 비밀번호 제거
        }
        passwordHistory.addLast(newPassword);
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