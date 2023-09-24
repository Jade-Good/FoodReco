package com.ssafy.special.repository.crew;

import com.ssafy.special.domain.crew.Crew;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrewRepository extends JpaRepository<Crew, Long> {

    Optional<Crew> findByCrewSeq(Long crewSeq);
}
