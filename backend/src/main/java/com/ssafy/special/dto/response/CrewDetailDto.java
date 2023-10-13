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
    private Long memberSeq;
    private String memberStatus;
    private String memberCheckVote;
    private List<CrewMembersDto> crewMembers;
    private List<CrewRecommendHistoryDto> histories;
    private VoteRecommendDto voteRecommend;
    @Builder
    public CrewDetailDto(Long crewSeq, String crewName, String crewImg, String crewStatus, Long memberSeq, String memberStatus, String memberCheckVote, List<CrewMembersDto> crewMembers, List<CrewRecommendHistoryDto> histories, VoteRecommendDto voteRecommend) {
        this.crewSeq = crewSeq;
        this.crewName = crewName;
        this.crewImg = crewImg;
        this.crewStatus = crewStatus;
        this.memberSeq = memberSeq;
        this.memberStatus = memberStatus;
        this.memberCheckVote = memberCheckVote;
        this.crewMembers = crewMembers;
        this.histories = histories;
        this.voteRecommend = voteRecommend;
    }
}
