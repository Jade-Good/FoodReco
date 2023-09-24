package com.ssafy.special.domain.crew;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class VoteId implements Serializable {
    private Long crewRecommendSeq;
    private Long member;
    private Long food;

    @Builder
    public VoteId(Long crewRecommendSeq, Long member, Long food) {
        this.crewRecommendSeq = crewRecommendSeq;
        this.member = member;
        this.food = food;
    }
}
