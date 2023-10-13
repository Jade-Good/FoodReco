package com.ssafy.special.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MemberDetailDto {

    private String profileUrl;
    private String nickname;
    private int height;
    private int weight;
    private int activity;

    @Builder
    public MemberDetailDto(String profileUrl, String nickname, int height, int weight, int activity) {
        this.profileUrl = profileUrl;
        this.nickname = nickname;
        this.height = height;
        this.weight = weight;
        this.activity = activity;
    }


}
