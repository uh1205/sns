package com.sparta.sns.like.repository;

import com.sparta.sns.like.entity.ContentType;
import com.sparta.sns.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByContentIdAndContentType(Long contentId, ContentType contentType);

}
