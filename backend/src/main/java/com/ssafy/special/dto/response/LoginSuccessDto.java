package com.ssafy.special.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class LoginSuccessDto {
    private Long memberSeq;
    private String nickname;
    private String email;
    private Date expAccessToken;
    private List<TypeRateDto> typeRates;
    private List<RecentFoodDto> recentFoods;

    @Builder
    public LoginSuccessDto(Long memberSeq, String nickname, String email, Date expAccessToken, List<TypeRateDto> typeRates, List<RecentFoodDto> recentFoods) {
        this.memberSeq = memberSeq;
        this.nickname = nickname;
        this.email = email;
        this.expAccessToken = expAccessToken;
        this.typeRates = typeRates;
        this.recentFoods = recentFoods;
    }
}
