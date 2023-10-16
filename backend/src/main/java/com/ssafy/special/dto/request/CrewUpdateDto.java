package com.ssafy.special.dto.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CrewUpdateDto {
    private Long crewSeq;
    private String name;
    private MultipartFile img;

    @Builder
    public CrewUpdateDto(Long crewSeq, String name, MultipartFile img) {
        this.crewSeq = crewSeq;
        this.name = name;
        this.img = img;
    }
}
