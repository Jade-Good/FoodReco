package com.ssafy.special.dto.request;

import lombok.*;

import java.util.Optional;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//4가지 버튼 클릭시 받을 Dto
public class FeedbackDto {
    private Long foodSeq;
    private int feedback;

    @Builder
    public FeedbackDto(Long foodSeq, int feedback) {
        this.foodSeq = foodSeq;
        this.feedback = feedback;
    }
}
