package com.ssafy.special.repository.crew;

import com.ssafy.special.domain.crew.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewMemberRepository extends JpaRepository<CrewMember, CrewMember.CrewId> {

}
