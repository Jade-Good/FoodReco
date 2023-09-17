package com.ssafy.special.domain.crew;


import com.ssafy.special.domain.food.Ingredient;
import com.ssafy.special.domain.member.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "crew")
@NoArgsConstructor
public class Crew {
    // crew_seq, PK값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_seq")
    private Long crewSeq;

    //crew_name
    @NotNull
    @Column(name = "crew_name",length = 30) // 컬럼 정의를 설정
    private String crewName;

    //created_at
    @NotNull
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    //last_modified_at
    @NotNull
    @UpdateTimestamp
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "crew_member",
            joinColumns = @JoinColumn(name = "crew_seq"),
            inverseJoinColumns = @JoinColumn(name = "member_seq"))
    private List<Member> members = new ArrayList<>();
}
