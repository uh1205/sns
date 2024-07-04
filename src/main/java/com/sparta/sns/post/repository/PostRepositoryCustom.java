package com.sparta.sns.post.repository;

import com.sparta.sns.post.dto.PostSearchCond;
import com.sparta.sns.post.entity.Post;
import com.sparta.sns.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<Post> findWithUserLike(User user, Pageable pageable);

    Page<Post> findWithUserFollow(User follower, PostSearchCond cond, Pageable pageable);
}
