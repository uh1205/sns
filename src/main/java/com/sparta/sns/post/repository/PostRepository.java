package com.sparta.sns.post.repository;

import com.sparta.sns.post.entity.Post;
import com.sparta.sns.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByUser(User user, Pageable pageable);

}
