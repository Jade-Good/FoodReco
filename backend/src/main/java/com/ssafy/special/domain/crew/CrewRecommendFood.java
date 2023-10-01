package com.ssafy.special.domain.crew;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ssafy.special.domain.food.Food;
import com.ssafy.special.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@Data
@Entity(name = "crew_recommend_Food")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrewRecommendFood {
    // 사용자 seq
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_recommend_food_seq")
    private Long crewRecommendFoodSeq;

    // 음식 seq
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "food_seq")
    private Food food;
    
    // 그룹 추천 seq
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "crew_recommend_seq")
    private CrewRecommend crewRecommend;

    @Builder
    public CrewRecommendFood(Long crewRecommendFoodSeq, Food food, CrewRecommend crewRecommend) {
        this.crewRecommendFoodSeq = crewRecommendFoodSeq;
        this.food = food;
        this.crewRecommend = crewRecommend;
    }
}
