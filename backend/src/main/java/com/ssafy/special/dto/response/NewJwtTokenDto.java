package com.ssafy.special.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewJwtTokenDto {
    private String authorization;
    private String authorizationRefresh;
    @Builder
    public NewJwtTokenDto(String authorization, String authorizationRefresh) {
        this.authorization = authorization;
        this.authorizationRefresh = authorizationRefresh;
    }
}
