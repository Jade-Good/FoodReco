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
    private Map<Food, List<Member>> voteList;

   @Builder
    public VoteDto(Long crewSeq, Map<Food, List<Member>> voteList) {
        this.crewSeq = crewSeq;
        this.voteList = voteList;
    }
}
