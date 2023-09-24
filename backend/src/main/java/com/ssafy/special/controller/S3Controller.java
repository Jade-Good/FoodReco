package com.ssafy.special.controller;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.ssafy.special.service.food.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {

    private final FoodService foodService;

    @PostMapping("/food/img")
    public ResponseEntity<?> uploadImg(@RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        try {

            foodService.uploadImg(file);

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
