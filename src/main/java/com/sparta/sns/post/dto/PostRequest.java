package com.sparta.sns.post.dto;

import com.sparta.sns.post.entity.Visibility;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PostRequest {

    private String content;

    @NotNull
    private Visibility visibility;

    private List<String> tagNames;

}
