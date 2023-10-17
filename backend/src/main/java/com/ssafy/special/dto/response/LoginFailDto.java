package com.ssafy.special.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginFailDto {
    private int status;
    private String message;

    @Builder
    public LoginFailDto(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
