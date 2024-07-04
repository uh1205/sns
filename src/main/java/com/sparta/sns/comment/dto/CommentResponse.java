package com.sparta.sns.comment.dto;

import com.sparta.sns.comment.entity.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {

    private final Long id;
    private final String content;
    private final int likeCount;
    private final Long postId;
    private final Long userId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.userId = comment.getUser().getId();
        this.postId = comment.getPost().getId();
        this.likeCount = comment.getLikeCount();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }

    public static CommentResponse of(Comment comment) {
        return new CommentResponse(comment);
    }

}
