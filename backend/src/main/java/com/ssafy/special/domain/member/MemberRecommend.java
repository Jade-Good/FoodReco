package com.ssafy.special.domain.member;


import com.ssafy.special.domain.food.Food;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "member_recommend")
@NoArgsConstructor
public class MemberRecommend {

    // member_recommend_seq, PK값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_recommend_seq")
    private Long memberRecommendSeq;

    // member_seq
    @ManyToOne
    @JoinColumn(name="member_seq")
    private Member member;

    // food_seq
    @ManyToOne
    @JoinColumn(name="food_seq")
    private Food food;

    // situation_type
    @NotNull
    @Column(name = "situation_type", length = 6)
    private String situationType;

    //weather
    @NotNull
    @Column(name = "weather", length = 6)
    private String weather;

    //is_situation
    @NotNull
    @Column(name = "is_situation",columnDefinition = "tinyint")
    private int isSituation;

    //food_rating
    @NotNull
    @Column(name = "food_rating",columnDefinition = "tinyint")
    private int foodRating;

    //recommend_at
    @NotNull
    @CreationTimestamp
    @Column(name = "recommend_at")
    private LocalDateTime recommendAt;

}
