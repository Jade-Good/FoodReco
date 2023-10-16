package com.ssafy.special.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class WeatherStatus {

    private String status;

}
