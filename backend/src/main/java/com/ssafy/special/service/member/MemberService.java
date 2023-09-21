package com.ssafy.special.service.member;

import com.ssafy.special.domain.member.Member;
import com.ssafy.special.dto.UserSignUpDto;
import com.ssafy.special.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 아이디 중복 값 검사
    public boolean duplicateEmail(String memberEmail) throws Exception {
        if (memberRepository.findByEmail(memberEmail).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }
        return true;
    }

    public void signUp(UserSignUpDto userSignUpDto) throws Exception  {
        if (memberRepository.findByEmail(userSignUpDto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }
        if (memberRepository.findByNickname(userSignUpDto.getNickname()).isPresent()) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        Member member = Member.builder()
                              .email(userSignUpDto.getEmail())
                              .password(userSignUpDto.getPassword())
                              .nickname(userSignUpDto.getNickname())
                              .img(userSignUpDto.getImg())
                              .createdAt(LocalDateTime.now())
                              .lastModifiedAt(LocalDateTime.now())
                              .build();
        member.passwordEncode(passwordEncoder);
        memberRepository.save(member);
    }
}
