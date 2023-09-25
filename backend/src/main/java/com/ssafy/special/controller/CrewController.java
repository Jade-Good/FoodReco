package com.ssafy.special.controller;

import com.ssafy.special.dto.CrewDto;
import com.ssafy.special.dto.CrewJoinDto;
import com.ssafy.special.dto.CrewSignUpDto;
import com.ssafy.special.service.crew.CrewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/crew")
@RequiredArgsConstructor
public class CrewController {
    private final CrewService crewService;
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

    @PostMapping("/regist")
    public ResponseEntity<?> registCrew(@RequestBody CrewSignUpDto registCrewDto){
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
    public ResponseEntity<?> updateCrewInfo(@RequestBody CrewDto crewDto){
        log.info("updateCrewInfo() 메소드 시작");
        try{
            crewService.updateCrew(crewDto);
            return ResponseEntity.ok().body("정상적으로 수정되었습니다.");
        }catch (IllegalArgumentException e){
            log.info("변경 불가능한 투표 상태 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (EntityNotFoundException e){
            log.info("Crew find 에러 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            log.info("처리되지 않은 에러 발생 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //@RequestBody SaveAttentionRateDto saveAttentionRateDto
    //@PathVariable Long userNo

    // 사용자 Email 가져오는 Email
    public String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
