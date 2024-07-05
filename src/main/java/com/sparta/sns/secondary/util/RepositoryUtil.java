package com.sparta.sns.secondary.util;

public class RepositoryUtil {

    public static long getTotal(Long totalCount) {
        return (totalCount == null) ? 0L : totalCount;
    }

}
