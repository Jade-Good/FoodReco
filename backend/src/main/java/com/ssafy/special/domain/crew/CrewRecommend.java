package com.ssafy.special.domain.crew;


import com.ssafy.special.domain.food.Food;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Entity(name = "crew_recommend")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrewRecommend {
    // crew_recommand_seq, PK값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_recommand_seq")
    private Long crewRecommendSeq;

    // crew_seq
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="crew_seq")
    private Crew crew;

    // food_seq
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="food_seq")
    private Food food;

    //recommend_at
    @NotNull
    @CreationTimestamp
    @Column(name = "recommend_at")
    private LocalDateTime recommendAt;

    @Builder
    public CrewRecommend(Long crewRecommendSeq, Crew crew, Food food, LocalDateTime recommendAt) {
        this.crewRecommendSeq = crewRecommendSeq;
        this.crew = crew;
        this.food = food;
        this.recommendAt = recommendAt;
    }
}
