package com.learnershigh.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// 토큰 정보를 저장할 Dto
public class TokenDto {
    // JWT 대한 인증 타입
    private String accessToken;
    private String refreshToken;
}
