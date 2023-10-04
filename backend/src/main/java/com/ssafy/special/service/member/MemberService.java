
package com.ssafy.special.service.member;

import com.ssafy.special.domain.food.Food;
import com.ssafy.special.domain.food.Ingredient;
import com.ssafy.special.domain.member.*;
import com.ssafy.special.dto.request.UserTasteDto;
import com.ssafy.special.dto.request.UserInfoUpdateDto;
import com.ssafy.special.dto.request.UserSignUpDto;
import com.ssafy.special.dto.response.*;
import com.ssafy.special.exception.DuplicateEmailException;
import com.ssafy.special.exception.DuplicateNicknameException;
import com.ssafy.special.exception.SignupFailedException;
import com.ssafy.special.repository.food.FoodRepository;
import com.ssafy.special.repository.food.IngredientRepository;
import com.ssafy.special.repository.member.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberRecommendRepository memberRecommendRepository;
    private final FriendListRepository friendListRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberFoodPreferenceRepository memberFoodPreferenceRepository;
    private final FoodRepository foodRepository;
    private final IngredientRepository ingredientRepository;
    private final MemberAllergyRepository memberAllergyRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;


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


            for(String allergyName : userSignUpDto.getAllergyList()) {
                Ingredient ingredient = ingredientRepository.findByName(allergyName);
                MemberAllergy memberAllergy = MemberAllergy.builder()
                        .member(member)
                        .ingredient(ingredient)
                        .createdAt(LocalDateTime.now())
                        .build();

                memberAllergyRepository.save(memberAllergy);
            }

        } catch (SignupFailedException e) {
            throw new SignupFailedException("회원가입 실패");
        }
    }

    public FriendListDto getFriendList(String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        // one 또는 other 필드가 memberId와 일치하는 친구 목록을 가져옴
        List<FriendList> friendLists = friendListRepository.findByOneMemberSeqOrOtherMemberSeq(member.getMemberSeq(), member.getMemberSeq());

        // 친구 목록을 저장할 List<Member> 생성
        List<MemberInfoDto> friends = new ArrayList<>();

        // FriendList에서 one 필드에 해당하는 친구를 friends 목록에 추가
        for (FriendList friendList : friendLists) {
            if (friendList.getOne().getMemberSeq().equals(member.getMemberSeq())) {
                Member friend = friendList.getOther();
                friends.add(MemberInfoDto.builder()
                        .memberSeq(friend.getMemberSeq())
                        .memberNickname(friend.getNickname())
                        .memberImg("https://" + bucket + ".s3." + region + ".amazonaws.com/" + friend.getImg())
                        .memberEmail(friend.getEmail())
                        .build());
            } else {
                Member friend = friendList.getOne();
                friends.add(MemberInfoDto.builder()
                        .memberSeq(friend.getMemberSeq())
                        .memberNickname(friend.getNickname())
                        .memberImg("https://" + bucket + ".s3." + region + ".amazonaws.com/" + friend.getImg())
                        .memberEmail(friend.getEmail())
                        .build());
            }
        }

        return FriendListDto.builder()
                .memberEmail(memberEmail)
                .friendList(friends)
                .build();
    }

    public MemberDetailDto getUserInfo(String email) throws NullPointerException {
        Optional<Member> member = memberRepository.findByEmail(email);

        if (member.isPresent()) {
            MemberDetailDto.MemberDetailDtoBuilder memberDetailDtoBuilder = MemberDetailDto.builder()
                    .nickname(member.get().getNickname())
                    .height(member.get().getHeight())
                    .weight(member.get().getWeight())
                    .activity(member.get().getActivity());

            if(member.get().getImg() != null) {
                memberDetailDtoBuilder.profileUrl("https://" + bucket + ".s3." + region + ".amazonaws.com/" + member.get().getImg());
            }

            MemberDetailDto memberDetailDto = memberDetailDtoBuilder.build();
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
                            .foodUrl("https://" + bucket + ".s3." + region + ".amazonaws.com/" + preference.getFood().getImg())
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

    @Transactional
    public void updateUserInfo(String email, UserInfoUpdateDto userInfoUpdateDto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        member.setNickname(userInfoUpdateDto.getNickname());
        member.setHeight(userInfoUpdateDto.getHeight());
        member.setWeight(userInfoUpdateDto.getWeight());
        member.setActivity(userInfoUpdateDto.getActivity());
    }

    @Transactional
    public void addFriend(String memberEmail, Long friendSeq){
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        Member friend = memberRepository.findByMemberSeq(friendSeq)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 친구 정보입니다."));

        FriendList friendList = FriendList.builder()
                .one(member)
                .other(friend)
                .createdAt(LocalDateTime.now())
                .deletedAt(LocalDateTime.now())
                .build();

        friendListRepository.save(friendList);
    }

    public FriendListDto getFriendListByNickname(String memberEmail,String searchKeyword) {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        // one 또는 other 필드가 memberId와 일치하는 친구 목록을 가져옴
        List<FriendList> friendLists = friendListRepository.findByOneMemberSeqOrOtherMemberSeq(member.getMemberSeq(), member.getMemberSeq());

        // 친구 목록을 저장할 List<Member> 생성
        List<MemberInfoDto> friends = new ArrayList<>();

        // FriendList에서 one 필드에 해당하는 친구를 friends 목록에 추가
        for (FriendList friendList : friendLists) {
            if (friendList.getOne().getMemberSeq().equals(member.getMemberSeq())) {
                Member friend = friendList.getOther();
                log.info("nickname : "+friend.getNickname() + " / search : "+searchKeyword);
                if(!friend.getNickname().contains(searchKeyword)) continue;
                friends.add(MemberInfoDto.builder()
                        .memberSeq(friend.getMemberSeq())
                        .memberNickname(friend.getNickname())
                        .memberImg("https://" + bucket + ".s3." + region + ".amazonaws.com/" + friend.getImg())
                        .memberEmail(friend.getEmail())
                        .build());
            } else {
                Member friend = friendList.getOne();
                if(!friend.getNickname().contains(searchKeyword)) continue;
                friends.add(MemberInfoDto.builder()
                        .memberSeq(friend.getMemberSeq())
                        .memberNickname(friend.getNickname())
                        .memberImg("https://" + bucket + ".s3." + region + ".amazonaws.com/" + friend.getImg())
                        .memberEmail(friend.getEmail())
                        .build());
            }
        }

        return FriendListDto.builder()
                .memberEmail(memberEmail)
                .friendList(friends)
                .build();
    }
}
