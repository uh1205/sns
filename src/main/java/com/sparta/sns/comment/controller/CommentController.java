package com.sparta.sns.comment.controller;

import com.sparta.sns.base.dto.CommonResponse;
import com.sparta.sns.comment.dto.CommentRequest;
import com.sparta.sns.comment.dto.CommentResponse;
import com.sparta.sns.comment.entity.Comment;
import com.sparta.sns.comment.service.CommentService;
import com.sparta.sns.security.UserDetailsImpl;
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

import static com.sparta.sns.util.ControllerUtil.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성
     */
    @PostMapping
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
    @GetMapping
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
     * 댓글 조회
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<CommonResponse<?>> getComment(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        Comment comment = commentService.getComment(postId, commentId);

        return getResponseEntity(CommentResponse.of(comment), "댓글 조회 성공");
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/{commentId}")
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
        verifyPathIdWithBody(postId, request.getPostId());

        Comment comment = commentService.updateComment(postId, commentId, request, userDetails.getUser());

        return getResponseEntity(CommentResponse.of(comment), "댓글 수정 성공");
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponse<?>> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long response = commentService.deleteComment(postId, commentId, userDetails.getUser());

        return getResponseEntity(response, "댓글 삭제 성공");
    }

}