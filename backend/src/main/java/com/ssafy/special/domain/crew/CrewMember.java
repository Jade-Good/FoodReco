package com.ssafy.special.domain.crew;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ssafy.special.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity(name = "crew_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(CrewMember.CrewId.class)
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class CrewId implements Serializable {
        private Long member;
        private Long crew;
    }

    // Builder 클래스 정의
    @Builder
    public CrewMember(Member member, Crew crew) {
        this.member = member;
        this.crew = crew;
    }
}
