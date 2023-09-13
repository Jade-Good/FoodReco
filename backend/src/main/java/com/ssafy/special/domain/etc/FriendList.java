package com.ssafy.special.domain.etc;


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
@NoArgsConstructor
@IdClass(FriendList.FriendListId.class)
public class FriendList {
    // 사용자 seq
    @Id
    @ManyToOne
    @JoinColumn(name= "member_seq",insertable = false, updatable = false)
    @JsonBackReference
    private Member member;

    // 친구 seq
    @Id
    @ManyToOne
    @JoinColumn(name= "member_seq")
    @JsonBackReference
    private Member friend;

    //friend_status
    @NotNull
    @Column(columnDefinition = "tinyint") // 컬럼 정의를 설정
    private int friendStatus;

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


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class FriendListId implements Serializable {
        private Long member;
        private Long friend;
    }



}
