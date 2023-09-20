package com.ssafy.special.domain.etc;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity(name="allergy")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Allergy {
    // allergy_seq, PK값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allergy_seq",columnDefinition = "tinyint")
    private int allergySeq;

    // allergy_name
    @NotNull
    @Column(name = "allergy_name", length = 10)
    private String allergyName;

    @Builder
    public Allergy(int allergySeq, String allergyName) {
        this.allergySeq = allergySeq;
        this.allergyName = allergyName;
    }
}
