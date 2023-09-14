package com.ssafy.special.domain.etc;


import com.ssafy.special.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name="alarm")
@NoArgsConstructor
public class Alarm {

    // alarm_seq, PK값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_seq")
    private Long alarmSeq;

    // member_seq
    @ManyToOne
    @JoinColumn(name="member_seq")
    private Member member;

    // content
    @NotNull
    @Column(name = "content", length = 50)
    private String content;

    //type
    @NotNull
    @Column(name = "type", length = 6)
    private String type;

    //status
    @NotNull
    @Column(name = "status", columnDefinition = "tinyint default 0")
    private int status;

    //check_at
    @Column(name = "check_at")
    private LocalDateTime checkAt;

    //created_at
    @NotNull
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    //deleted_at
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
