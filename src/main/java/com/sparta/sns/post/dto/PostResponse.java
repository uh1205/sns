package com.sparta.sns.post.dto;

import com.sparta.sns.image.entity.Image;
import com.sparta.sns.post.entity.Post;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostResponse {

    private Long id;
    private String content;
    private int likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private List<String> tags;
    private List<String> imagePaths;

    private PostResponse(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.likeCount = post.getLikeCount();
        this.userId = post.getUser().getId();
        this.tags = post.getPostTags().stream()
                .map(pt -> pt.getTag().getName())
                .toList();
        this.imagePaths = post.getImages().stream()
                .map(Image::getFilePath)
                .toList();
    }

    public static PostResponse of(Post post) {
        return new PostResponse(post);
    }

}