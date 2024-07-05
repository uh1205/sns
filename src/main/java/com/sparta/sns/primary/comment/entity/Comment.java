package com.sparta.sns.primary.comment.entity;

import com.sparta.sns.primary.comment.dto.CommentRequest;
import com.sparta.sns.primary.post.entity.Post;
import com.sparta.sns.primary.user.entity.User;
import com.sparta.sns.secondary.exception.DifferentPostException;
import com.sparta.sns.secondary.exception.DifferentUserException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    private int likeCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 생성자
     */
    private Comment(CommentRequest request, Post post, User user) {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.content = request.getContent();
        this.likeCount = 0;
        this.user = user;
        this.post = post;
        post.getComments().add(this);
    }

    public static Comment of(CommentRequest request, Post post, User user) {
        return new Comment(request, post, user);
    }

    public void verifyPost(Long postId) {
        if (!postId.equals(this.post.getId())) {
            throw new DifferentPostException();
        }
    }

    public void verifyUser(Long userId) {
        if (!userId.equals(this.user.getId())) {
            throw new DifferentUserException();
        }
    }

    public void update(CommentRequest request) {
        this.content = request.getContent();
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

}