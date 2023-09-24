package com.ssafy.special.controller;

import com.ssafy.special.dto.CrewDto;
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
        }
    }

    @PostMapping("/regist")
    public ResponseEntity<?> registCrew(@RequestBody CrewSignUpDto registCrewDto){
        log.info("registCrew() 메소드 시작");
        try{
            crewService.registCrewforMember(registCrewDto,getEmail());
            return ResponseEntity.ok().body("그룹에 가입되었습니다.");
        }catch (IllegalArgumentException e){
            log.info("최소 인원 부족 에러");
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (EntityNotFoundException e){
            log.info("Member find 에러");
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
