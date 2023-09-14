package com.ssafy.special.domain.food;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity(name="food")
@NoArgsConstructor
public class Food {

    // food_seq, PK값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_seq")
    private Long foodSeq;

    // food_name
    @NotNull
    @Column(name = "food_name", length = 30)
    private String foodName;

    // food_cooking_method
    @NotNull
    @Column(name = "food_cooking_method", length = 16)
    private String foodCookingMethod;

    // food_type
    @NotNull
    @Column(name = "food_type", length = 12)
    private String foodType;

    // food_category
    @NotNull
    @Column(name = "food_category", length = 12)
    private String foodCategory;

    // food_association
    @NotNull
    @Column(name = "food_association",columnDefinition = "tinyint")
    private int food_type;

    @OneToMany(mappedBy = "food",fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Ingredient> ingredientList;
}
