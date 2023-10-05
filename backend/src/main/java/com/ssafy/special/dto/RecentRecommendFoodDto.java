package com.ssafy.special.dto;

import com.ssafy.special.dto.request.UserTasteDto;
import lombok.*;

import java.util.Objects;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
//UserTasteDto가 이 클래스 상속받기
public class RecentRecommendFoodDto{
    Long foodSeq;
    String name;
    public RecentRecommendFoodDto(UserTasteDto userTasteDto){
        this.foodSeq = userTasteDto.getFoodSeq();
        this.name = userTasteDto.getFoodName();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecentRecommendFoodDto that = (RecentRecommendFoodDto) o;
        return Objects.equals(foodSeq, that.foodSeq) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodSeq, name);
    }
}
