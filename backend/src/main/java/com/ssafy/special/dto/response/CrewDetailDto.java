package com.ssafy.special.dto.response;

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
    private List<CrewRecommendHistoryDto> histories;
    @Builder

    public CrewDetailDto(Long crewSeq, String crewName, String crewImg, String crewStatus, String memberStatus, List<CrewMembersDto> crewMembers, List<CrewRecommendHistoryDto> histories) {
        this.crewSeq = crewSeq;
        this.crewName = crewName;
        this.crewImg = crewImg;
        this.crewStatus = crewStatus;
        this.memberStatus = memberStatus;
        this.crewMembers = crewMembers;
        this.histories = histories;
    }
}
