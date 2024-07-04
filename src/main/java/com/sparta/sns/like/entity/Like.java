package com.sparta.sns.like.entity;

import com.sparta.sns.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private Long contentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime likedAt;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    /**
     * 생성자
     */
    private Like(Long contentId, ContentType contentType, User user) {
        this.contentId = contentId;
        this.contentType = contentType;
        this.user = user;
        this.likedAt = LocalDateTime.now();
    }

    public static Like of(Long contentId, ContentType contentType, User user) {
        return new Like(contentId, contentType, user);
    }

}
