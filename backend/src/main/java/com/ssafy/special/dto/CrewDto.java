package com.ssafy.special.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrewDto {
    private Long crewSeq;
    private String name;
    private String img;
    // 현재 그룹에 들어간건지 안들어간 건지 상태를 나타내는 필드
    private String status;

    @Builder
    public CrewDto(Long crewSeq, String name, String img, String status) {
        this.crewSeq = crewSeq;
        this.name = name;
        this.img = img;
        this.status = status;
    }
}
