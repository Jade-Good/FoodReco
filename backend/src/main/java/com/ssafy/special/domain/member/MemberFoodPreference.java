package com.ssafy.special.domain.member;


import com.ssafy.special.domain.food.Food;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Entity(name = "member_food_preference")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberFoodPreference {

    // preference_seq, PK값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preference_seq")
    private Long memberRecommendSeq;

    // member_seq
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_seq")
    private Member member;

    // food_seq
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="food_seq")
    private Food food;

    //preference_type
    @NotNull
    @Column(name = "preference_type", columnDefinition = "tinyint")
    private int preferenceType;

    @Builder
    public MemberFoodPreference(Long memberRecommendSeq, Member member, Food food, int preferenceType) {
        this.memberRecommendSeq = memberRecommendSeq;
        this.member = member;
        this.food = food;
        this.preferenceType = preferenceType;
    }
}
