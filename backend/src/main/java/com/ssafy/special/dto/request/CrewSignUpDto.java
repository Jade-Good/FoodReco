package com.ssafy.special.dto.request;

import com.ssafy.special.domain.member.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CrewSignUpDto {
    private String crewName;
    private String crewImg;
    private List<Long> crewMembers;

    @Builder
    public CrewSignUpDto(String crewName, String crewImg, List<Long> crewMembers) {
        this.crewName = crewName;
        this.crewImg = crewImg;
        this.crewMembers = crewMembers;
    }
}
