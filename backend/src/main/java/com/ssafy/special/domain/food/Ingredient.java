package com.ssafy.special.domain.food;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity(name="ingredient")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ingredient {

    // ingredient_seq, PK값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_seq")
    private Long ingredientSeq;

    // ingredient_name
    @NotNull
    @Column(name = "ingredient_name", length = 16)
    private String name;

    //is_allergy
    @NotNull
    @Column(name = "is_allergy", columnDefinition = "tinyint default 0")
    private int isAllergy;

    @Builder
    public Ingredient(Long ingredientSeq, String name, int isAllergy) {
        this.ingredientSeq = ingredientSeq;
        this.name = name;
        this.isAllergy = isAllergy;
    }
}
