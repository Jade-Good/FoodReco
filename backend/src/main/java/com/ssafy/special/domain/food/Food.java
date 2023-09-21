package com.ssafy.special.domain.food;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity(name="food")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    @Builder.Default
    private List<FoodIngredient> foodIngredientList = new ArrayList<>();

    @Builder
    public Food(Long foodSeq, String foodName, String foodCookingMethod, String foodType, String foodCategory, int food_type, int avgPrice, String img, List<FoodIngredient> foodIngredientList) {
        this.foodSeq = foodSeq;
        this.foodName = foodName;
        this.foodCookingMethod = foodCookingMethod;
        this.foodType = foodType;
        this.foodCategory = foodCategory;
        this.food_type = food_type;
        this.avgPrice = avgPrice;
        this.img = img;
        this.foodIngredientList = foodIngredientList;
    }
}
