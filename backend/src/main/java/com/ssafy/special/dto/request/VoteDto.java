package com.ssafy.special.dto.request;

import com.ssafy.special.domain.food.Food;
import com.ssafy.special.domain.member.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class VoteDto {
    private Long crewSeq;
    private Long crewRecommendSeq;
    private Long foodSeq;

   @Builder
    public VoteDto(Long crewSeq, Long crewRecommendSeq, Long foodSeq) {
        this.crewSeq = crewSeq;
        this.crewRecommendSeq = crewRecommendSeq;
        this.foodSeq = foodSeq;
    }
}
