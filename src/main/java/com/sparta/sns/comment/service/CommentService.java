package com.sparta.sns.comment.service;

import com.sparta.sns.comment.Repository.CommentRepository;
import com.sparta.sns.comment.Repository.CommentRepositoryCustomImpl;
import com.sparta.sns.comment.dto.CommentRequest;
import com.sparta.sns.comment.entity.Comment;
import com.sparta.sns.exception.CommentNotFoundException;
import com.sparta.sns.exception.PostNotFoundException;
import com.sparta.sns.post.entity.Post;
import com.sparta.sns.post.repository.PostRepository;
import com.sparta.sns.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentRepositoryCustomImpl commentRepositoryCustom;

    /**
     * 댓글 작성
     */
    @Transactional
    public Comment createComment(Long postId, CommentRequest request, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        Comment comment = Comment.of(request, post, user);
        return commentRepository.save(comment);
    }

    /**
     * 해당 게시물의 전체 댓글 조회
     */
    public Page<Comment> getAllPostComments(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        return commentRepository.findAllByPost(post, pageable);
    }

    /**
     * 해당 사용자가 좋아요한 전체 댓글 조회
     */
    public Page<Comment> getLikedComments(User user, Pageable pageable) {
        return commentRepositoryCustom.findWithUserLike(user, pageable);
    }

    /**
     * 특정 댓글 조회
     */
    public Comment getComment(Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        comment.verifyPost(postId);
        return comment;
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public Comment updateComment(Long postId, Long commentId, CommentRequest request, User user) {
        Comment comment = getComment(postId, commentId);
        comment.verifyUser(user.getId());
        comment.update(request);
        return comment;
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public Long deleteComment(Long postId, Long commentId, User user) {
        Comment comment = getComment(postId, commentId);
        comment.verifyUser(user.getId());
        commentRepository.delete(comment);
        return commentId;
    }

}
