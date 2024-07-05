package com.sparta.sns.primary.comment.service;

import com.sparta.sns.primary.comment.Repository.CommentRepository;
import com.sparta.sns.primary.comment.Repository.CommentRepositoryCustomImpl;
import com.sparta.sns.primary.comment.dto.CommentRequest;
import com.sparta.sns.primary.comment.entity.Comment;
import com.sparta.sns.secondary.exception.CommentNotFoundException;
import com.sparta.sns.secondary.exception.PostNotFoundException;
import com.sparta.sns.primary.post.entity.Post;
import com.sparta.sns.primary.post.repository.PostRepository;
import com.sparta.sns.primary.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
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
     * 전체 댓글 조회 (admin only)
     */
    public Page<Comment> getAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    /**
     * 해당 게시물의 전체 댓글 조회
     */
    public Page<Comment> getAllPostComments(Long postId, Pageable pageable) {
        return commentRepository.findAllByPostId(postId, pageable);
    }

    /**
     * 해당 사용자가 좋아요한 전체 댓글 조회
     */
    public Page<Comment> getLikedComments(User user, Pageable pageable) {
        return commentRepositoryCustom.findWithUserLike(user, pageable);
    }

    /**
     * 댓글 조회
     */
    public Comment getPostComment(Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        comment.verifyPost(postId); // 해당 게시물의 댓글이 맞는지 확인
        return comment;
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public Comment updateComment(Long commentId, CommentRequest request, User user) {
        Comment comment = getPostComment(request.getPostId(), commentId);
        comment.verifyUser(user.getId()); // 해당 댓글의 작성자가 맞는지 확인
        comment.update(request);
        return comment;
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public Long deleteComment(Long postId, Long commentId, User user) {
        Comment comment = getPostComment(postId, commentId);
        comment.verifyUser(user.getId());
        commentRepository.delete(comment);
        return commentId;
    }

}
