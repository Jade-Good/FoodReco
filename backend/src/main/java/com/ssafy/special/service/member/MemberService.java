package com.ssafy.special.service.member;

import com.ssafy.special.domain.member.FriendList;
import com.ssafy.special.domain.member.Member;
import com.ssafy.special.dto.UserSignUpDto;
import com.ssafy.special.repository.member.FriendListRepository;
import com.ssafy.special.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final FriendListRepository friendListRepository;
    private final PasswordEncoder passwordEncoder;

    // 아이디 중복 값 검사
    public boolean duplicateEmail(String memberEmail) throws Exception {
        if (memberRepository.findByEmail(memberEmail).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }
        return true;
    }

    /*
     * sign up 유효성 체크 후 DB에 저장
     */
    public String signUp(UserSignUpDto userSignUpDto) throws Exception {
        try {
            if (memberRepository.findByEmail(userSignUpDto.getEmail()).isPresent()) {
                return "이미 존재하는 이메일입니다.";
            }
            if (memberRepository.findByNickname(userSignUpDto.getNickname()).isPresent()) {
                return "이미 존재하는 닉네임입니다.";
            }

            Member member = Member.builder()
                    .email(userSignUpDto.getEmail())
                    .password(userSignUpDto.getPassword())
                    .nickname(userSignUpDto.getNickname())
//                    .activity(userSignUpDto.getActivity())
                    .sex(userSignUpDto.getSex())
                    .isDeleted(0)
                    .createdAt(LocalDateTime.now())
                    .lastModifiedAt(LocalDateTime.now())
                    .build();
            member.passwordEncode(passwordEncoder);
            log.info(member.getPassword());
            memberRepository.save(member);
        } catch(Exception e) {
            throw new Exception("회원가입 실패");
        }
        return "회원 가입 성공";
    }

    public List<Member> getFriendList(Long memberSeq) {
        // one 또는 other 필드가 memberId와 일치하는 친구 목록을 가져옴
        List<FriendList> friendLists = friendListRepository.findByOneMemberSeqOrOtherMemberSeq(memberSeq, memberSeq);

        // 친구 목록을 저장할 List<Member> 생성
        List<Member> friends = new ArrayList<>();

        // FriendList에서 one 필드에 해당하는 친구를 friends 목록에 추가
        for (FriendList friendList : friendLists) {
            if (friendList.getOne().getMemberSeq().equals(memberSeq)) {
                friends.add(friendList.getOther());
            } else {
                friends.add(friendList.getOne());
            }
        }
        return friends;
    }
}
