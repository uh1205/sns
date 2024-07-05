package com.sparta.sns.primary.post.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostRequest {

    private String content;

    private List<String> tagNames;

}
