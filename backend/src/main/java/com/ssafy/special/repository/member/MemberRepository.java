package com.ssafy.special.repository.member;

import com.ssafy.special.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{
        Optional<Member> findByEmail(String email);
        Optional<Member> findByNickname(String nickname);
        Optional<Member> findByJwtRefreshToken(String refreshToken);
        Optional<Member> findByMemberSeq(Long memberSeq);
}
