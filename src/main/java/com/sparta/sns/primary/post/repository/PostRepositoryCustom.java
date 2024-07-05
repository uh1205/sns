package com.sparta.sns.primary.post.repository;

import com.sparta.sns.primary.post.dto.PostSearchCond;
import com.sparta.sns.primary.post.entity.Post;
import com.sparta.sns.primary.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<Post> findWithUserLike(User user, Pageable pageable);

    Page<Post> findWithUserFollow(User follower, PostSearchCond cond, Pageable pageable);
}
