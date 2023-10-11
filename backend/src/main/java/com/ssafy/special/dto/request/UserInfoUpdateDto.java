package com.ssafy.special.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class UserInfoUpdateDto {

    private MultipartFile img;
    private String nickname;
    private int height;
    private int weight;
    private int activity;

}
