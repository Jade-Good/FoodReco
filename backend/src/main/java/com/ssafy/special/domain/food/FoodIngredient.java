package com.ssafy.special.domain.food;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ssafy.special.domain.crew.Crew;
import com.ssafy.special.domain.crew.CrewMember;
import com.ssafy.special.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
@Getter
@Entity(name = "food_ingredient")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(FoodId.class)
public class FoodIngredient {

    // food_seq, PK값
    @Id
    @ManyToOne
    @JoinColumn(name= "food_seq")
    @JsonBackReference
    private Food food;

    // 그룹 seq
    @Id
    @ManyToOne
    @JoinColumn(name= "ingredient_seq")
    @JsonBackReference
    private Ingredient ingredient;


    @Builder
    public FoodIngredient(Food food, Ingredient ingredient) {
        this.food = food;
        this.ingredient = ingredient;
    }
}
