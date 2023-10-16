package com.ssafy.special.controller;



import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.special.dto.request.WeatherRequestDto;
import com.ssafy.special.dto.response.WeatherStatus;
import com.ssafy.special.service.etc.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WeatherController {


    private final WeatherStatus weatherStatus;
    private final WeatherService weatherService;

    @Value("${weather.service-key}")
    private String serviceKey;

    @GetMapping("/weather")
    public void getWeather(@RequestBody WeatherRequestDto weatherRequestDto) throws Exception {

        weatherService.getWeather(weatherRequestDto.getLon(),weatherRequestDto.getLet());



    }


}

