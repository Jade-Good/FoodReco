package com.ssafy.special.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class RecentFoodDto {
    private Long foodSeq;
    private String foodName;
    private String foodImg;
    @Builder
    public RecentFoodDto(Long foodSeq, String foodName, String foodImg) {
        this.foodSeq = foodSeq;
        this.foodName = foodName;
        this.foodImg = foodImg;
    }
}
