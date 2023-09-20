package com.ssafy.special.domain.member;


import com.ssafy.special.domain.crew.CrewMember;
import com.ssafy.special.domain.etc.FriendList;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @Builder
    public Member(Long memberSeq, String email, String password, String nickname, String tendency, int height, int weight, String sex, int isDeleted, LocalDateTime createdAt, LocalDateTime lastModifiedAt, String img, List<CrewMember> crewMembers, List<FriendList> friendLists) {
        this.memberSeq = memberSeq;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.tendency = tendency;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.img = img;
        this.crewMembers = crewMembers;
        this.friendLists = friendLists;
    }
}
