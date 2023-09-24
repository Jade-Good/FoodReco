package com.ssafy.special.repository.crew;

import com.ssafy.special.domain.crew.CrewMember;
import com.ssafy.special.domain.crew.CrewRecommendVote;
import com.ssafy.special.domain.crew.VoteId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewRecommendVoteRepository extends JpaRepository<CrewRecommendVote, VoteId> {

}
