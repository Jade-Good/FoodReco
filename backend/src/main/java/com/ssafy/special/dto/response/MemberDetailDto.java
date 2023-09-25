package com.ssafy.special.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class MemberDetailDto {

    private String profileUrl;
    private String nickName;
    private int height;
    private int weight;
    private int activity;

    @Builder
    public MemberDetailDto(String profileUrl, String nickName, int height, int weight, int activity) {
        this.profileUrl = profileUrl;
        this.nickName = nickName;
        this.height = height;
        this.weight = weight;
        this.activity = activity;
    }
}
