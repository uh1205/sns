package com.sparta.sns.primary.post.repository;

import com.sparta.sns.primary.post.entity.Post;
import com.sparta.sns.primary.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Page<Post> findAllByUser(User user, Pageable pageable);

}
