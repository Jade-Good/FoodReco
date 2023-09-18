package com.ssafy.special.domain.crew;


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
@Entity(name = "crew_recommend")
@NoArgsConstructor
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
}
