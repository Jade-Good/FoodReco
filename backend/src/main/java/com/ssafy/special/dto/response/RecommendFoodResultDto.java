package com.ssafy.special.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendFoodResultDto {
    Long recommendedFoodSeq;
    String recommendedFoodName;
    String cookingMethod;
    String type;
    String category;
    String img;
    Double ingredientSimilarity;
    Double foodNameSimilarity;
    String originalFoodName;

    @Builder

    public RecommendFoodResultDto(Long recommendedFoodSeq, String recommendedFoodName, String cookingMethod, String type, String category, String img, Double ingredientSimilarity, Double foodNameSimilarity, String originalFoodName) {
        this.recommendedFoodSeq = recommendedFoodSeq;
        this.recommendedFoodName = recommendedFoodName;
        this.cookingMethod = cookingMethod;
        this.type = type;
        this.category = category;
        this.img = img;
        this.ingredientSimilarity = ingredientSimilarity;
        this.foodNameSimilarity = foodNameSimilarity;
        this.originalFoodName = originalFoodName;
    }
}
