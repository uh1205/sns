package com.sparta.sns.like.service;

import com.sparta.sns.comment.Repository.CommentRepository;
import com.sparta.sns.comment.entity.Comment;
import com.sparta.sns.exception.CommentNotFoundException;
import com.sparta.sns.exception.PostNotFoundException;
import com.sparta.sns.exception.SelfLikeException;
import com.sparta.sns.like.dto.LikeRequest;
import com.sparta.sns.like.entity.Like;
import com.sparta.sns.like.entity.LikeStatus;
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
     * 좋아요 토글
     */
    @Transactional
    public Like toggleLike(LikeRequest request, User user) {
        Like like = likeRepository.findByContentIdAndContentType(request.getContentId(), request.getContentType())
                .orElseGet(() -> createLike(request, user));

        if (like.getStatus().equals(LikeStatus.CANCELED)) {
            doLike(like, user.getId()); // 취소된 좋아요거나 신규 좋아요인 경우 좋아요 수행
        } else {
            cancelLike(like, user.getId()); // 이미 좋아요 상태인 경우 좋아요 취소
        }
        return like;
    }

    /**
     * 신규 좋아요 생성
     */
    public Like createLike(LikeRequest request, User user) {
        Long writerId = null;
        Long contentId = request.getContentId();
        switch (request.getContentType()) {
            case POST -> {
                Post post = getPost(contentId);
                writerId = post.getUser().getId();
            }
            case COMMENT -> {
                Comment comment = getComment(contentId);
                writerId = comment.getUser().getId();
            }
        }
        if (writerId.equals(user.getId())) {
            throw new SelfLikeException();
        }
        return likeRepository.save(Like.of(request, user));
    }

    /**
     * 좋아요 수행
     */
    private void doLike(Like like, Long userId) {
        Long contentId = like.getContentId();
        switch (like.getContentType()) {
            case POST -> {
                Post post = getPost(contentId);
                post.increaseLikeCount();
            }
            case COMMENT -> {
                Comment comment = getComment(contentId);
                comment.increaseLikeCount();
            }
        }
        like.doLike(userId);
    }

    /**
     * 좋아요 취소
     */
    private void cancelLike(Like like, Long userId) {
        Long contentId = like.getContentId();
        switch (like.getContentType()) {
            case POST -> {
                Post post = getPost(contentId);
                post.decreaseLikeCount();
            }
            case COMMENT -> {
                Comment comment = getComment(contentId);
                comment.decreaseLikeCount();
            }
        }
        like.cancelLike(userId);
    }

    private Post getPost(Long contentId) {
        return postRepository.findById(contentId)
                .orElseThrow(PostNotFoundException::new);
    }

    private Comment getComment(Long contentId) {
        return commentRepository.findById(contentId)
                .orElseThrow(CommentNotFoundException::new);
    }

}
