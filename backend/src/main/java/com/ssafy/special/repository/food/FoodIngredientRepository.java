package com.ssafy.special.repository.food;

import com.ssafy.special.domain.food.Food;
import com.ssafy.special.domain.food.FoodId;
import com.ssafy.special.domain.food.FoodIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodIngredientRepository extends JpaRepository<FoodIngredient, FoodId> {

}
