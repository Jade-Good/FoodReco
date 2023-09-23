package com.ssafy.special.controller;

import com.ssafy.special.dto.UserSignUpDto;
import com.ssafy.special.service.member.MemberService;
import com.ssafy.special.service.member.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final VerificationService verificationService;
    private final MemberService memberService;

    /*
     * 회원가입 메소드
     */
    @PostMapping("/sign-up")
    public String signUp(@RequestBody UserSignUpDto userSignUpDto) throws Exception {
        memberService.signUp(userSignUpDto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @PostMapping("/sendVerification")
    private ResponseEntity<Map<String,String>> setVerifyCode(@RequestParam String email) {
        Map<String,String> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        try {
            verificationService.sendVerifyCode(email);
            resultMap.put("message", "인증번호 전송 완료");
        } catch(Exception e) {
            resultMap.put("message","인증번호 전송 실패");
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(resultMap,status);
    }

    @PostMapping("/checkVerification")
    private ResponseEntity<Map<String,String>> checkVerifyCode(@RequestParam String email, @RequestParam String code) {

        Map<String,String> resultMap = new HashMap<>();
        HttpStatus status = null;
        int verified = verificationService.check(email, code);

        switch(verified) {
            case 0:
                resultMap.put("message","인증 기간 만료");
                status = HttpStatus.BAD_REQUEST;
                break;
            case 1:
                resultMap.put("message", "인증 완료");
                status = HttpStatus.ACCEPTED;
                break;
            case 2:
                resultMap.put("message", "인증 실패");
                status = HttpStatus.BAD_REQUEST;
                break;
        }

        return new ResponseEntity<>(resultMap,status);
    }

    // 사용자 Email 가져오는 Email
    public String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
