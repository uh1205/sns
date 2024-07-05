package com.sparta.sns.primary.follow.entity;

import com.sparta.sns.primary.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private User following;

    private LocalDateTime followedAt;

    private Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
        this.followedAt = LocalDateTime.now();
    }

    public static Follow of(User follower, User following) {
        return new Follow(follower, following);
    }

}
