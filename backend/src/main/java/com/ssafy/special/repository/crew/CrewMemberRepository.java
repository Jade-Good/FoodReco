package com.ssafy.special.repository.crew;

import com.ssafy.special.domain.crew.CrewId;
import com.ssafy.special.domain.crew.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrewMemberRepository extends JpaRepository<CrewMember, CrewId> {
    @Override
    List<CrewMember> findAllById(Iterable<CrewId> crewIds);
}
