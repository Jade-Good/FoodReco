package com.ssafy.special.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleTokenDto {
    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String scope;
    private String token_type;

    @Builder
    public GoogleTokenDto(String access_token, int expires_in, String refresh_token, String scope, String token_type) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
        this.scope = scope;
        this.token_type = token_type;
    }
}
