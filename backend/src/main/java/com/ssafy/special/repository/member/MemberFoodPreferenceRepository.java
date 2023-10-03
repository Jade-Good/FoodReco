package com.ssafy.special.repository.member;

import com.ssafy.special.domain.member.Member;
import com.ssafy.special.domain.member.MemberFoodPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberFoodPreferenceRepository extends JpaRepository<MemberFoodPreference, Long>{


    Optional<MemberFoodPreference> findMemberFoodPreferenceByMember_MemberSeqAndFood_FoodSeq(Long memberSeq, Long foodSeq);

    List<MemberFoodPreference> findMemberFoodPreferencesByMember_MemberSeqAndPreferenceType(Long memberSeq, int preferenceType);

    @Modifying
    @Query("update member_food_preference mfp SET mfp.preferenceType = :preferenceType WHERE mfp.member.memberSeq = :memberSeq AND mfp.food.foodSeq = :foodSeq")
    void updateMemberFoodPreference(@Param("preferenceType") int preferenceType,
                                           @Param("memberSeq") Long memberSeq,
                                           @Param("foodSeq") Long foodSeq);


}
