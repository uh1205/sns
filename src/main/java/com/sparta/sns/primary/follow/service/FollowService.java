package com.sparta.sns.primary.follow.service;

import com.sparta.sns.primary.follow.dto.FollowRequest;
import com.sparta.sns.primary.follow.entity.Follow;
import com.sparta.sns.primary.follow.repository.FollowRepository;
import com.sparta.sns.primary.user.entity.User;
import com.sparta.sns.primary.user.repository.UserRepository;
import com.sparta.sns.secondary.exception.*;
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
        // 본인을 팔로우 하는지 확인
        Long followingId = request.getFollowingId();
        if (follower.getId().equals(followingId)) {
            throw new SelfFollowException();
        }
        // 이미 팔로우 상태인지 확인
        User following = getUser(followingId);
        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new AlreadyFollowException("이미 팔로우 상태입니다.");
        }
        increaseFollow(follower, following);
        return followRepository.save(Follow.of(follower, following));
    }

    @Transactional
    public Long unfollowUser(FollowRequest request, User follower) {
        User following = getUser(request.getFollowingId());
        Follow follow = getFollow(follower, following);

        decreaseFollow(follower, following);
        followRepository.delete(follow);
        return follow.getId();
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private Follow getFollow(User follower, User following) {
        return followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new FollowNotFoundException(follower.getId(), following.getId()));
    }

    private void increaseFollow(User follower, User following) {
        following.increaseFollowersCount();
        follower.increaseFollowingCount();
    }

    private void decreaseFollow(User follower, User following) {
        following.decreaseFollowersCount();
        follower.decreaseFollowingCount();
    }

}