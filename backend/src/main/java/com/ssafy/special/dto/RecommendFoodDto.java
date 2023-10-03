package com.ssafy.special.dto;

import com.ssafy.special.domain.food.Food;
import lombok.*;

import javax.management.ConstructorParameters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
