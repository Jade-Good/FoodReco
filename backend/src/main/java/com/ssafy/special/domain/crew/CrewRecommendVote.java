package com.ssafy.special.domain.crew;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ssafy.special.domain.food.Food;
import com.ssafy.special.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity(name = "crew_recommend_vote")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(VoteId.class)
public class CrewRecommendVote {
    // 사용자 seq
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "crew_recommend_seq")
    @JsonBackReference
    private CrewRecommend crewRecommendSeq;

    // 음식 seq
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "food_seq")
    @JsonBackReference
    private Food food;

    // 사용자 seq
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "member_seq")
    @JsonBackReference
    private Member member;

    @Builder
    public CrewRecommendVote(CrewRecommend crewRecommendSeq, Food food, Member member) {
        this.crewRecommendSeq = crewRecommendSeq;
        this.food = food;
        this.member = member;
    }
}
