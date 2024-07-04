package com.sparta.sns.comment.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sns.comment.entity.Comment;
import com.sparta.sns.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta.sns.comment.entity.QComment.comment;
import static com.sparta.sns.like.entity.QLike.like;
import static com.sparta.sns.util.RepositoryUtil.getTotal;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Comment> findWithUserLike(User user, Pageable pageable) {
        // 데이터 조회 쿼리
        List<Comment> comments = queryFactory
                .selectFrom(comment)
                .join(like).fetchJoin()
                .on(like.contentId.eq(comment.id))
                .where(
                        like.user.id.eq(user.getId())
                )
                .orderBy(like.likedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리
        Long totalCount = queryFactory
                .select(comment.count()) // count(comment.id)
                .from(comment)
                .join(like).fetchJoin()
                .on(like.contentId.eq(comment.id))
                .where(
                        like.user.id.eq(user.getId())
                )
                .fetchOne();

        return new PageImpl<>(comments, pageable, getTotal(totalCount));
    }



}
