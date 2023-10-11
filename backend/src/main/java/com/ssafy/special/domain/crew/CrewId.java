package com.ssafy.special.domain.crew;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CrewId implements Serializable {
    private Long member;
    private Long crew;

    @Builder
    public CrewId(Long member, Long crew) {
        this.member = member;
        this.crew = crew;
    }
}
