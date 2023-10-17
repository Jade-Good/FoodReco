package com.ssafy.special.repository.member;

import com.ssafy.special.domain.member.MemberAllergy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MemberAllergyRepository extends JpaRepository<MemberAllergy, Long> {
    List<MemberAllergy> findMemberAllergiesByMember_MemberSeq(Long memberSeq);

}
