package com.ssafy.special.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.special.service.member.GoogleAuthService;
import com.ssafy.special.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/google")
public class GoogleOauthController {
    private final GoogleAuthService googleAuthService;
    private final SecurityUtils securityUtils;
    @PatchMapping("/auth")
    public ResponseEntity<?> getAuthTokens(@RequestBody Map<String, String> exchangeToken) {
        String memberEmail = securityUtils.getEmail();

        try{
            String tokens = googleAuthService.getAccessTokens(memberEmail, exchangeToken.get("exchangeToken"));
            googleAuthService.storeAuthTokens(memberEmail, tokens);
            googleAuthService.getActivityFromGoogle(memberEmail);
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


}
