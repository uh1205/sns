package com.sparta.sns.comment.entity;

import com.sparta.sns.comment.dto.CommentRequest;
import com.sparta.sns.exception.DifferentPostException;
import com.sparta.sns.exception.DifferentUserException;
import com.sparta.sns.like.entity.Like;
import com.sparta.sns.post.entity.Post;
import com.sparta.sns.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

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