package com.sparta.sns.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {

    @NotNull
    private Long postId;

    @NotBlank
    private String content;

}
