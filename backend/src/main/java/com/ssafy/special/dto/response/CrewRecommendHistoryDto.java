package com.ssafy.special.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CrewRecommendHistoryDto {
    private Long crewRecommendSeq;
    private List<CrewRecommendHistoryByFoodDto> foodList;

    @Builder
    public CrewRecommendHistoryDto(Long crewRecommendSeq, List<CrewRecommendHistoryByFoodDto> foodList) {
        this.crewRecommendSeq = crewRecommendSeq;
        this.foodList = foodList;
    }
}
