package com.ssafy.special.repository.crew;

import com.ssafy.special.domain.crew.CrewMember;
import com.ssafy.special.domain.crew.CrewRecommendVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewRecommendVoteRepository extends JpaRepository<CrewRecommendVote, CrewRecommendVote.VoteId> {

}
