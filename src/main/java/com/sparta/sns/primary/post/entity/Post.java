package com.sparta.sns.primary.post.entity;

import com.sparta.sns.primary.comment.entity.Comment;
import com.sparta.sns.primary.image.entity.Image;
import com.sparta.sns.primary.post.dto.PostRequest;
import com.sparta.sns.primary.tag.Tag;
import com.sparta.sns.primary.user.entity.User;
import com.sparta.sns.secondary.exception.DifferentUserException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String content;

    private int likeCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostTag> postTags = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();
    // 이미지는 서버, DB, 파일 시스템에 저장할 수 있으며, 각각의 장단점이 있으나 보통 외부 서버에 저장하는 것이 권장된다.
    // 보통 클라우드 스토리지(예: AWS S3)를 많이 이용한다.

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    /**
     * 생성자
     */
    private Post(PostRequest request, List<Image> images, Set<Tag> tags, User user) {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.content = request.getContent();
        this.likeCount = 0;
        this.user = user;
        setImages(images);
        setPostTags(tags);
    }

    public static Post of(PostRequest request, List<Image> images, Set<Tag> tags, User user) {
        return new Post(request, images, tags, user);
    }

    public void verifyUser(Long userId) {
        if (!userId.equals(this.user.getId())) {
            throw new DifferentUserException();
        }
    }

    public void update(PostRequest request, Set<Tag> tags) {
        this.content = request.getContent();
        this.updatedAt = LocalDateTime.now();
        setPostTags(tags);
    }

    private void setImages(List<Image> images) {
        for (Image image : images) {
            this.images.add(image);
            image.setPost(this);
        }
    }

    private void setPostTags(Set<Tag> newTags) {
        Set<PostTag> currentTags = new HashSet<>(this.postTags); // 데이터 무결성 유지를 위한 복사본

        // 새로운 태그 세트에 포함되지 않는 현재 태그를 제거
        currentTags.stream()
                .filter(postTag -> !newTags.contains(postTag.getTag()))
                .forEach(this::removePostTag);

        // 현재 태그 세트에 포함되지 않는 새로운 태그를 추가
        newTags.stream()
                .filter(tag -> currentTags.stream().noneMatch(postTag -> postTag.getTag().equals(tag)))
                .forEach(this::addPostTag);
    }

    private void removePostTag(PostTag postTag) {
        this.postTags.remove(postTag);
        postTag.getTag().decreaseUsage();
    }

    private void addPostTag(Tag tag) {
        PostTag postTag = PostTag.of(this, tag);
        this.postTags.add(postTag);
        tag.increaseUsage();
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

}
