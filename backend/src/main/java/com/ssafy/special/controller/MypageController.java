
package com.ssafy.special.controller;

import com.ssafy.special.dto.request.UserTasteDto;
import com.ssafy.special.dto.request.UserInfoUpdateDto;
import com.ssafy.special.dto.response.MemberDetailDto;
import com.ssafy.special.service.food.FoodService;
import com.ssafy.special.service.member.MemberService;
import com.ssafy.special.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {


    private final MemberService memberService;
    private final SecurityUtils securityUtils;

    private final FoodService foodService;


    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getUserInfo() {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        try {
            MemberDetailDto memberDetailDto = memberService.getUserInfo(securityUtils.getEmail());

            if (memberDetailDto != null) {
                resultMap.put("memberDetailDto", memberDetailDto);
                status = HttpStatus.OK;
            }
        } catch (NullPointerException e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            resultMap.put("message", e.getMessage());
        }
        return new ResponseEntity<>(resultMap, status);
    }


    @GetMapping("/list/favorite")
    public ResponseEntity<Map<String, Object>> getUserFavorite() {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        String message = null;
        try {
            List<UserTasteDto> userFavoriteList = memberService.getUserPreference("jjhjjh1159@gmail.com", 0);

            message = "조회 성공";
            status = HttpStatus.OK;
            resultMap.put("userFavoriteList", userFavoriteList);
        } catch (Exception e) {
            message = "조회 실패";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        resultMap.put("message", message);

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/list/hate")
    public ResponseEntity<Map<String, Object>> getUserHate() {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        String message = null;
        try {
            List<UserTasteDto> userFavoriteList = memberService.getUserPreference("jjhjjh1159@gmail.com", 1);

            message = "조회 성공";
            status = HttpStatus.OK;
            resultMap.put("userFavoriteList", userFavoriteList);
        } catch (Exception e) {
            message = "조회 실패";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        resultMap.put("message", message);

        return new ResponseEntity<>(resultMap, status);
    }


    @PatchMapping("/info")
    public ResponseEntity<Map<String, String>> updateUserInfo(UserInfoUpdateDto userInfoUpdateDto) {

        Map<String, String> resultMap = new HashMap<>();
        HttpStatus status = null;
        String message = "";
        log.info("아라리오~~~~~~");
        try {
            foodService.uploadImg("jjhjjh1159@gmail.com", userInfoUpdateDto.getImg());

//            memberService.updateUserInfo(userInfoUpdateDto);
            status = HttpStatus.OK;
            message = "수정 완료";
        } catch (EntityNotFoundException e) {
            message = e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } catch (Exception e) {
            message = e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        resultMap.put("message", message);

        return new ResponseEntity<>(resultMap, status);
    }

}
	