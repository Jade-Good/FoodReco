package com.ssafy.special.dto.request;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
public class UserTasteDto {

    private String foodUrl;
    private Long foodSeq;
    private String foodName;

    @Builder
    public UserTasteDto(String foodUrl, Long foodSeq, String foodName) {
        this.foodUrl = foodUrl;
        this.foodSeq = foodSeq;
        this.foodName = foodName;
    }
}
