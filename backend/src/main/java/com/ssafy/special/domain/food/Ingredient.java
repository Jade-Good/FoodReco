package com.ssafy.special.domain.food;


import com.ssafy.special.domain.etc.Allergy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity(name="ingredient")
@NoArgsConstructor
public class Ingredient {

    // ingredient_seq, PK값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_seq")
    private Long ingredientSeq;

    // allergy_seq
    @ManyToOne
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

}
