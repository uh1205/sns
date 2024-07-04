package com.sparta.sns.image.entity;

import com.sparta.sns.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    private String fileName; // 파일 원본 이름

    private String filePath; // 파일 저장 경로

    // 이 둘을 따로 지정한 이유는 동일한 이름을 가진 파일이 업로드될 경우 오류가 발생하기 때문이다.

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private Image(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public static Image of(String fileName, String filePath) {
        return new Image(fileName, filePath);
    }

}
