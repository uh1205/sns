package com.sparta.sns.primary.like.service;

import com.sparta.sns.primary.comment.Repository.CommentRepository;
import com.sparta.sns.primary.comment.entity.Comment;
import com.sparta.sns.primary.like.dto.LikeRequest;
import com.sparta.sns.primary.like.entity.ContentType;
import com.sparta.sns.primary.like.entity.Like;
import com.sparta.sns.primary.like.repository.LikeRepository;
import com.sparta.sns.primary.post.entity.Post;
import com.sparta.sns.primary.post.repository.PostRepository;
import com.sparta.sns.primary.user.entity.User;
import com.sparta.sns.secondary.exception.*;
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
    public Like like(LikeRequest request, User user) {
        ContentType contentType = request.getContentType();
        Long contentId = request.getContentId();
        // 이미 좋아요 상태인지 확인
        if (likeRepository.existsByContentTypeAndContentIdAndUser(contentType, contentId, user)) {
            throw new AlreadyLikeException("이미 좋아요 상태입니다.");
        }
        Like like = null;
        switch (contentType) {
            case POST -> like = likePost(contentId, user);
            case COMMENT -> like = likeComment(contentId, user);
        }
        return like;
    }

    private Like likePost(Long postId, User user) {
        Post post = getPost(postId);
        checkSelfLike(post.getUser().getId(), user.getId());
        post.increaseLikeCount();
        user.increaseLikedPostsCount();
        return likeRepository.save(Like.of(postId, ContentType.POST, user));
    }

    private Like likeComment(Long commentId, User user) {
        Comment comment = getComment(commentId);
        checkSelfLike(comment.getUser().getId(), user.getId());
        comment.increaseLikeCount();
        user.increaseLikedCommentsCount();
        return likeRepository.save(Like.of(commentId, ContentType.COMMENT, user));
    }

    /**
     * 좋아요 취소
     */
    @Transactional
    public Long unlike(LikeRequest request, User user) {
        ContentType contentType = request.getContentType();
        Long contentId = request.getContentId();

        Like like = getLike(contentType, contentId, user);
        likeRepository.delete(like);

        switch (contentType) {
            case POST -> unLikePost(contentId, user);
            case COMMENT -> unLikeComment(contentId, user);
        }
        return like.getId();
    }

    private void unLikePost(Long postId, User user) {
        Post post = getPost(postId);
        post.decreaseLikeCount();
        user.decreaseLikedPostsCount();
    }

    private void unLikeComment(Long commentId, User user) {
        Comment comment = getComment(commentId);
        comment.decreaseLikeCount();
        user.decreaseLikedCommentsCount();
    }

    /**
     * 본인이 작성한 컨텐츠인지 확인
     */
    private void checkSelfLike(Long authorId, Long userId) {
        if (authorId.equals(userId)) {
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
