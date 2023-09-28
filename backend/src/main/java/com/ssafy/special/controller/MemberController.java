package com.ssafy.special.controller;



import com.ssafy.special.dto.request.*;

import com.ssafy.special.service.etc.FcmService;
import com.ssafy.special.service.member.MemberService;
import com.ssafy.special.service.member.VerificationService;
import com.ssafy.special.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final VerificationService verificationService;
    private final MemberService memberService;
    private final RedisUtil redisUtil;
    private final FcmService fcmService;
    // 회원가입 API
    @PostMapping("/regist")
    public ResponseEntity<Map<String, String>> signUp(@RequestBody UserSignUpDto userSignUpDto) {

        Map<String, String> resultMap = new HashMap<>();
        HttpStatus status = null;

        try {
            memberService.signUp(userSignUpDto);

            resultMap.put("message", "회원가입 성공");
            status = HttpStatus.OK;

        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    // 이메일 인증번호 발송
    @PostMapping("/verification/email")
    private ResponseEntity<Map<String, String>> setVerifyCode(@RequestBody UserEmailDto userEmailDto) {

        Map<String, String> resultMap = new HashMap<>();
        HttpStatus status = null;
        try {
            verificationService.sendVerifyCode(userEmailDto.getEmail());
            resultMap.put("message", "인증번호 전송 완료");
            resultMap.put("code", redisUtil.getData(userEmailDto.getEmail()));
            status = HttpStatus.OK;
        } catch (Exception e) {
            resultMap.put("message", "인증번호 전송 실패");
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(resultMap, status);
    }

    // 이메일 인증번호 체크
    @PostMapping("/verification/email/code")
    private ResponseEntity<Map<String, String>> checkVerifyCode(@RequestBody UserCodeDto userCodeDto) {

        Map<String, String> resultMap = new HashMap<>();
        HttpStatus status = null;
        int verified = verificationService.check(userCodeDto.getEmail(), userCodeDto.getCode());

        switch (verified) {
            case 0:
                resultMap.put("message", "인증 기간 만료");
                status = HttpStatus.BAD_REQUEST;
                break;
            case 1:
                resultMap.put("message", "인증 완료");
                status = HttpStatus.ACCEPTED;
                break;
            case 2:
                resultMap.put("message", "인증 실패");
                status = HttpStatus.NOT_ACCEPTABLE;
                break;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    // 이메일 중복 검사
    @PostMapping("/checkEmail")
    public ResponseEntity<Map<String, String>> checkEmail(@RequestBody UserEmailDto userEmailDto) {
        Map<String, String> resultMap = new HashMap<>();
        HttpStatus status = null;
        try {
            memberService.checkEmail(userEmailDto.getEmail());
            resultMap.put("message", "이메일 사용 가능");
            status = HttpStatus.ACCEPTED;

        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    // 닉네임 중복 검사
    @PostMapping("/checkNickname")
    public ResponseEntity<Map<String, String>> checkNickname(@RequestBody UserNicknameDto userNicknameDto) {
        Map<String, String> resultMap = new HashMap<>();
        HttpStatus status = null;
        try {
            memberService.checkNickname(userNicknameDto.getNickName());
            resultMap.put("message", "닉네임 사용 가능");
            status = HttpStatus.ACCEPTED;

        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @PostMapping("/fcm")
    public ResponseEntity<?> sendMessage(@RequestBody FcmMessageDto fcmMessageDto){
        log.info(fcmMessageDto.toString());
        log.info("sendMessage() 메소드 시작");
        try{
            fcmService.sendNotification(fcmMessageDto);
            return ResponseEntity.ok().body("정상적으로 전송되었습니다.");
        }catch (EntityNotFoundException e){
            log.info("Crew find 에러 : "+ e.getMessage());
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
