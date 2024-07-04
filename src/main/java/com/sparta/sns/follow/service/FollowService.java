package com.sparta.sns.follow.service;

import com.sparta.sns.exception.*;
import com.sparta.sns.follow.dto.FollowRequest;
import com.sparta.sns.follow.entity.Follow;
import com.sparta.sns.follow.repository.FollowRepository;
import com.sparta.sns.user.entity.User;
import com.sparta.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public Follow followUser(FollowRequest request, User follower) {
        Long followerId = follower.getId();
        Long followingId = request.getFollowingId();
        // 본인을 팔로우 하는지 확인
        if (followerId.equals(followingId)) {
            throw new SelfFollowException();
        }
        // 이미 팔로우 상태인지 확인
        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new AlreadyFollowException("이미 팔로우 상태입니다.");
        }
        Follow follow = Follow.of(follower, getUser(followingId));
        return followRepository.save(follow);
    }

    @Transactional
    public Long unfollowUser(FollowRequest request, User follower) {
        Long followerId = follower.getId();
        Long followingId = request.getFollowingId();
        // 본인을 언팔로우 하는지 확인
        if (followerId.equals(followingId)) {
            throw new SelfUnfollowException();
        }
        Follow follow = getFollow(followerId, followingId);
        followRepository.delete(follow);
        return follow.getId();
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private Follow getFollow(Long followerId, Long followingId) {
        return followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new FollowNotFoundException(followerId, followingId));
    }

}