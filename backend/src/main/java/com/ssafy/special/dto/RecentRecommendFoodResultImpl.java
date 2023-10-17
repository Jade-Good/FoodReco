package com.ssafy.special.dto;

public class RecentRecommendFoodResultImpl implements RecentRecommendFoodResult {
    private Long foodSeq;
    private String name;

    public RecentRecommendFoodResultImpl(Long foodSeq, String name) {
        this.foodSeq = foodSeq;
        this.name = name;
    }

    @Override
    public Long getFoodSeq() {
        return this.foodSeq;
    }

    @Override
    public String getName() {
        return this.name;
    }

}