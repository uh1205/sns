package com.sparta.sns.like.dto;

import com.sparta.sns.like.entity.ContentType;
import com.sparta.sns.like.entity.Like;
import com.sparta.sns.like.entity.LikeStatus;
import lombok.Data;

@Data
public class LikeResponse {

    private final Long id;
    private final Long contentId;
    private final ContentType contentType;
    private final LikeStatus status; // 좋아요 상태 [LIKED, CANCELED]
    private final Long userId;

    private LikeResponse(Like like) {
        this.id = like.getId();
        this.contentType = like.getContentType();
        this.contentId = like.getContentId();
        this.status = like.getStatus();
        this.userId = like.getUser().getId();
    }

    public static LikeResponse of(Like like) {
        return new LikeResponse(like);
    }

}
