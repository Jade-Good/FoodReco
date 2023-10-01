package com.ssafy.special.repository.crew;

import com.ssafy.special.domain.crew.CrewRecommend;
import com.ssafy.special.domain.crew.CrewRecommendFood;
import com.ssafy.special.domain.crew.CrewRecommendVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrewRecommendVoteRepository extends JpaRepository<CrewRecommendVote, Long> {

    List<CrewRecommendVote> findAllByCrewRecommendFood(CrewRecommendFood crewRecommendFood);
}
