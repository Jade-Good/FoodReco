package com.ssafy.special.domain.crew;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ssafy.special.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@Entity(name = "crew_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(CrewId.class)
public class CrewMember {
    // 사용자 seq
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "member_seq")
    @JsonBackReference
    private Member member;

    // 그룹 seq
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "crew_seq")
    @JsonBackReference
    private Crew crew;

    //status
    @NotNull
    @Column(name = "status", columnDefinition = "tinyint default 0")
    private int status;

    //check_vote
    @NotNull
    @Column(name = "check_vote", columnDefinition = "tinyint default 0")
    private int checkVote;

    // Builder 클래스 정의
    @Builder
    public CrewMember(Member member, Crew crew, int status, int checkVote) {
        this.member = member;
        this.crew = crew;
        this.status = status;
        this.checkVote = checkVote;
    }
}
