package com.ssafy.special.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class MemberInfoDto {
    private Long memberSeq;
    private String memberEmail;
    private String memberImg;
    private String memberNickname;

    @Builder
    public MemberInfoDto(Long memberSeq, String memberEmail, String memberImg, String memberNickname) {
        this.memberSeq = memberSeq;
        this.memberEmail = memberEmail;
        this.memberImg = memberImg;
        this.memberNickname = memberNickname;
    }
}
