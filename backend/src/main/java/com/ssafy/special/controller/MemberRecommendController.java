package com.ssafy.special.controller;

import com.ssafy.special.dto.request.FeedbackDto;
import com.ssafy.special.dto.response.RecommendFoodResultDto;
import com.ssafy.special.service.crew.CrewRecommendService;
import com.ssafy.special.service.member.MemberRecommendService;
import com.ssafy.special.util.SecurityUtils;
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
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
public class MemberRecommendController {
    private final MemberRecommendService memberRecommendService;
    private final CrewRecommendService crewRecommendService;
    private final SecurityUtils securityUtils;

    @PatchMapping("/feedback/{nextFoodSeq}")
    public ResponseEntity<?> implicitFeedback(@RequestBody FeedbackDto feedbackRequestDto, @PathVariable Long nextFoodSeq ){
        String memberEmail = securityUtils.getEmail();
        try {
            memberRecommendService.implicitFeedback(memberEmail, feedbackRequestDto, nextFoodSeq);
            log.info("묵시적 피드백 업데이트 완료");
            return ResponseEntity.ok().body("묵시적 피드백 업데이트 완료");
        }catch (EntityNotFoundException e){
            log.info("요청한 사용자가 해당 음식을 추천 받은 적 없음");
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            log.info("처리되지 않은 에러 발생");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/personal")
    public ResponseEntity<?> personalRecommendation(){
        String memberEmail = securityUtils.getEmail();
        try {
            List<RecommendFoodResultDto> recommendFoodDtoList = memberRecommendService.recommendFood(memberEmail);
            log.info("추천 완료");
            return ResponseEntity.ok().body(recommendFoodDtoList);
        }catch (EntityNotFoundException e){
            log.info("해당 사용자 없음");
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            log.info("처리되지 않은 에러 발생");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/crew/{crewSeq}")
    public ResponseEntity<?> crewRecommendation(@PathVariable Long crewSeq){
        log.info("crewRecommendation() 메소드 시작");
        try{
            crewRecommendService.recommendFood(crewSeq);
            return ResponseEntity.ok().body("그룹 투표 시작");
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


//    @GetMapping("/detail{foodSeq}")
//    public ResponseEntity<?> foodDetail(@PathVariable Long foodSeq){
//        String memberEmail = securityUtils.getEmail();
//        try {
//            Food foodDetail = memberRecommendService.getFoodDetail(foodSeq, memberEmail);
//            return ResponseEntity.ok().body(foodDetail);
//        }catch (EntityNotFoundException e){
//            log.info("묵시적 피드백 반영시 에러발생");
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }catch (Exception e){
//            log.info("처리되지 않은 에러 발생");
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    public String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
