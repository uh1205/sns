package com.sparta.sns.like.dto;

import com.sparta.sns.like.entity.ContentType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LikeRequest {

    @NotNull
    private Long contentId;

    @NotNull
    private ContentType contentType;

}
