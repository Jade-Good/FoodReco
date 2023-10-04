package com.ssafy.special.repository.member;

import com.ssafy.special.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{
        Optional<Member> findByEmail(String email);
        Optional<Member> findByNickname(String nickname);
        Optional<Member> findByJwtRefreshToken(String refreshToken);
        Optional<Member> findByMemberSeq(Long memberSeq);
        @Modifying
        @Query("UPDATE member m SET m.googleRefreshToken = :refreshToken WHERE m.memberSeq = :memberSeq")
        void updateMemberRefreshToken(@Param("memberSeq") Long memberSeq, @Param("refreshToken") String refreshToken);
        @Query("SELECT m.googleRefreshToken FROM member m WHERE m.memberSeq = :memberSeq")
        Optional<String> findGoogleRefreshTokenByMemberSeq(@Param("memberSeq") Long memberSeq);

        @Modifying
        @Query("UPDATE member m SET m.activity = :activity WHERE m.memberSeq = :memberSeq")
        void updateMemberActivity(@Param("memberSeq") Long memberSeq, @Param("activity") int activity);
}
