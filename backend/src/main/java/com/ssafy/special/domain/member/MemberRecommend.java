package com.ssafy.special.domain.member;


import com.ssafy.special.domain.food.Food;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Entity(name = "member_recommend")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRecommend {

    // member_recommend_seq, PK값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_recommend_seq")
    private Long memberRecommendSeq;

    // member_seq
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_seq")
    private Member member;

    // food_seq
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="food_seq")
    private Food food;

    //weather
    @NotNull
    @Column(name = "weather", length = 6)
    private String weather;

    //food_rating
    @Column(name = "food_rating",columnDefinition = "tinyint")
    private int foodRating;

    //recommend_at
    @NotNull
    @CreationTimestamp
    @Column(name = "recommend_at")
    private LocalDateTime recommendAt;

    //activity_calorie
    @Column(name = "activity_calorie",columnDefinition = "smallint")
    private int activityCalorie;

    @Builder
    public MemberRecommend(Long memberRecommendSeq, Member member, Food food, String weather, int foodRating, LocalDateTime recommendAt, int activityCalorie) {
        this.memberRecommendSeq = memberRecommendSeq;
        this.member = member;
        this.food = food;
        this.weather = weather;
        this.foodRating = foodRating;
        this.recommendAt = recommendAt;
        this.activityCalorie = activityCalorie;
    }
}
