package com.ssafy.special.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.special.service.member.MemberGoogleAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/google")
public class MemberGoogleOauthController {
    private final MemberGoogleAuthService memberGoogleAuthService;
    @PatchMapping("/auth")
    public ResponseEntity<?> getAuthTokens(@RequestBody Map<String, String> exchangeToken) {
        String memberEmail = getEmail();

        try{
            String tokens = memberGoogleAuthService.getAccessTokens(memberEmail, exchangeToken.get("exchangeToken"));
            memberGoogleAuthService.storeRefreshToken(memberEmail, tokens);
            return ResponseEntity.ok().body("완료");
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (HttpClientErrorException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("토큰 형식이 잘못됐습니다");
        }catch (JsonProcessingException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("처리되지 않은 예외");
        }
    }


    // 사용자 Email 가져오는 Email
    public String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
