package com.ssafy.special.controller;


import com.ssafy.special.dto.request.CrewDto;
import com.ssafy.special.dto.request.JwtTokenDto;
import com.ssafy.special.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/jwt")
@RequiredArgsConstructor
public class JwtController {
    private final JwtService jwtService;
    @PostMapping
    public ResponseEntity<?> updateRefreshToken(@RequestBody JwtTokenDto jwtTokenDto){
        log.info("updateRefreshToken() 메소드 시작");
        try{
            return ResponseEntity.ok().body(jwtService.newToken(jwtTokenDto));
        }catch (IllegalArgumentException e){
            log.info("refreshtoken 다름 : "+ e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("토큰 다름");
        }catch (Exception e){
            log.info("처리되지 않은 에러 발생 : "+ e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
