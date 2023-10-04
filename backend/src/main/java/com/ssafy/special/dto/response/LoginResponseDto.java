package com.ssafy.special.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponseDto {
    private int status;
    private String message;

    @Builder
    public LoginResponseDto(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
