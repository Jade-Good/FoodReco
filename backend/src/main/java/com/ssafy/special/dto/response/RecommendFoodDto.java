package com.ssafy.special.dto.response;

import com.ssafy.special.domain.food.Food;
import com.ssafy.special.domain.member.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RecommendFoodDto {
    private Member member;
    private List<Food> recommendFoods;

    @Builder
    public RecommendFoodDto(Member member, List<Food> recommendFoods) {
        this.member = member;
        this.recommendFoods = recommendFoods;
    }
}
