package com.ssafy.special.service.member;

import com.ssafy.special.domain.member.FriendList;
import com.ssafy.special.domain.member.Member;
import com.ssafy.special.dto.request.UserSignUpDto;
import com.ssafy.special.exception.DuplicateEmailException;
import com.ssafy.special.exception.DuplicateNicknameException;
import com.ssafy.special.exception.SignupFailedException;
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
    public void checkEmail(String memberEmail) throws DuplicateEmailException {
        if (memberRepository.findByEmail(memberEmail).isPresent()) {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다.");
        }
    }

    // 닉네임 중복 검사
    public void checkNickname(String nickName) throws Exception {
        try {
            if (memberRepository.findByNickname(nickName).isPresent()) {
                throw new DuplicateNicknameException("이미 존재하는 닉네임 입니다.");
            }
        } catch (Exception e) {
            throw new Exception("서버 에러");
        }
    }

    /*
     * sign up 유효성 체크 후 DB에 저장
     */

    public void signUp(UserSignUpDto userSignUpDto) throws DuplicateEmailException, DuplicateNicknameException, SignupFailedException {

        try {
            if (memberRepository.findByEmail(userSignUpDto.getEmail()).isPresent()) {
                throw new DuplicateEmailException("이미 존재하는 이메일입니다.");
            }
            if (memberRepository.findByNickname(userSignUpDto.getNickname()).isPresent()) {
                throw new DuplicateNicknameException("이미 존재하는 닉네임입니다.");
            }

            Member member = Member.builder()
                    .email(userSignUpDto.getEmail())
                    .password(userSignUpDto.getPassword())
                    .nickname(userSignUpDto.getNickname())
//                    .tendency(userSignUpDto.getTendency())
//                    .activity(userSignUpDto.getActivity())
                    .sex(userSignUpDto.getSex())
                    .isDeleted(0)
                    .createdAt(LocalDateTime.now())
                    .lastModifiedAt(LocalDateTime.now())
                    .build();
            member.passwordEncode(passwordEncoder);
            log.info(member.getPassword());
            memberRepository.save(member);
        } catch (SignupFailedException e) {
            throw new SignupFailedException("회원가입 실패");
        }
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
