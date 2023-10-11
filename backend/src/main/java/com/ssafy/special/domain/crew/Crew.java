package com.ssafy.special.domain.crew;


import com.ssafy.special.domain.food.Ingredient;
import com.ssafy.special.domain.member.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity(name = "crew")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Crew {
    // crew_seq, PK값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_seq")
    private Long crewSeq;

    //crew_name
    @NotNull
    @Column(name = "crew_name",length = 30)
    private String name;

    //img
    @NotNull
    @Column(name = "img",length = 50)
    private String img;

    //status
    @NotNull
    @Column(name = "status",length = 9)
    private String status;

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

    // 자신이 속한 crew member list
    @OneToMany(mappedBy = "crew")
    private List<CrewMember> crewMembers;

    @Builder
    public Crew(Long crewSeq, String name, String img, String status, LocalDateTime createdAt, LocalDateTime lastModifiedAt, List<CrewMember> crewMembers) {
        this.crewSeq = crewSeq;
        this.name = name;
        this.img = img;
        this.status = status;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.crewMembers = crewMembers;
    }
}
