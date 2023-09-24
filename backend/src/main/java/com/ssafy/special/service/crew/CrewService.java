package com.ssafy.special.service.crew;

import com.ssafy.special.domain.crew.Crew;
import com.ssafy.special.domain.crew.CrewId;
import com.ssafy.special.domain.crew.CrewMember;
import com.ssafy.special.domain.member.Member;
import com.ssafy.special.dto.*;
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
    public List<CrewDto> getCrewListforMemeber(String memberEmail)
            throws EntityNotFoundException{
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
        // 회원이 속한 모든 크루를 가져옵니다.
        List<CrewDto> crews = new ArrayList<>();
        for (CrewMember c:member.getCrewMembers()) {
            CrewDto crew = CrewDto.builder()
                    .crewSeq(c.getCrew().getCrewSeq())
                    .name(c.getCrew().getName())
                    .img(c.getCrew().getImg())
                    .status(c.getStatus()==0?"미반응":(c.getStatus()==-1?"거절":"수락"))
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
                    .member(
                            Member.builder()
                                    .memberSeq(memberSeq)
                                    .build()
                    )
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

    @Transactional
    public void joinCrewforMember(Long crewSeq, String memberEmail)
                    throws EntityNotFoundException,IllegalArgumentException{
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
        CrewId crewId = CrewId.builder()
                        .crew(crewSeq)
                        .member(member.getMemberSeq())
                        .build();
        crewMemberRepository.findById(crewId)
                .ifPresentOrElse(
                        crewJoin ->{
                            if(crewJoin.getStatus() == 1){
                                throw new IllegalArgumentException("이미 가입한 그룹입니다.");
                            }
                            crewJoin.setStatus(1);
                            crewMemberRepository.save(crewJoin);
                        } ,
                        ()->{
                            throw new IllegalArgumentException("해당하는 그룹이 없습니다.");
                        }
                );
    }

    public CrewDetailDto getDetailInfo(Long crewSeq, String memberEmail)
            throws EntityNotFoundException,IllegalArgumentException {
        Crew crew = crewRepository.findByCrewSeq(crewSeq)
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹을 찾을 수 없습니다."));
        List<CrewMembersDto> crewMembers = new ArrayList<>();
        String memberStatus="";
        for (CrewMember c : crew.getCrewMembers()) {
            Member m = c.getMember();
            if (m.getEmail().equals(memberEmail)) {
                memberStatus = c.getStatus() == 0 ? "미응답" : (c.getStatus() == -1 ? "거절" : "수락");
            }else{
                crewMembers.add(CrewMembersDto.builder()
                        .memberSeq(m.getMemberSeq())
                        .memberName(m.getNickname())
                        .memberStatus(c.getStatus() == 0 ? "미응답" : (c.getStatus() == -1 ? "거절" : "수락"))
                        .memberImg(m.getImg())
                        .build()
                );
            }
        }
        if(memberStatus.equals("")){
            throw new IllegalArgumentException("그룹 초대가 되지 않은 사용자입니다.");
        }
        CrewDetailDto crewDetailDto = CrewDetailDto.builder()
                .crewSeq(crew.getCrewSeq())
                .crewName(crew.getName())
                .crewImg(crew.getImg())
                .crewStatus(crew.getStatus())
                .memberStatus(memberStatus)
                .crewMembers(crewMembers)
                .build();
        crewDetailDto.setCrewMembers(crewMembers);
        return crewDetailDto;
    }
}
