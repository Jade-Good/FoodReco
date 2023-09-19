package com.ssafy.special.domain.member;


import com.ssafy.special.domain.crew.CrewMember;
import com.ssafy.special.domain.etc.FriendList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity(name = "member")
@NoArgsConstructor
public class Member {

    // member_seq, PK값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_seq")
    private Long memberSeq;

    // email
    @NotNull
    @Column(name = "email", length = 100)
    private String email;

    // password
    @NotNull
    @Column(name = "password", length = 255)
    private String password;

    //nickname
    @NotNull
    @Column(name = "nickname", length = 18)
    private String nickname;

    //tendency
    @Column(name = "tendency", length = 18)
    private String tendency;

    //height
    @Column(name = "height", columnDefinition = "smallint")
    private int height;

    //weight
    @Column(name = "weight", columnDefinition = "smallint")
    private int weight;

    //sex
    @Column(name = "sex", length = 3)
    private String sex;

    //is_deleted
    @NotNull
    @Column(name = "is_deleted",columnDefinition = "tinyint default 0") // 컬럼 정의를 설정
    private int isDeleted;

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

    // img
    @Column(length = 50, name = "img")
    private String img;

    // 자신이 속한 crew list
    @OneToMany(mappedBy = "crew", fetch = FetchType.LAZY)
    private List<CrewMember> crewMembers = new ArrayList<>();

    // 친구 리스트
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY) // member 필드와 연관 관계 설정
    private List<FriendList> friendLists = new ArrayList<>();
}
