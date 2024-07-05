package com.sparta.sns.primary.follow.repository;

import com.sparta.sns.primary.follow.entity.Follow;
import com.sparta.sns.primary.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(User follower, User following);

    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

}