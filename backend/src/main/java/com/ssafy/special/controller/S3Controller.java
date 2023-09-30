
package com.ssafy.special.controller;


import com.ssafy.special.service.food.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {

    private final FoodService foodService;

    @PostMapping("/food/img")
    public ResponseEntity<?> uploadImg(@RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        try {

            foodService.uploadImg("jjhjjh1159@gmail.com",file);

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






}
