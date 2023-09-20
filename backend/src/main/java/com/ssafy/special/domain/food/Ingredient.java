package com.ssafy.special.domain.food;


import com.ssafy.special.domain.etc.Allergy;
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

    // allergy_seq
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="allergy_seq")
    private Allergy allergy;

    // ingredient_name
    @NotNull
    @Column(name = "ingredient_name", length = 16)
    private String ingredientName;

    //is_allergy
    @NotNull
    @Column(name = "is_allergy", columnDefinition = "tinyint")
    private int isAllergy;

    @Builder
    public Ingredient(Long ingredientSeq, Allergy allergy, String ingredientName, int isAllergy) {
        this.ingredientSeq = ingredientSeq;
        this.allergy = allergy;
        this.ingredientName = ingredientName;
        this.isAllergy = isAllergy;
    }
}
