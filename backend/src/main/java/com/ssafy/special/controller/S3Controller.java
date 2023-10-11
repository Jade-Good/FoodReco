
package com.ssafy.special.controller;


import com.ssafy.special.service.food.FoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {

    private final FoodService foodService;

    @PostMapping("/food/img")
    public ResponseEntity<?> uploadImg(@RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        try {

            foodService.uploadImg(getEmail(),file);

            resultMap.put("message", "업로드 성공");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);

        } catch(Exception e) {

            resultMap.put("message", "업로드 실패");
            return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping("/food/img")
    public ResponseEntity<byte[]> downloadImg(String fileName) {

        try {

            return foodService.downloadImg(fileName);

        }catch (Exception e) {

            return new ResponseEntity<>(null,null,HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping("/foods/{foodSeq}/img")
    public ResponseEntity<byte[]> getFoodImg(@PathVariable int foodSeq) {

        log.info(String.valueOf(foodSeq));
        try {
            return foodService.getFoodImg(foodSeq);
        } catch (Exception e) {
            log.info(e.getMessage());
           return new ResponseEntity<>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    // 사용자 Email 가져오는 Email
    public String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }





}
