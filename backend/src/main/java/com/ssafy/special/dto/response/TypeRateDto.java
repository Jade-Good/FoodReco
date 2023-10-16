package com.ssafy.special.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class TypeRateDto {
    private String type;
    private double rating;

    @Builder
    public TypeRateDto(String type, double rating) {
        this.type = type;
        this.rating = rating;
    }
}
