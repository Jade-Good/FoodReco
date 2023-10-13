package com.ssafy.special.dto.request;

import com.ssafy.special.domain.member.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
public class CrewSignUpDto {
    private String crewName;
    private MultipartFile crewImg;
    private String crewMembers;

    @Builder
    public CrewSignUpDto(String crewName, MultipartFile crewImg, String crewMembers) {
        this.crewName = crewName;
        this.crewImg = crewImg;
        this.crewMembers = crewMembers;
    }
}
