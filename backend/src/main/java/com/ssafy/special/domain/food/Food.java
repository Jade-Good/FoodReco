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
    @Column(name = "name", length = 30)
    private String foodName;

    // food_cooking_method
    @NotNull
    @Column(name = "cooking_method", length = 16)
    private String foodCookingMethod;

    // food_type
    @NotNull
    @Column(name = "type", length = 12)
    private String foodType;

    // food_category
    @NotNull
    @Column(name = "category", length = 12)
    private String foodCategory;

    // food_association
    @NotNull
    @Column(name = "association",columnDefinition = "tinyint")
    private int food_type;

    // avg_price
    @NotNull
    @Column(name = "avg_price")
    private int avgPrice;

    // img
    @NotNull
    @Column(name = "img", length = 50)
    private String img;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL)
    private List<Ingredient> ingredientList;
}
