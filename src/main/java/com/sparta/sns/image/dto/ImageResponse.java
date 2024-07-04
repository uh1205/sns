package com.sparta.sns.image.dto;

import com.sparta.sns.image.entity.Image;
import lombok.Data;

@Data
public class ImageResponse {

    private Long id;
    private String fileName;
    private String filePath;
    private Long postId;

    private ImageResponse(Image image) {
        this.id = image.getId();
        this.filePath = image.getFilePath();
        this.fileName = image.getFileName();
        this.postId = image.getPost().getId();
    }

    public ImageResponse of(Image image) {
        return new ImageResponse(image);
    }

}
