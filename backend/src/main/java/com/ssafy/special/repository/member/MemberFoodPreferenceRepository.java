package com.ssafy.special.repository.member;

import com.ssafy.special.domain.member.Member;
import com.ssafy.special.domain.member.MemberFoodPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberFoodPreferenceRepository extends JpaRepository<MemberFoodPreference, Long>{

}
