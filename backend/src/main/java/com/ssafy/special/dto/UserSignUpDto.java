package com.ssafy.special.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserSignUpDto {
    private String email;
    private String password;
    private String nickname;
    private String img;
}