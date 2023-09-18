package com.ssafy.special.domain.member;


import com.ssafy.special.domain.etc.Allergy;
import com.ssafy.special.domain.food.Food;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity(name = "member_allergy")
@NoArgsConstructor
public class MemberAllergy {

    // member_allergy_seq, PK값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_allergy_seq")
    private Long memberAllergySeq;

    // member_seq
    @ManyToOne
    @JoinColumn(name="member_seq")
    private Member member;

    // food_seq
    @ManyToOne
    @JoinColumn(name="allergy_seq")
    private Allergy allergy;

    //is_deleted
    @NotNull
    @Column(name = "is_deleted",columnDefinition = "tinyint default 0") // 컬럼 정의를 설정
    private boolean isDeleted;

    //created_at
    @NotNull
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // deleted_at
    @UpdateTimestamp
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
