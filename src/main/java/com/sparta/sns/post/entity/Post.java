package com.sparta.sns.post.entity;

import com.sparta.sns.comment.entity.Comment;
import com.sparta.sns.exception.DifferentUserException;
import com.sparta.sns.image.Image;
import com.sparta.sns.post.dto.PostRequest;
import com.sparta.sns.tag.Tag;
import com.sparta.sns.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    private int likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostTag> postTags = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    /**
     * 생성자
     */
    private Post(PostRequest request, List<MultipartFile> images, Set<Tag> tags, User user) {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.content = request.getContent();
        this.visibility = request.getVisibility();
        this.likeCount = 0;
        this.user = user;
        setPostTags(tags);
    }

    public static Post of(PostRequest request, List<MultipartFile> images, Set<Tag> tags, User user) {
        return new Post(request, images, tags, user);
    }

    public void verifyUser(User user) {
        if (!user.getId().equals(this.user.getId())) {
            throw new DifferentUserException();
        }
    }

    public void update(PostRequest request, Set<Tag> tags) {
        this.content = request.getContent();
        this.visibility = request.getVisibility();
        this.updatedAt = LocalDateTime.now();
        setPostTags(tags);
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

}
