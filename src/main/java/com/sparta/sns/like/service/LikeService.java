package com.sparta.sns.like.service;

import com.sparta.sns.comment.Repository.CommentRepository;
import com.sparta.sns.comment.entity.Comment;
import com.sparta.sns.exception.*;
import com.sparta.sns.like.dto.LikeRequest;
import com.sparta.sns.like.entity.ContentType;
import com.sparta.sns.like.entity.Like;
import com.sparta.sns.like.repository.LikeRepository;
import com.sparta.sns.post.entity.Post;
import com.sparta.sns.post.repository.PostRepository;
import com.sparta.sns.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    /**
     * 좋아요
     */
    @Transactional
    public Like doLike(LikeRequest request, User user) {
        Long contentId = request.getContentId();
        ContentType contentType = request.getContentType();

        if (likeRepository.existsByContentTypeAndContentIdAndUser(contentType, contentId, user)) {
            throw new AlreadyLikeException("이미 좋아요 상태입니다.");
        }
        Like like = null;
        switch (contentType) {
            case POST -> like = doLikePost(contentId, user);
            case COMMENT -> like = doLikeComment(contentId, user);
        }
        return like;
    }

    private Like doLikePost(Long postId, User user) {
        Post post = getPost(postId);
        checkSelfLike(post.getUser().getId(), user.getId());

        post.increaseLikeCount();
        user.getLikedPosts().add(post);

        return Like.of(postId, ContentType.POST, user);
    }

    private Like doLikeComment(Long commentId, User user) {
        Comment comment = getComment(commentId);
        checkSelfLike(comment.getUser().getId(), user.getId());

        comment.increaseLikeCount();
        user.getLikedComments().add(comment);

        return Like.of(commentId, ContentType.COMMENT, user);
    }

    /**
     * 좋아요 취소
     */
    @Transactional
    public Long cancelLike(LikeRequest request, User user) {
        Long contentId = request.getContentId();
        ContentType contentType = request.getContentType();

        Like like = getLike(contentType, contentId, user);
        likeRepository.delete(like);

        switch (contentType) {
            case POST -> cancelPostLike(contentId, user);
            case COMMENT -> cancelCommentLike(contentId, user);
        }
        return like.getId();
    }

    private void cancelPostLike(Long postId, User user) {
        Post post = getPost(postId);
        post.decreaseLikeCount();
        user.getLikedPosts().remove(post);
    }

    private void cancelCommentLike(Long commentId, User user) {
        Comment comment = getComment(commentId);
        comment.decreaseLikeCount();
        user.getLikedComments().remove(comment);
    }

    /**
     * 본인이 작성한 컨텐츠인지 확인
     */
    private void checkSelfLike(Long writerId, Long userId) {
        if (writerId.equals(userId)) {
            throw new SelfLikeException();
        }
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    }

    private Like getLike(ContentType contentType, Long contentId, User user) {
        return likeRepository.findByContentTypeAndContentIdAndUser(contentType, contentId, user)
                .orElseThrow(() -> new LikeNotFoundException(contentType, contentId));
    }

}
