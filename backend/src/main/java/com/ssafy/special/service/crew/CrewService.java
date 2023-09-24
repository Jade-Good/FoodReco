package com.ssafy.special.service.crew;

import com.ssafy.special.domain.crew.Crew;
import com.ssafy.special.domain.crew.CrewMember;
import com.ssafy.special.domain.member.Member;
import com.ssafy.special.dto.CrewDto;
import com.ssafy.special.dto.CrewSignUpDto;
import com.ssafy.special.repository.crew.CrewMemberRepository;
import com.ssafy.special.repository.crew.CrewRecommendRepository;
import com.ssafy.special.repository.crew.CrewRepository;
import com.ssafy.special.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CrewService {
    private final MemberRepository memberRepository;
    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final CrewRecommendRepository crewRecommendRepository;

    /*
     * 사용자 Email으로 자신이 속한 crew List을 출력하는 메소드
     * 사용자 정보를 가져온 후 Crew 내용을 제공한다.
     */
    public List<CrewDto> getCrewListforMemeber(String memberEmail) throws EntityNotFoundException {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
        // 회원이 속한 모든 크루를 가져옵니다.
        List<CrewDto> crews = new ArrayList<>();
        for (CrewMember c:member.getCrewMembers()) {
            CrewDto crew = CrewDto.builder()
                    .crewSeq(c.getCrew().getCrewSeq())
                    .name(c.getCrew().getName())
                    .img(c.getCrew().getImg())
                    .status(c.getStatus()==0?"가입 수락 전":"가입 수락 후")
                    .build();
            crews.add(crew);
        }
        return crews;
    }

    @Transactional
    public void registCrewforMember(CrewSignUpDto crewSignUpDto, String memberEmail)
            throws IllegalArgumentException, EntityNotFoundException {
        if(crewSignUpDto.getCrewMembers().isEmpty()) {
            throw new IllegalArgumentException("그룹은 최소 2명 이상이여야 합니다.");
        }
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
        // Crew 생성
        Crew crew = Crew.builder()
                .name(crewSignUpDto.getCrewName())
                .img(crewSignUpDto.getCrewImg())
                .status("투표전")
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .build();
        crew = crewRepository.save(crew);

        for (Long memberSeq:crewSignUpDto.getCrewMembers()) {
            CrewMember crewMember = CrewMember.builder()
                    .crew(crew)
                    .member(Member.builder().memberSeq(memberSeq).build())
                    .build();
            crewMemberRepository.save(crewMember);
        }
        CrewMember crewMember = CrewMember.builder()
                .crew(crew)
                .member(member)
                .status(1)
                .build();
        crewMemberRepository.save(crewMember);
        log.info("crew 생성 완료");
    }
}
