package com.sparta.sns.primary.user.dto.request;

import lombok.Data;

@Data
public class ProfileRequest {

    private String name; // 이름 (nullable)

    private String bio; // 소개 (nullable)

}
