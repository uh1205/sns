package com.sparta.sns.like.entity;

import com.sparta.sns.base.entity.Timestamped;
import com.sparta.sns.exception.DifferentUserException;
import com.sparta.sns.like.dto.LikeRequest;
import com.sparta.sns.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    private Long contentId;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Enumerated(EnumType.STRING)
    private LikeStatus status; // 좋아요 상태 [LIKED, CANCELED]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 생성자
     */
    private Like(LikeRequest request, User user) {
        this.contentId = request.getContentId();
        this.contentType = request.getContentType();
        this.status = LikeStatus.CANCELED; // 좋아요 로직 수행 전에는 취소 상태
        this.user = user;
    }

    public static Like of(LikeRequest request, User user) {
        return new Like(request, user);
    }

    public void doLike(Long userId) {
        this.verifyUser(userId);
        this.status = LikeStatus.LIKED;
    }

    public void cancelLike(Long userId) {
        this.verifyUser(userId);
        this.status = LikeStatus.CANCELED;
    }

    public void verifyUser(Long userId) {
        if (!userId.equals(this.user.getId())) {
            throw new DifferentUserException("해당 좋아요의 주인이 아닙니다.");
        }
    }

}
