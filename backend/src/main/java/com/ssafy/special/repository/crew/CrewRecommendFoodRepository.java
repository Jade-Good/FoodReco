package com.ssafy.special.repository.crew;

import com.ssafy.special.domain.crew.CrewRecommend;
import com.ssafy.special.domain.crew.CrewRecommendFood;
import com.ssafy.special.domain.crew.CrewRecommendVote;
import com.ssafy.special.domain.food.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrewRecommendFoodRepository extends JpaRepository<CrewRecommendFood, Long> {

    List<CrewRecommendFood> findAllByCrewRecommend(CrewRecommend crewRecommend);

    CrewRecommendFood findByCrewRecommendAndFood(CrewRecommend crewRecommend, Food food);
}
