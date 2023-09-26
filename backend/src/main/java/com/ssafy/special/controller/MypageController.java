package com.ssafy.special.controller;

import com.ssafy.special.dto.response.MemberDetailDto;
import com.ssafy.special.service.member.MemberService;
import com.ssafy.special.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {


    private final MemberService memberService;
    private final SecurityUtils securityUtils;

    @GetMapping("/info")
    public ResponseEntity<Map<String,Object>> getUserInfo() {

        Map<String,Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        try {
            MemberDetailDto memberDetailDto = memberService.getUserInfo(securityUtils.getEmail());

            if (memberDetailDto != null) {
                resultMap.put("memberDetailDto",memberDetailDto);
                status = HttpStatus.OK;
            }
        } catch (NullPointerException e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            resultMap.put("message", e.getMessage());
        }
        return new ResponseEntity<>(resultMap,status);
    }


//    @GetMapping("/list/favorite")
//
//    @GetMapping("/list/hate")
//
//    @PatchMapping("/list/favorite")
//
//    @PatchMapping("/list/hate")
//
//    @PatchMapping("/list/info")
//
//    @GetMapping("/list/food")
//
//    @GetMapping("/list/food/search/{foodName}")

}
