package com.ssafy.special.domain.etc;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity(name="allergy")
@NoArgsConstructor
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
}
