package com.ssafy.special.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
public class FcmMessageDto {
    private Long targetSeq;
    private String title;
    private String body;
    private String image;
    private Map<String, String> data;

    @Builder

    public FcmMessageDto(Long targetSeq, String title, String body, String image, Map<String, String> data) {
        this.targetSeq = targetSeq;
        this.title = title;
        this.body = body;
        this.image = image;
        this.data = data;
    }
}