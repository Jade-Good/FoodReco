package com.ssafy.special.service.member;

import com.ssafy.special.domain.member.Member;
import com.ssafy.special.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor  // final 키워드가 붙은 필드를 사용하여 생성자를 제공하는 어노테이션
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    // 아이디 중복 값 검사
    public boolean duplicateEmail(String userEmail) {
        Member member = memberRepository.findByEmail(userEmail);
        if (member == null) {
            // 중복된 email이 없다면 true
            return true;
        } else {
            // 중복된 email 있으면 false
            return false;
        }

    }

}
