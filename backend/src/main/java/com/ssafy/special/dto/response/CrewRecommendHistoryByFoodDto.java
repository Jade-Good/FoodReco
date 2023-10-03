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

    @Builder
    public CrewRecommendHistoryByFoodDto(Long foodSeq, String foodName, String foodImg, int foodVoteCount) {
        this.foodSeq = foodSeq;
        this.foodName = foodName;
        this.foodImg = foodImg;
        this.foodVoteCount = foodVoteCount;
    }
}
