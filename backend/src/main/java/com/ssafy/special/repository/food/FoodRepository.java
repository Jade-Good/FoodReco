
package com.ssafy.special.repository.food;

import com.ssafy.special.domain.food.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {

    Food findByName(String name);

    Food findFoodByFoodSeq(Long foodSeq);
}
