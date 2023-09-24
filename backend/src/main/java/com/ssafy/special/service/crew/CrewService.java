package com.ssafy.special.service.crew;

import com.ssafy.special.domain.crew.Crew;
import com.ssafy.special.domain.crew.CrewMember;
import com.ssafy.special.domain.member.Member;
import com.ssafy.special.dto.CrewDto;
import com.ssafy.special.repository.crew.CrewMemberRepository;
import com.ssafy.special.repository.crew.CrewRecommendRepository;
import com.ssafy.special.repository.crew.CrewRepository;
import com.ssafy.special.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CrewService {
    private final MemberRepository memberRepository;
    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final CrewRecommendRepository crewRecommendRepository;

    public List<CrewDto> getCrewListforMemeber(String memberEmail) throws EntityNotFoundException {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));


        // 회원이 속한 모든 크루를 가져옵니다.
        List<CrewDto> crews = new ArrayList<>();
        for (CrewMember c:member.getCrewMembers()) {
            CrewDto crew = new CrewDto().builder()
                    .crewSeq(c.getCrew().getCrewSeq())
                    .name(c.getCrew().getName())
                    .img(c.getCrew().getImg())
                    .status(c.getStatus()==0?"가입 전":"가입")
                    .build();
            crews.add(crew);
        }
        return crews;
    }
}
