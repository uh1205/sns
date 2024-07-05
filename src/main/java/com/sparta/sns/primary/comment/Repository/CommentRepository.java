package com.sparta.sns.primary.comment.Repository;

import com.sparta.sns.primary.comment.entity.Comment;
import com.sparta.sns.primary.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    Page<Comment> findAllByPost(Post post, Pageable pageable);

}