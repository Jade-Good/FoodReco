package com.ssafy.special.repository.crew;

import com.ssafy.special.domain.crew.Crew;
import com.ssafy.special.domain.crew.CrewId;
import com.ssafy.special.domain.crew.CrewMember;
import com.ssafy.special.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CrewMemberRepository extends JpaRepository<CrewMember, CrewId> {
    @Override
    List<CrewMember> findAllById(Iterable<CrewId> crewIds);

    Optional<CrewMember> findByCrew(Crew crew);
    Optional<CrewMember> findByMemberAndCrew(Member member,Crew crew);
}
