package com.ssafy.special.domain.food;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class FoodId implements Serializable {
    private Long food;
    private Long ingredient;

    @Builder
    public FoodId(Long food, Long ingredient) {
        this.food = food;
        this.ingredient = ingredient;
    }
}
