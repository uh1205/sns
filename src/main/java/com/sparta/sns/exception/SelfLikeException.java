package com.sparta.sns.exception;

public class SelfLikeException extends RuntimeException {

    public SelfLikeException() {
        super("본인이 작성한 컨텐츠에 좋아요를 남길 수 없습니다.");
    }

    public SelfLikeException(String message) {
        super(message);
    }
}
