
package com.ssafy.special.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class UserSignUpDto {
    private String email;
    private String password;
    private String nickname;
    private int age;
    private String sex;
    private int height;
    private int weight;
    private int activity;
    private List<String> favoriteList;
    private List<String> hateList;
    private List<String> allergyList;
}