package com.ssafy.special.controller;

import com.ssafy.special.dto.request.*;
import com.ssafy.special.service.crew.CrewService;
import com.ssafy.special.service.etc.FcmService;
import com.ssafy.special.service.etc.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/crew")
@RequiredArgsConstructor
public class CrewController {
    private final CrewService crewService;
    private final FcmService fcmService;
    private final SseService sseService;
    /*
     * 사용자의 crew list를 가져오는 메소드
     */
    @GetMapping("/list")
    public ResponseEntity<?> getCrewList(){
        log.info("getCrewList() 메소드 시작");
        try{
            String memberEmail = getEmail();
            List<CrewDto> crewMember = crewService.getCrewListforMemeber(memberEmail);
            log.info("crew list 반환");
            return ResponseEntity.ok().body(crewMember);
        }catch (EntityNotFoundException e){
            log.info("Member find 에러");
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            log.info("처리되지 않은 에러 발생 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(path = "/regist", consumes = {"multipart/form-data"})
    public ResponseEntity<?> registCrew(CrewSignUpDto registCrewDto){
        log.info("registCrew() 메소드 시작");
        try{
            crewService.registCrewforMember(registCrewDto,getEmail());
            return ResponseEntity.ok().body("그룹이 등록되었습니다.");
        }catch (IllegalArgumentException e){
            log.info("최소 인원 부족 에러");
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (EntityNotFoundException e){
            log.info("Member find 에러");
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            log.info("처리되지 않은 에러 발생 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinCrew(@RequestBody CrewJoinDto joinDto){
        log.info("joinCrew() 메소드 시작");
        try{
            crewService.joinCrewforMember(joinDto,getEmail());
            return ResponseEntity.ok().body("처리되었습니다.");
        }catch (IllegalArgumentException e){
            log.info("그룹 가입 간 에러 발생 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (EntityNotFoundException e){
            log.info("Member find 에러 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            log.info("처리되지 않은 에러 발생 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/detail/{crewSeq}")
    public ResponseEntity<?> getDetailInfoforCrew(@PathVariable Long crewSeq){
        log.info("getDetailInfoforCrew() 메소드 시작");
        try{
            return ResponseEntity.ok().body(crewService.getDetailInfo(crewSeq,getEmail()));
        }catch (IllegalArgumentException e){
            log.info("초대가 되지 않은 사용자 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (EntityNotFoundException e){
            log.info("Crew find 에러 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            log.info("처리되지 않은 에러 발생 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping ("/update")
    public ResponseEntity<?> updateCrewInfo(@RequestBody CrewUpdateDto CrewUpdateDto){
        log.info("updateCrewInfo() 메소드 시작");
        try{
            crewService.updateCrew(CrewUpdateDto);
            return ResponseEntity.ok().body("정상적으로 수정되었습니다.");
        }catch (IllegalArgumentException e){
            log.info("변경 불가능한 투표 상태 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (EntityNotFoundException e){
            log.info("Crew find 에러 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (IllegalStateException e){
            log.info("이미지 업로드 에러 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            log.info("처리되지 않은 에러 발생 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/send/notification")
    public ResponseEntity<?> sendMessageToCrew(@RequestBody FcmMessageDto fcmMessageDto){
        log.info("sendMessageToCrew() 메소드 시작");
        try{
            fcmService.sendNotificationToCrew(fcmMessageDto);
            return ResponseEntity.ok().body("정상적으로 전송되었습니다.");
        }catch (EntityNotFoundException e){
            log.info("Crew find 에러 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (IllegalArgumentException e){
            log.info("FCM 에러 발생 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            log.info("처리되지 않은 에러 발생 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping(value = "/sse/{crewSeq}/{memberSeq}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> sseSubscribe(@PathVariable Long crewSeq, @PathVariable Long memberSeq){
        log.info("sseSubscribe() 메소드 시작");
        try{
            SseEmitter sse = sseService.connect(memberSeq);
            return ResponseEntity.ok().body(sse);
        }catch (IllegalArgumentException e){
            log.info(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/vote")
    public ResponseEntity<?> voteFood(@RequestBody VoteDto voteDto){
        log.info("voteFood() 메소드 시작");
        try{
            crewService.vote(voteDto,getEmail());
            return ResponseEntity.ok().body("정상적으로 전송되었습니다.");
        }catch (EntityNotFoundException e){
            log.info("Crew find 에러 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (IllegalArgumentException e){
            log.info("FCM 에러 발생 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            log.info("처리되지 않은 에러 발생 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // 사용자 Email 가져오는 Email
    public String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
