package com.sparta.sns.post.service;

import com.sparta.sns.exception.PostNotFoundException;
import com.sparta.sns.exception.UserNotFoundException;
import com.sparta.sns.image.entity.Image;
import com.sparta.sns.post.dto.PostRequest;
import com.sparta.sns.post.dto.PostSearchCond;
import com.sparta.sns.post.entity.Post;
import com.sparta.sns.post.repository.PostRepository;
import com.sparta.sns.post.repository.PostRepositoryCustomImpl;
import com.sparta.sns.tag.Tag;
import com.sparta.sns.tag.TagRepository;
import com.sparta.sns.user.entity.User;
import com.sparta.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final PostRepositoryCustomImpl postRepositoryCustom;

    /**
     * 게시물 작성
     */
    @Transactional
    public Post createPost(PostRequest request, List<Image> images, User user) {
        Set<Tag> tags = getTagsFromRequest(request.getTagNames());
        Post post = Post.of(request, images, tags, user);
        return postRepository.save(post);
    }

    /**
     * 전체 게시물 조회
     *
     * @param userId - 해당 회원의 전체 게시물을 가져옵니다.
     */
    public Page<Post> getAllPosts(Long userId, Pageable pageable) {
        if (userId == null) {
            return postRepository.findAll(pageable);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return postRepository.findAllByUser(user, pageable);
    }

    /**
     * 팔로잉 중인 회원들의 전체 게시물 조회
     */
    public Page<Post> getFollowingPosts(User user, PostSearchCond cond, Pageable pageable) {
        return postRepositoryCustom.findWithUserFollow(user, cond, pageable);
    }

    /**
     * 해당 사용자가 좋아요한 전체 게시물 조회
     */
    public Page<Post> getLikedPosts(User user, Pageable pageable) {
        return postRepositoryCustom.findWithUserLike(user, pageable);
    }

    /**
     * 게시물 조회
     */
    public Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }

    /**
     * 게시물 수정
     */
    @Transactional
    public Post updatePost(Long postId, PostRequest request, User user) {
        Post post = getPost(postId);
        post.verifyUser(user.getId());
        Set<Tag> tags = getTagsFromRequest(request.getTagNames());
        post.update(request, tags);
        return post;
    }

    /**
     * 게시물 삭제
     */
    @Transactional
    public Long deletePost(Long postId, User user) {
        Post post = getPost(postId);
        post.verifyUser(user.getId());
        postRepository.delete(post);
        return postId;
    }

    /**
     * PostRequest의 tagNames를 기반으로 Tag 엔티티를 조회하거나 생성하여 Set<Tag>으로 반환
     */
    private Set<Tag> getTagsFromRequest(List<String> tagNames) {
        Set<Tag> tags = new HashSet<>();
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(tagName)));
            tags.add(tag);
        }
        return tags;
    }

}
