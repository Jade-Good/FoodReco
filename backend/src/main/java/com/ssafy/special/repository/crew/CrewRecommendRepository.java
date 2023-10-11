package com.ssafy.special.repository.crew;

import com.ssafy.special.domain.crew.Crew;
import com.ssafy.special.domain.crew.CrewRecommend;
import com.ssafy.special.domain.crew.CrewRecommendVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewRecommendRepository extends JpaRepository<CrewRecommend, Long> {
    List<CrewRecommend> findAllByCrewOrderByRecommendAtDesc(Crew crew);
    CrewRecommend findByCrewRecommendSeq(Long crewRecommendSeq);
    CrewRecommend findFirstByCrewOrderByRecommendAtDesc(Crew crew);
}
