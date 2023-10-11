package com.ssafy.special.domain.etc;


import com.ssafy.special.domain.member.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Entity(name="alarm")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm {

    // alarm_seq, PK값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_seq")
    private Long alarmSeq;

    // member_seq
    @ManyToOne(fetch = FetchType.LAZY)
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

    @Builder
    public Alarm(Long alarmSeq, Member member, String content, String type, int status, LocalDateTime checkAt, LocalDateTime createdAt, LocalDateTime deletedAt) {
        this.alarmSeq = alarmSeq;
        this.member = member;
        this.content = content;
        this.type = type;
        this.status = status;
        this.checkAt = checkAt;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }
}
