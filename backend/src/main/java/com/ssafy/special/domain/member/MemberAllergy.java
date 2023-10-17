package com.ssafy.special.domain.member;


import com.ssafy.special.domain.food.Ingredient;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@Entity(name = "member_allergy")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAllergy {

    // member_allergy_seq, PK값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_allergy_seq")
    private Long memberAllergySeq;

    // member_seq
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_seq")
    private Member member;

    // ingredient_seq
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ingredient_seq")
    private Ingredient ingredient;

    //created_at
    @NotNull
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public MemberAllergy(Long memberAllergySeq, Member member, Ingredient ingredient, LocalDateTime createdAt) {
        this.memberAllergySeq = memberAllergySeq;
        this.member = member;
        this.ingredient = ingredient;
        this.createdAt = createdAt;
    }
}
