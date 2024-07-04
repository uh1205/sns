package com.sparta.sns.like.repository;

import com.sparta.sns.like.entity.ContentType;
import com.sparta.sns.like.entity.Like;
import com.sparta.sns.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByContentTypeAndContentIdAndUser(ContentType contentType, Long contentId, User user);

    Optional<Like> findByContentTypeAndContentIdAndUser(ContentType contentType, Long contentId, User user);

}
