package com.ssafy.special.repository.food;

import com.ssafy.special.domain.food.Food;
import com.ssafy.special.domain.food.FoodId;
import com.ssafy.special.domain.food.FoodIngredient;
import com.ssafy.special.domain.food.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodIngredientRepository extends JpaRepository<FoodIngredient, FoodId> {
    List<FoodIngredient> findFoodIngredientsByFood_FoodSeq(Long foodSeq);

    List<FoodIngredient> findFoodIngredientsByFood_FoodSeqAndIngredient_IsAllergy(Long foodSeq, int isAllergy);

}
