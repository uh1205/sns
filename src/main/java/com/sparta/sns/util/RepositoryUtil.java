package com.sparta.sns.util;

public class RepositoryUtil {

    public static long getTotal(Long totalCount) {
        return (totalCount == null) ? 0L : totalCount;
    }

}
