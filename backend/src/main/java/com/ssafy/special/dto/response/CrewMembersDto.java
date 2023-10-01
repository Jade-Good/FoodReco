package com.ssafy.special.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrewMembersDto {
    private Long memberSeq;
    private String memberName;
    private String memberImg;
    private String memberStatus;

    @Builder
    public CrewMembersDto(Long memberSeq, String memberName, String memberImg, String memberStatus) {
        this.memberSeq = memberSeq;
        this.memberName = memberName;
        this.memberImg = memberImg;
        this.memberStatus = memberStatus;
    }
}
