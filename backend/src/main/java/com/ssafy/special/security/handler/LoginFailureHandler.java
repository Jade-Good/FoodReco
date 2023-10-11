package com.ssafy.special.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.special.dto.response.LoginFailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 로그인 실패 시 처리하는 핸들러
 * SimpleUrlAuthenticationFailureHandler를 상속받아서 구현
 */
@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        LoginFailDto loginFailDto = LoginFailDto.builder()
                .status(404)
                .message("로그인 실패! 이메일이나 비밀번호를 확인해주세요.")
                .build();
        // 객체를 JSON으로 변환
        response.getWriter().write(new ObjectMapper().writeValueAsString(loginFailDto));
        log.info("로그인에 실패했습니다. 메시지 : {}", exception.getMessage());
    }
}