package com.sparta.sns.primary.post.entity;

import com.sparta.sns.primary.tag.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    private PostTag(Post post, Tag tag) {
        this.post = post;
        this.tag = tag;
    }

    public static PostTag of(Post post, Tag tag) {
        return new PostTag(post, tag);
    }

    @PreRemove
    public void preRemove() {
        tag.decreaseUsage();
    }

    /**
     * equals() 재정의 - PostTag 객체의 동등성을 Post와 Tag의 조합으로 재정의
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return (o instanceof PostTag pt)
                && Objects.equals(this.post, pt.post)
                && Objects.equals(this.tag, pt.tag);
    }

    /**
     * hashCode() 재정의 - 동등한 객체에 대해 동일한 해시 코드를 반환하도록 구현
     */
    @Override
    public int hashCode() {
        return Objects.hash(post, tag);
    }

}