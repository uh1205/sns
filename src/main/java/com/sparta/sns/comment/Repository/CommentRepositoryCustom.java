package com.sparta.sns.comment.Repository;

import com.sparta.sns.comment.entity.Comment;
import com.sparta.sns.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {

    Page<Comment> findWithUserLike(User user, Pageable pageable);

}
