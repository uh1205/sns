package com.sparta.sns.primary.like.dto;

import com.sparta.sns.primary.like.entity.ContentType;
import com.sparta.sns.primary.like.entity.Like;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LikeResponse {

    private final Long id;
    private final ContentType contentType;
    private final Long contentId;
    private final Long userId;
    private final LocalDateTime likedAt;

    private LikeResponse(Like like) {
        this.id = like.getId();
        this.contentType = like.getContentType();
        this.contentId = like.getContentId();
        this.userId = like.getUser().getId();
        this.likedAt = like.getLikedAt();
    }

    public static LikeResponse of(Like like) {
        return new LikeResponse(like);
    }

}
