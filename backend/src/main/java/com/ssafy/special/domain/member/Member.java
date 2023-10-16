package com.ssafy.special.domain.member;


import com.ssafy.special.domain.crew.CrewMember;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    // member_seq, PK값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_seq")
    private Long memberSeq;

    // email
    @Column(name = "email", length = 100,unique = true)
    private String email;

    // password
    @Column(name = "password", length = 512)
    private String password;

    @Column(name = "age", columnDefinition = "tinyint")
    private int age;

    //nickname
    @NotNull
    @Column(name = "nickname", length = 18,unique = true)
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


    //activity
    @Column(name = "activity", columnDefinition = "smallint default 0")
    private int activity;

    //sex
    @Column(name = "sex", length = 3)
    private String sex;

    // img
    @Column(length = 50, name = "img")
    private String img;

    // fcm_token
    @Column(length = 180, name = "fcm_token")
    private String fcmToken;

    // refresh_token
    @Column(length = 512, name = "jwt_refresh_token")
    private String jwtRefreshToken;

    // google_refresh_token
    @Column(length = 150, name = "google_refresh_token")
    private String googleRefreshToken;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<MemberFoodPreference> foodPreferences = new ArrayList<>();

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


    // 자신이 속한 crew list
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<CrewMember> crewMembers = new ArrayList<>();

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String jwtRefreshToken){
        this.jwtRefreshToken = jwtRefreshToken;
    }
    @Builder

    public Member(Long memberSeq, String email, String password, int age, String nickname, String tendency, int height, int weight, int activity, String sex, String img, String fcmToken, String jwtRefreshToken, String googleRefreshToken, List<MemberFoodPreference> foodPreferences, int isDeleted, LocalDateTime createdAt, LocalDateTime lastModifiedAt, List<CrewMember> crewMembers) {
        this.memberSeq = memberSeq;
        this.email = email;
        this.password = password;
        this.age = age;
        this.nickname = nickname;
        this.tendency = tendency;
        this.height = height;
        this.weight = weight;
        this.activity = activity;
        this.sex = sex;
        this.img = img;
        this.fcmToken = fcmToken;
        this.jwtRefreshToken = jwtRefreshToken;
        this.googleRefreshToken = googleRefreshToken;
        this.foodPreferences = foodPreferences;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.crewMembers = crewMembers;
    }
}
