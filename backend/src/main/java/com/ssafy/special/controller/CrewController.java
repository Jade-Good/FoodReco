package com.ssafy.special.controller;

import com.ssafy.special.domain.crew.Crew;
import com.ssafy.special.dto.CrewDto;
import com.ssafy.special.service.crew.CrewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/crew")
@RequiredArgsConstructor
public class CrewController {
    private final CrewService crewService;
    @GetMapping("/list")
    public ResponseEntity<?> getCrewList(){
        log.info("crew list 반환11");
        try{
            String memberEmail = getEmail();
            List<CrewDto> crewMember = crewService.getCrewListforMemeber(memberEmail);
            log.info("crew list 반환");
            return ResponseEntity.ok().body(crewMember);
        }catch (EntityNotFoundException e){
            log.info("crew list 찾기 에러");
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
