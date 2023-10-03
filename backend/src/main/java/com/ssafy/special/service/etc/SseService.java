package com.ssafy.special.service.etc;

import com.ssafy.special.domain.crew.Crew;
import com.ssafy.special.domain.crew.CrewMember;
import com.ssafy.special.domain.member.Member;
import com.ssafy.special.dto.request.VoteDto;
import com.ssafy.special.repository.EmitterRepository;
import com.ssafy.special.repository.crew.CrewMemberRepository;
import com.ssafy.special.repository.crew.CrewRepository;
import com.ssafy.special.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {
    private final static Long DEFAULT_TIMEOUT = 7200000L;
    private final static String NOTIFICATION_NAME = "notification";
    private final EmitterRepository emitterRepository;
    private final MemberRepository memberRepository;
    private final CrewRepository crewRepository;
    public SseEmitter connectVote(String memberEmail) throws EntityNotFoundException{
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        // 새로운 SseEmitter를 만든다
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        // 유저 ID로 SseEmitter를 저장한다.
        log.info(memberEmail + " sse 등록");
        emitterRepository.save(member.getMemberSeq(), sseEmitter);

        // 세션이 종료될 경우 저장한 SseEmitter를 삭제한다.
        sseEmitter.onCompletion(() -> emitterRepository.delete(member.getMemberSeq()));
        sseEmitter.onTimeout(() -> emitterRepository.delete(member.getMemberSeq()));

        // 503 Service Unavailable 오류가 발생하지 않도록 첫 데이터를 보낸다.
        try {
            log.info(memberEmail + " sse 초기 이벤트 발생");
            sseEmitter.send(SseEmitter.event().id(memberEmail).name("setting").data("Connection completed"));
        } catch (IOException exception) {
            log.info("sse 구독 중 에러 발생");
            throw new IllegalStateException("IOException");
        }
        return sseEmitter;
    }

//    public void vote(VoteDto voteDto) {
//        Crew crew = crewRepository.findByCrewSeq(voteDto.getCrewSeq())
//                .orElseThrow(() -> new EntityNotFoundException("해당 그룹을 찾을 수 없습니다."));
//
//
//
//
//
//        Optional<SseEmitter> sseEmitter = emitterRepository.get(memberEmail)
//                .ifPresent(sseEmitter);
//        if (sseEmitter.isPresent()) {
//            try {
//                sseEmitter.get().send(SseEmitter.event().id(crewSeq+"").name("voteStart");
//            } catch (IOException exception) {
//                // IOException이 발생하면 저장된 SseEmitter를 삭제하고 예외를 발생시킨다.
//                emitterRepository.delete(crewSeq);
//                log.info("IOException 발생 crewSeq emitter delete");
//                throw new IllegalStateException("IOException");
//            }
//        } else {
//            log.info("No emitter");
//        }
//    }
//
//    public void send(SaveWarningDto saveWarningDto) {
//        User user = userRepository.findByUserNo(saveWarningDto.getStudentNo());
//        Optional<SseEmitter> sseEmitter = emitterRepository.get(user.getUserId());
//        if (sseEmitter.isPresent()) {
//            try {
//                sseEmitter.get().send(SseEmitter.event().id(saveWarningDto.getTeacherId()).name("send")
//                        .data("알림"));
//            } catch (IOException exception) {
//                // IOException이 발생하면 저장된 SseEmitter를 삭제하고 예외를 발생시킨다.
//                emitterRepository.delete(user.getUserId());
//                logger.info("*** IOException 발생 user.getUserId() emitter delete");
//                throw new IllegalStateException("IOException");
//            }
//        } else {
//            logger.info("*** No emitter found");
//        }
//    }
}
