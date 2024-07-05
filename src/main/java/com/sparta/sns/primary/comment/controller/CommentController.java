package com.sparta.sns.primary.comment.controller;

import com.sparta.sns.secondary.base.dto.CommonResponse;
import com.sparta.sns.primary.comment.dto.CommentRequest;
import com.sparta.sns.primary.comment.dto.CommentResponse;
import com.sparta.sns.primary.comment.entity.Comment;
import com.sparta.sns.primary.comment.service.CommentService;
import com.sparta.sns.secondary.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.sparta.sns.secondary.util.ControllerUtil.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성
     */
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommonResponse<?>> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "댓글 작성 실패");
        }
        verifyPathIdWithBody(postId, request.getPostId());

        Comment comment = commentService.createComment(postId, request, userDetails.getUser());

        return getResponseEntity(CommentResponse.of(comment), "댓글 작성 성공");
    }

    /**
     * 해당 게시물의 전체 댓글 조회
     */
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<CommonResponse<?>> getAllPostComments(
            @PathVariable Long postId,
            @PageableDefault(
                    sort = "createdAt", // Pageable 정렬보다 JpaRepository 메서드 네임 쿼리가 우선권을 가진다.
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        Page<Comment> page = commentService.getAllPostComments(postId, pageable);
        Page<CommentResponse> response = page.map(CommentResponse::of);

        return getResponseEntity(response, "게시물 전체 댓글 조회 성공");
    }

    /**
     * 해당 사용자가 좋아요한 전체 댓글 조회
     */
    @GetMapping("/user/likes/comments")
    public ResponseEntity<CommonResponse<?>> getLikedComments(
            @PageableDefault(
                    size = 5
            ) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Page<Comment> page = commentService.getLikedComments(userDetails.getUser(), pageable);
        Page<CommentResponse> response = page.map(CommentResponse::of);

        return getResponseEntity(response, "사용자가 좋아요한 전체 댓글 조회 성공");
    }

    /**
     * 댓글 조회
     */
    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<?>> getComment(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        Comment comment = commentService.getPostComment(postId, commentId);

        return getResponseEntity(CommentResponse.of(comment), "댓글 조회 성공");
    }

    /**
     * 댓글 수정
     */
    @PatchMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<?>> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "댓글 수정 실패");
        }
        verifyPathIdWithBody(postId, request.getPostId()); // 수정하고자 하는 게시물이 맞는지 확인

        Comment comment = commentService.updateComment(commentId, request, userDetails.getUser());

        return getResponseEntity(CommentResponse.of(comment), "댓글 수정 성공");
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<?>> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long response = commentService.deleteComment(postId, commentId, userDetails.getUser());

        return getResponseEntity(response, "댓글 삭제 성공");
    }

}