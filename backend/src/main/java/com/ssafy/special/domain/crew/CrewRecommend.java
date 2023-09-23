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
    // crew_recommend_seq, PK값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_recommend_seq")
    private Long crewRecommendSeq;

    // crew_seq
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="crew_seq")
    private Crew crew;

    //recommend_at
    @NotNull
    @CreationTimestamp
    @Column(name = "recommend_at")
    private LocalDateTime recommendAt;

    @Builder
    public CrewRecommend(Long crewRecommendSeq, Crew crew, LocalDateTime recommendAt) {
        this.crewRecommendSeq = crewRecommendSeq;
        this.crew = crew;
        this.recommendAt = recommendAt;
    }
}
