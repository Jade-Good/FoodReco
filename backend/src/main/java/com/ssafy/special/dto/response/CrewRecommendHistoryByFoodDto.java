package com.ssafy.special.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrewRecommendHistoryByFoodDto {
    private Long foodSeq;
    private String foodName;
    private String foodImg;
    private  int foodVoteCount;
    private boolean isVote;

    @Builder
    public CrewRecommendHistoryByFoodDto(Long foodSeq, String foodName, String foodImg, int foodVoteCount,boolean isVote) {
        this.foodSeq = foodSeq;
        this.foodName = foodName;
        this.foodImg = foodImg;
        this.foodVoteCount = foodVoteCount;
        this.isVote= isVote;
    }
}
