package com.ssafy.special.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendFoodDto {
    Long foodSeq;
    Double similarity;
    String name;
    Double nameSimilarity;
    String originName;

    @Builder
    public RecommendFoodDto(Long foodSeq, Double similarity, String name, Double nameSimilarity, String originName){
        this.foodSeq = foodSeq;
        this.similarity = similarity;
        this.name = name;
        this.nameSimilarity = nameSimilarity;
        this.originName = originName;

    }
}