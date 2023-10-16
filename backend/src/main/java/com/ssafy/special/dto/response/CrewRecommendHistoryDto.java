package com.ssafy.special.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class CrewRecommendHistoryDto {
    private Long crewRecommendSeq;
    private List<CrewRecommendHistoryByFoodDto> foodList;
    private LocalDateTime crewRecommendTime;

    @Builder

    public CrewRecommendHistoryDto(Long crewRecommendSeq, List<CrewRecommendHistoryByFoodDto> foodList, LocalDateTime crewRecommendTime) {
        this.crewRecommendSeq = crewRecommendSeq;
        this.foodList = foodList;
        this.crewRecommendTime = crewRecommendTime;
    }
}
