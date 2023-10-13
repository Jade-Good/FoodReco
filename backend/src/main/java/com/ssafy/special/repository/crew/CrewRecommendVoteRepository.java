package com.ssafy.special.repository.crew;

import com.ssafy.special.domain.crew.CrewRecommend;
import com.ssafy.special.domain.crew.CrewRecommendFood;
import com.ssafy.special.domain.crew.CrewRecommendVote;
import com.ssafy.special.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CrewRecommendVoteRepository extends JpaRepository<CrewRecommendVote, Long> {

    List<CrewRecommendVote> findAllByCrewRecommendFood(CrewRecommendFood crewRecommendFood);
    Optional<CrewRecommendVote> findByCrewRecommendFoodAndMember(CrewRecommendFood crewRecommendFood, Member member);
}
