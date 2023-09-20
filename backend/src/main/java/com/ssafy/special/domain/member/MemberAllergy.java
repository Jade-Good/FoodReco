package com.ssafy.special.domain.member;


import com.ssafy.special.domain.etc.Allergy;
import com.ssafy.special.domain.food.Food;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@Setter
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

    // food_seq
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="allergy_seq")
    private Allergy allergy;

    //is_deleted
    @Column(name = "is_deleted",columnDefinition = "tinyint default 0") // 컬럼 정의를 설정
    private int isDeleted;

    //created_at
    @NotNull
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // deleted_at
    @UpdateTimestamp
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public MemberAllergy(Long memberAllergySeq, Member member, Allergy allergy, int isDeleted, LocalDateTime createdAt, LocalDateTime deletedAt) {
        this.memberAllergySeq = memberAllergySeq;
        this.member = member;
        this.allergy = allergy;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }
}
