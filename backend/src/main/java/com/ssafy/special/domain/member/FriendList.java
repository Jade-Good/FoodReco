package com.ssafy.special.domain.member;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ssafy.special.domain.member.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "friend_list")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendList {
    // 친구 목록 seq
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_seq")
    private Long friendSeq;

    // one_seq
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="one_seq")
    private Member one;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="other_seq")
    private Member other;

    //created_at
    @NotNull
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    //deleted_at
    @UpdateTimestamp
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public FriendList(Long friendSeq, Member one, Member other, LocalDateTime createdAt, LocalDateTime deletedAt) {
        this.friendSeq = friendSeq;
        this.one = one;
        this.other = other;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }
}
