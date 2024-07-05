package com.sparta.sns.primary.post.controller;

import com.sparta.sns.primary.image.entity.Image;
import com.sparta.sns.primary.image.service.ImageService;
import com.sparta.sns.primary.post.dto.PostRequest;
import com.sparta.sns.primary.post.dto.PostResponse;
import com.sparta.sns.primary.post.dto.PostSearchCond;
import com.sparta.sns.primary.post.entity.Post;
import com.sparta.sns.primary.post.service.PostService;
import com.sparta.sns.secondary.base.dto.CommonResponse;
import com.sparta.sns.secondary.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.sparta.sns.secondary.util.ControllerUtil.getFieldErrorResponseEntity;
import static com.sparta.sns.secondary.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final ImageService imageService;

    /**
     * 게시물 작성
     */
    @PostMapping(
            value = "/posts",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<CommonResponse<?>> createPost(
            @RequestPart @Valid PostRequest request,
            @RequestPart List<MultipartFile> files,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "게시물 작성 실패");
        }
        List<Image> images = imageService.upload(files);
        Post post = postService.createPost(request, images, userDetails.getUser());

        return getResponseEntity(PostResponse.of(post), "게시물 작성 성공");
    }

    /**
     * 전체 게시물 조회
     *
     * @param userId - 해당 회원의 전체 게시물을 가져옵니다.
     */
    @GetMapping("/posts")
    public ResponseEntity<CommonResponse<?>> getAllPosts(
            @RequestParam(defaultValue = "") Long userId,
            @PageableDefault(
                    sort = "createdAt", // Pageable 정렬보다 JpaRepository 메서드 네임 쿼리가 우선권을 가진다.
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        Page<Post> page = postService.getAllPosts(userId, pageable);
        Page<PostResponse> response = page.map(PostResponse::of);

        return getResponseEntity(response, "전체 게시물 조회 성공");
    }

    /**
     * 게시물 조회
     */
    @GetMapping("/posts/{postId}")
    public ResponseEntity<CommonResponse<?>> getPost(
            @PathVariable Long postId
    ) {
        Post post = postService.getPost(postId);

        return getResponseEntity(PostResponse.of(post), "게시물 조회 성공");
    }

    /**
     * 게시물 수정
     */
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<CommonResponse<?>> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "게시물 수정 실패");
        }
        Post post = postService.updatePost(postId, request, userDetails.getUser());

        return getResponseEntity(PostResponse.of(post), "게시물 수정 성공");
    }

    /**
     * 게시물 삭제
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<CommonResponse<?>> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long response = postService.deletePost(postId, userDetails.getUser());

        return getResponseEntity(response, "게시물 삭제 성공");
    }

    /**
     * 사용자가 좋아요한 전체 게시물 조회
     */
    @GetMapping("/user/likes/posts")
    public ResponseEntity<CommonResponse<?>> getLikedPosts(
            @PageableDefault(
                    size = 5
            ) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Page<Post> page = postService.getLikedPosts(userDetails.getUser(), pageable);
        Page<PostResponse> response = page.map(PostResponse::of);

        return getResponseEntity(response, "사용자가 좋아요한 전체 게시물 조회 성공");
    }

    /**
     * 사용자가 팔로잉 중인 회원들의 전체 게시물 조회
     */
    @GetMapping("/user/follows/posts")
    public ResponseEntity<CommonResponse<?>> getFollowingPosts(
            @RequestBody PostSearchCond cond,
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Page<Post> page = postService.getFollowingPosts(userDetails.getUser(), cond, pageable);
        Page<PostResponse> response = page.map(PostResponse::of);

        return getResponseEntity(response, "사용자가 팔로잉 중인 회원들의 전체 게시물 조회 성공");
    }

}
