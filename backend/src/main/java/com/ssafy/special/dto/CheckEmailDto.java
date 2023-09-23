package com.ssafy.special.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
public class CheckEmailDto {

    private String email;
    private String code;
}
