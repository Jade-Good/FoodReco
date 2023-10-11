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
    private String name;

    // food_cooking_method
    @NotNull
    @Column(name = "cooking_method", length = 9)
    private String cookingMethod;

    // food_type
    @NotNull
    @Column(name = "type", length = 15)
    private String type;

    // food_category
    @NotNull
    @Column(name = "category", length = 20)
    private String category;

    // img
    @NotNull
    @Column(name = "img", length = 50)
    private String img;

    @Builder
    public Food(Long foodSeq, String name, String cookingMethod, String type, String category, String img) {
        this.foodSeq = foodSeq;
        this.name = name;
        this.cookingMethod = cookingMethod;
        this.type = type;
        this.category = category;

        this.img = img;
    }
}
