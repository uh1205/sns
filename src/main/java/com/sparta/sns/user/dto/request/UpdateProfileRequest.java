package com.sparta.sns.user.dto.request;

import lombok.Data;

@Data
public class UpdateProfileRequest {

    private String name; // 이름
    private String bio; // 소개

}
