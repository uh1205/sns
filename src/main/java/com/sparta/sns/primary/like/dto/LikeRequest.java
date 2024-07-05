package com.sparta.sns.primary.like.dto;

import com.sparta.sns.primary.like.entity.ContentType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LikeRequest {

    @NotNull
    private ContentType contentType;

    @NotNull
    private Long contentId;

}
