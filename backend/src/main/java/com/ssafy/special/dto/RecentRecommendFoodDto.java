package com.ssafy.special.dto;

import com.ssafy.special.dto.request.UserTasteDto;
import lombok.*;

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

}
