package com.ssafy.special.controller;

import com.ssafy.special.dto.request.FeedbackDto;
import com.ssafy.special.dto.response.RecommendFoodResultDto;
import com.ssafy.special.dto.response.WeatherStatus;
import com.ssafy.special.service.etc.WeatherService;
import com.ssafy.special.service.member.MemberGoogleAuthService;
import com.ssafy.special.service.crew.CrewRecommendService;
import com.ssafy.special.service.member.MemberRecommendService;
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
    private final MemberGoogleAuthService memberGoogleAuthService;
    private final CrewRecommendService crewRecommendService;
    private final WeatherService weatherService;

    @PatchMapping("/feedback/{nextFoodSeq}/{let}/{lon}")
    public ResponseEntity<?> implicitFeedback(@RequestBody FeedbackDto feedbackRequestDto, @PathVariable Long nextFoodSeq, @PathVariable Double let, @PathVariable Double lon){
        String memberEmail = getEmail();
        try {
            String weather;
            try {
                weather = weatherService.getWeather(let, lon);
                log.info("날씨 저장 완료");
            }catch (Exception e){
                weather = "맑음";
            }
            int googleSteps = memberGoogleAuthService.getActivityFromGoogle(memberEmail);
            log.info(Integer.toString(googleSteps));
            memberRecommendService.implicitFeedback(memberEmail, feedbackRequestDto, nextFoodSeq, googleSteps, weather);
            log.info("묵시적 피드백 업데이트 완료");
            return ResponseEntity.ok().body("묵시적 피드백 업데이트 완료");
        }catch (EntityNotFoundException e){
            log.info("요청한 사용자가 해당 음식을 추천 받은 적 없음");
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            log.info("처리되지 않은 에러 발생");
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/personal/{let}/{lon}")
    public ResponseEntity<?> personalRecommendation(@PathVariable Double let, @PathVariable Double lon){
        String memberEmail = getEmail();
        try {
            String weather;
            try {
                weather = weatherService.getWeather(let, lon);
                log.info("날씨 저장 완료");
            }catch (Exception e){
                weather = "맑음";
            }
            int googleCalorie = memberGoogleAuthService.getActivityFromGoogle(memberEmail);
            log.info(Integer.toString(googleCalorie));
            List<RecommendFoodResultDto> recommendFoodDtoList = memberRecommendService.recommendFood(memberEmail, googleCalorie, weather);
            log.info("추천 완료");
            return ResponseEntity.ok().body(recommendFoodDtoList);
        }catch (EntityNotFoundException e){
            log.info("해당 사용자 없음");
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            log.info("처리되지 않은 에러 발생");
            e.printStackTrace();
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


    public String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
