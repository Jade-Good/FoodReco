package com.ssafy.special.dto;

import com.ssafy.special.domain.member.Member;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrewDetailDto {
    private Long crewSeq;
    private String crewName;
    private String crewImg;
    private String crewStatus;
    private String memberStatus;
    private List<CrewMembersDto> crewMembers;

    @Builder
    public CrewDetailDto(Long crewSeq, String crewName, String crewImg, String crewStatus, String memberStatus, List<CrewMembersDto> crewMembers) {
        this.crewSeq = crewSeq;
        this.crewName = crewName;
        this.crewImg = crewImg;
        this.crewStatus = crewStatus;
        this.memberStatus = memberStatus;
        this.crewMembers = crewMembers;
    }
}
