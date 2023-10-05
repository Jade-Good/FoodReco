package com.ssafy.special.service.etc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.special.domain.crew.Crew;
import com.ssafy.special.domain.crew.CrewMember;
import com.ssafy.special.domain.member.Member;
import com.ssafy.special.dto.response.VoteRecommendDto;
import com.ssafy.special.repository.EmitterRepository;
import com.ssafy.special.repository.crew.CrewRepository;
import com.ssafy.special.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {
    private final static Long DEFAULT_TIMEOUT = 720000000L;
    private final EmitterRepository emitterRepository;
    private final MemberRepository memberRepository;
    private final CrewRepository crewRepository;
    public SseEmitter connect(Long memberSeq) throws EntityNotFoundException{
        memberRepository.findByMemberSeq(memberSeq)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        // 새로운 SseEmitter를 만든다
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        // 유저 ID로 SseEmitter를 저장한다.
        log.info(memberSeq + " sse 등록");
        emitterRepository.save(memberSeq, sseEmitter);

        // 세션이 종료될 경우 저장한 SseEmitter를 삭제한다.
        sseEmitter.onCompletion(() -> emitterRepository.delete(memberSeq));
        sseEmitter.onTimeout(() -> emitterRepository.delete(memberSeq));

        // 503 Service Unavailable 오류가 발생하지 않도록 첫 데이터를 보낸다.
        try {
            log.info(memberSeq + " sse 초기 이벤트 발생");
            sseEmitter.send(SseEmitter.event().id(memberSeq+"").name("connect").data("Connection completed"));
        } catch (IOException exception) {
            log.info("sse 구독 중 에러 발생");
            throw new IllegalStateException("구독 에러");
        }
        return sseEmitter;
    }

    @Transactional
    public void vote(Long crewSeq, Long memberSeq, VoteRecommendDto voteRecommendDto) {
        Crew crew = crewRepository.findByCrewSeq(crewSeq)
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹을 찾을 수 없습니다."));
        for(CrewMember c : crew.getCrewMembers()){
            log.info(c.getMember().getNickname()+"님 sse send1");
            Member crewMember = c.getMember();
            SseEmitter sseEmitter = emitterRepository.get(crewMember.getMemberSeq());
            if (sseEmitter != null && !crewMember.getMemberSeq().equals(memberSeq)) {
                try {
                    sseEmitter.send(SseEmitter.event().id(crewSeq+"").name("vote")
                            .data(voteRecommendDto));
                    log.info(c.getMember().getNickname()+"님 sse send2");
                } catch (IOException exception) {
                    // IOException이 발생하면 저장된 SseEmitter를 삭제하고 예외를 발생시킨다.
                    emitterRepository.delete(memberSeq);
                    log.info("IOException 발생 crewSeq emitter delete");
                    throw new IllegalStateException("sse 알림 중 에러 발생");
                }
            }
        }
    }

    @Transactional
    public void chageVote(Long crewSeq, String status, VoteRecommendDto voteRecommendDto){
        Crew crew = crewRepository.findByCrewSeq(crewSeq)
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹을 찾을 수 없습니다."));
        for(CrewMember c : crew.getCrewMembers()){
            Member crewMember = c.getMember();
            SseEmitter sseEmitter = emitterRepository.get(crewMember.getMemberSeq());
            if(sseEmitter !=null){
                try {
                    if(voteRecommendDto != null){
                        sseEmitter.send(SseEmitter.event().id(crewSeq+"").name(status).data(voteRecommendDto));
                    }else{
                        sseEmitter.send(SseEmitter.event().id(crewSeq+"").name(status).data("그룹 상태 변경"));
                    }
                } catch (IOException exception) {
                    // IOException이 발생하면 저장된 SseEmitter를 삭제하고 예외를 발생시킨다.
                    emitterRepository.delete(crewMember.getMemberSeq());
                    log.info("IOException 발생 crewSeq emitter delete");
                    throw new IllegalStateException("sse 알림 중 에러 발생");
                }
            }else{
                log.info("No emitter");
            }
        }
    }

    @Transactional
    public void voteToOne(Long memberSeq,VoteRecommendDto voteRecommendDto) {
        SseEmitter sseEmitter = emitterRepository.get(memberSeq);
        if (sseEmitter != null) {
            try {
                sseEmitter.send(SseEmitter.event().id(memberSeq+"").name("vote")
                        .data(voteRecommendDto));
                log.info("voteToOne");
            } catch (IOException exception) {
                // IOException이 발생하면 저장된 SseEmitter를 삭제하고 예외를 발생시킨다.
                emitterRepository.delete(memberSeq);
                log.info("IOException 발생 crewSeq emitter delete");
                throw new IllegalStateException("sse 알림 중 에러 발생");
            }
        }

    }
}
