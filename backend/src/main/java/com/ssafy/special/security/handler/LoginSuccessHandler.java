package com.ssafy.special.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.special.domain.member.Member;
import com.ssafy.special.dto.response.LoginSuccessDto;
import com.ssafy.special.repository.member.MemberRepository;
import com.ssafy.special.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpiration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String email = extractUsername(authentication); // 인증 정보에서 Username(email) 추출
        String accessToken = jwtService.createAccessToken(email); // JwtService의 createAccessToken을 사용하여 AccessToken 발급
        String refreshToken = jwtService.createRefreshToken(); // JwtService의 createRefreshToken을 사용하여 RefreshToken 발급

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken); // 응답 헤더에 AccessToken, RefreshToken 실어서 응답

        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member != null){
            member.updateRefreshToken(refreshToken);
            memberRepository.saveAndFlush(member);
        }
        log.info("로그인에 성공하였습    니다. 이메일 : {}", email);
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
        log.info("발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);
    
        // 로그인 성공 시 제공할 데이터
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        LoginSuccessDto LoginSuccesDto = jwtService.successLogin(member,accessTokenExpiration);
        log.info(new ObjectMapper().writeValueAsString(LoginSuccesDto));
        response.getWriter().write(new ObjectMapper().writeValueAsString(LoginSuccesDto));
    }


    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}