package com.ssafy.special.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrewJoinDto {
    private Long crewSeq;
    private int crewJoinType;

    @Builder
    public CrewJoinDto(Long creswSeq, int crewJoinType) {
        this.crewSeq = crewSeq;
        this.crewJoinType = crewJoinType;
    }
}
