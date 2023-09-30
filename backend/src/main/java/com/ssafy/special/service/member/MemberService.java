
package com.ssafy.special.service.member;

import com.ssafy.special.domain.food.Food;
import com.ssafy.special.domain.member.FriendList;
import com.ssafy.special.domain.member.Member;
import com.ssafy.special.domain.member.MemberFoodPreference;
import com.ssafy.special.dto.request.UserTasteDto;
import com.ssafy.special.dto.request.UserInfoUpdateDto;
import com.ssafy.special.dto.request.UserSignUpDto;
import com.ssafy.special.dto.response.MemberDetailDto;
import com.ssafy.special.exception.DuplicateEmailException;
import com.ssafy.special.exception.DuplicateNicknameException;
import com.ssafy.special.exception.SignupFailedException;
import com.ssafy.special.repository.food.FoodRepository;
import com.ssafy.special.repository.member.FriendListRepository;
import com.ssafy.special.repository.member.MemberFoodPreferenceRepository;
import com.ssafy.special.repository.member.MemberRepository;
import com.ssafy.special.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final FriendListRepository friendListRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberFoodPreferenceRepository memberFoodPreferenceRepository;
    private final FoodRepository foodRepository;
    private final SecurityUtils securityUtils;


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
                    .age(userSignUpDto.getAge())
                    .sex(userSignUpDto.getSex())
                    .height(userSignUpDto.getHeight())
                    .weight(userSignUpDto.getWeight())
                    .activity(userSignUpDto.getActivity())
                    .isDeleted(0)
                    .createdAt(LocalDateTime.now())
                    .lastModifiedAt(LocalDateTime.now())
                    .build();
            member.passwordEncode(passwordEncoder);
            log.info(member.getPassword());
            memberRepository.save(member);

            for (String foodName : userSignUpDto.getFavoriteList()) {
                Food food = foodRepository.findByName(foodName);

                MemberFoodPreference memberFoodPreference = MemberFoodPreference.builder()
                        .food(food)
                        .member(member)
                        .preferenceType(0)
                        .build();

                memberFoodPreferenceRepository.save(memberFoodPreference);
            }

            for (String foodName : userSignUpDto.getHateList()) {
                Food food = foodRepository.findByName(foodName);

                MemberFoodPreference memberFoodPreference = MemberFoodPreference.builder()
                        .food(food)
                        .member(member)
                        .preferenceType(1)
                        .build();

                memberFoodPreferenceRepository.save(memberFoodPreference);
            }

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

    public MemberDetailDto getUserInfo(String email) throws NullPointerException {


        Optional<Member> member = memberRepository.findByEmail(email);

        if (member.isPresent()) {
            MemberDetailDto memberDetailDto = MemberDetailDto.builder()
//                    .profileUrl(member.get().getProfileUrl())
                    .nickName(member.get().getNickname())
                    .height(member.get().getHeight())
                    .weight(member.get().getWeight())
                    .activity(member.get().getActivity())
                    .build();

            return memberDetailDto;
        } else {
            throw new NullPointerException("멤버 정보가 없습니다.");
        }


    }

    public List<UserTasteDto> getUserPreference(String email, int type) {

        try {
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

            List<UserTasteDto> userFavoriteList = new ArrayList<>();

            log.info(String.valueOf(member.getFoodPreferences().size()));
            for (MemberFoodPreference preference : member.getFoodPreferences()) {

                if (preference.getPreferenceType() == type) {
                    UserTasteDto userFavoriteDto = UserTasteDto.builder()
                            .foodSeq(preference.getFood().getFoodSeq())
                            .foodUrl(preference.getFood().getImg())
                            .foodName(preference.getFood().getName())
                            .build();
                    userFavoriteList.add(userFavoriteDto);
                }
            }

            return userFavoriteList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void updateUserInfo(UserInfoUpdateDto userInfoUpdateDto) throws Exception {


        Member member = Member.builder()
                .nickname(userInfoUpdateDto.getNickname())
                .age(userInfoUpdateDto.getAge())
                .sex(userInfoUpdateDto.getSex())
                .height(userInfoUpdateDto.getHeight())
                .weight(userInfoUpdateDto.getWeight())
                .isDeleted(0)
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .build();
        memberRepository.save(member);


        // 총 걸음 수를 얻어낼 API 요청
//            String activity_category = userSignUpDto.getActivityCategory();
//            int activity_hour = userSignUpDto.getActivityHour();
//
//            int activity = API 요청 반환값
//      member.setActivity(activity);


    }
}
