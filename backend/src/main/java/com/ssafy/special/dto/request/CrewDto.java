package com.ssafy.special.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrewDto {
    private Long crewSeq;
    private String name;
    private String img;
    // 현재 그룹에 들어간건지 안들어간 건지 상태를 나타내는 필드
    private String status;
    private Long recentRecommend;
    private int crewCnt;

    @Builder
    public CrewDto(Long crewSeq, String name, String img, String status, Long recentRecommend, int crewCnt) {
        this.crewSeq = crewSeq;
        this.name = name;
        this.img = img;
        this.status = status;
        this.recentRecommend = recentRecommend;
        this.crewCnt = crewCnt;
    }
}
