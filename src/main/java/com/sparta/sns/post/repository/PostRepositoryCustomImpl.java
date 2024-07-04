package com.sparta.sns.post.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sns.post.dto.PostSearchCond;
import com.sparta.sns.post.entity.Post;
import com.sparta.sns.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta.sns.follow.entity.QFollow.follow;
import static com.sparta.sns.like.entity.QLike.like;
import static com.sparta.sns.post.entity.QPost.post;
import static com.sparta.sns.util.RepositoryUtil.getTotal;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> findWithUserLike(User user, Pageable pageable) {
        // 데이터 조회 쿼리
        List<Post> posts = queryFactory
                .selectFrom(post)
                .join(like).fetchJoin()
                .on(like.contentId.eq(post.id))
                .where(
                        like.user.id.eq(user.getId())
                )
                .orderBy(like.likedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리
        Long totalCount = queryFactory
                .select(Wildcard.count) // count(*)
                .from(post)
                .join(like).fetchJoin()
                .on(like.contentId.eq(post.id))
                .where(
                        like.user.id.eq(user.getId())
                )
                .fetchOne();

        return new PageImpl<>(posts, pageable, getTotal(totalCount));
    }

    @Override
    public Page<Post> findWithUserFollow(User follower, PostSearchCond cond, Pageable pageable) {
        // 데이터 조회 쿼리
        JPAQuery<Post> query = queryFactory
                .selectFrom(post)
                .join(follow).fetchJoin()
                .on(post.user.id.eq(follow.following.id))
                .where(
                        follow.follower.id.eq(follower.getId()),
                        authorNameEq(cond.getAuthorName())
                );

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(post.getType(), post.getMetadata());
            query.orderBy(new OrderSpecifier(
                    o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Post> posts = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리
        Long totalCount = queryFactory
                .select(Wildcard.count) // count(*)
                .from(post)
                .join(follow).fetchJoin()
                .on(post.user.id.eq(follow.following.id))
                .where(
                        follow.follower.id.eq(follower.getId())
                )
                .fetchOne();

        return new PageImpl<>(posts, pageable, getTotal(totalCount));
    }

    private BooleanExpression authorNameEq(String authorName) {
        return hasText(authorName) ? follow.following.name.eq(authorName) : null;
    }

}