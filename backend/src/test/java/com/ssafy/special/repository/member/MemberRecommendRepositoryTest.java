//package com.ssafy.special.repository.member;
//
//import com.ssafy.special.domain.food.Food;
//import com.ssafy.special.domain.member.Member;
//import com.ssafy.special.domain.member.MemberFoodPreference;
//import com.ssafy.special.domain.member.MemberRecommend;
//import com.ssafy.special.dto.request.FeedbackDto;
//import com.ssafy.special.dto.response.MemberDetailDto;
//import com.ssafy.special.dto.response.RecommendFoodDto;
//import com.ssafy.special.dto.response.RecommendFoodResultDto;
//import com.ssafy.special.repository.food.FoodRepository;
//import com.ssafy.special.service.member.MemberRecommendService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//@SpringBootTest
//@Transactional
//class MemberRecommendRepositoryTest {
//    @Autowired
//    FoodRepository foodRepository;
//    @Autowired
//    MemberRecommendRepository memberRecommendRepository;
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    MemberFoodPreferenceRepository memberFoodPreferenceRepository;
//    @Autowired
//    MemberRecommendService memberRecommendService;
//    Food food, food1, food2, nextFood;
//    Optional<Member> member;
//    MemberFoodPreference memberFoodPreference;
//    FeedbackDto feedbackDto;
//    MemberRecommend memberRecommend;
//    @PersistenceContext
//    private EntityManager em;
//
//    @BeforeEach
//    void setUp() {
//        food = foodRepository.findByName("해물볶음우동");
//        food1 = foodRepository.findByName("통삼겹오븐구이");
//        food2 = foodRepository.findByName("순두부찌개");
//        Food food3 = foodRepository.findByName("우동볶음");
//        nextFood = foodRepository.findByName("파스타");
//        member = memberRepository.findByEmail("ldg03198@gmail.com");
//
//        memberFoodPreference = MemberFoodPreference.builder()
//                .food(food)
//                .member(member.get())
//                .preferenceType(0)
//                .build();
//        memberRepository.save(member.get());
//        memberFoodPreferenceRepository.save(memberFoodPreference);
//
//
//        em.clear();
//
//        food = foodRepository.findByName("해물볶음우동");
//
//
//
//        feedbackDto = FeedbackDto.builder()
//                .foodSeq(food.getFoodSeq())
//                .feedback(0)
//                .build();
//
//        memberRecommend = MemberRecommend.builder()
//                .member(member.get())
//                .food(food)
//                .weather("맑음")
//                .foodRating(1)
//                .recommendAt(LocalDateTime.now().minusDays(9))
//                .activityCalorie(300)
//                .build();
//        MemberRecommend memberRecommend1 = MemberRecommend.builder()
//                .member(member.get())
//                .food(food)
//                .weather("맑음")
//                .foodRating(2)
//                .recommendAt(LocalDateTime.now().minusDays(2))
//                .activityCalorie(300)
//                .build();
//        MemberRecommend memberRecommend2 = MemberRecommend.builder()
//                .member(member.get())
//                .food(food2)
//                .weather("맑음")
//                .foodRating(3)
//                .recommendAt(LocalDateTime.now().minusDays(15))
//                .activityCalorie(300)
//                .build();
//        MemberRecommend memberRecommend3 = MemberRecommend.builder()
//                .member(member.get())
//                .food(food3)
//                .weather("맑음")
//                .foodRating(2)
//                .recommendAt(LocalDateTime.now().minusDays(5))
//                .activityCalorie(300)
//                .build();
//        System.out.println("현재날짜 - 29일" + LocalDateTime.now().minusDays(29));
//        memberRecommendRepository.save(memberRecommend);
//        memberRecommendRepository.save(memberRecommend1);
//        memberRecommendRepository.save(memberRecommend2);
//        memberRecommendRepository.save(memberRecommend3);
//
//    }
//
//    @Test
//    public void testRateAdjust() throws Exception {
//
//        Optional<MemberRecommend> findMemberRecommend =
//                memberRecommendRepository.findMemberRecommendByMember_MemberSeqAndFood_FoodSeq(member.get().getMemberSeq(), food.getFoodSeq());
//
////        assertThat(findMemberRecommend.get(0).getMember()).isEqualTo(member);
////        assertThat(findMemberRecommend.get(0).getFood()).isEqualTo(food);
//
//    }
//
//    @Test
//    public void testUpdateRate() throws Exception {
//
//        List<RecommendFoodDto> tempRecommendList = new ArrayList<>();
//        RecommendFoodDto recommendFoodDto = RecommendFoodDto.builder()
//                .foodSeq(17L)
//                .name("오므라이스")
//                .nameSimilarity(0.5)
//                .originName(food.getName())
//                .build();
//        tempRecommendList.add(recommendFoodDto);
//        memberRecommendService.setGlobalrecommendFoodDtoList(tempRecommendList);
//
//        memberRecommendService.implicitFeedback(member.get().getEmail(), feedbackDto, 0L);
//
//        em.clear();
//
//        Optional<MemberRecommend> findMemberRecommend = memberRecommendRepository
//                    .findMemberRecommendByMember_MemberSeqAndFood_FoodSeq(member.get().getMemberSeq(), food.getFoodSeq());
//        assertThat(findMemberRecommend.get().getFoodRating()).isEqualTo(0);
//        Optional<MemberFoodPreference> memberFoodPreferences = memberFoodPreferenceRepository
//                .findMemberFoodPreferenceByMember_MemberSeqAndFood_FoodSeq(member.get().getMemberSeq(), feedbackDto.getFoodSeq());
////        assertThat(memberFoodPreferences.size()).isEqualTo(1);
//
//        assertThat(memberFoodPreferences.get().getMember().getMemberSeq()).isEqualTo(member.get().getMemberSeq());
//        System.out.println("foodPreference.getFood().getName() = " + memberFoodPreferences.get().getFood().getName());
//        System.out.println("foodPreference.getPreferenceType() = " + memberFoodPreferences.get().getPreferenceType());
//
//    }
//    @Test
//    public void testNextFoodRecommend() throws Exception {
//        FeedbackDto feedbackDto2 = FeedbackDto.builder()
//                .foodSeq(food.getFoodSeq())
//                .feedback(3)
//                .build();
//        List<RecommendFoodDto> tempRecommendList = new ArrayList<>();
//        RecommendFoodDto recommendFoodDto = RecommendFoodDto.builder()
//                .foodSeq(17L)
//                .name("오므라이스")
//                .nameSimilarity(0.5)
//                .originName(food.getName())
//                .build();
//        tempRecommendList.add(recommendFoodDto);
//
//        memberRecommendService.setGlobalrecommendFoodDtoList(tempRecommendList);
//
//        List<MemberRecommend> latestMemberRecommend = memberRecommendRepository.findLatestMemberRecommend(member.get().getMemberSeq(), food.getFoodSeq(), PageRequest.of(0, 1));
////        assertThat(latestMemberRecommend.size()).isEqualTo(1);
//        assertThat(latestMemberRecommend.get(0).getFoodRating()).isEqualTo(2);
////        memberRecommendService.implicitFeedback(member.get().getEmail(), feedbackDto, 17L);
////        em.clear();
////
////        Optional<MemberRecommend> findMemberRecommend = memberRecommendRepository
////                .findMemberRecommendByMember_MemberSeqAndFood_FoodSeq(member.get().getMemberSeq(), 17L);
////        assertThat(findMemberRecommend.get().getFoodRating()).isEqualTo(1);
//    }
//
//    @Test
//    public void testRecommend() throws Exception {
//        List<RecommendFoodResultDto> recommendFoodDtoList = memberRecommendService.recommendFood(member.get().getEmail());
//
//        for (RecommendFoodResultDto recommendFoodDto : recommendFoodDtoList) {
//            System.out.println("recommendFoodDto = " + recommendFoodDto);
//        }
//    }
//
//    @Test
//    public void testFilteringRecommend() throws Exception{
//        Food hateFood1 = foodRepository.findFoodByFoodSeq(2199L);
//        Food hateFood2 = foodRepository.findFoodByFoodSeq(2238L);
//        System.out.println("hate1: " + hateFood1.getName());
//        System.out.println("hate2: " + hateFood2.getName());
//
//        MemberFoodPreference memberFoodPreference1 = MemberFoodPreference.builder()
//                .member(member.get())
//                .food(hateFood1)
//                .preferenceType(1)
//                .build();
//        MemberFoodPreference memberFoodPreference2 = MemberFoodPreference.builder()
//                .member(member.get())
//                .food(hateFood2)
//                .preferenceType(1)
//                .build();
//
//        memberFoodPreferenceRepository.save(memberFoodPreference1);
//        memberFoodPreferenceRepository.save(memberFoodPreference2);
////        em.clear();
////        member = memberRepository.findByEmail("ldg03198@gmail.com");
//
//
//        List<RecommendFoodResultDto> recommendFoodDtoList = memberRecommendService.recommendFood(member.get().getEmail());
//        for (RecommendFoodResultDto recommendFoodDto : recommendFoodDtoList) {
//            System.out.println("recommendFoodDto = " + recommendFoodDto);
//
//        }
//    }
//}