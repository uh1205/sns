package com.sparta.sns.primary.comment.Repository;

import com.sparta.sns.primary.comment.entity.Comment;
import com.sparta.sns.primary.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {

    Page<Comment> findWithUserLike(User user, Pageable pageable);

}
