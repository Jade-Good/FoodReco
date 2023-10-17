package com.ssafy.special.service.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.special.domain.food.Food;
import com.ssafy.special.domain.food.FoodIngredient;
import com.ssafy.special.domain.food.Ingredient;
import com.ssafy.special.domain.member.Member;
import com.ssafy.special.domain.member.MemberAllergy;
import com.ssafy.special.domain.member.MemberFoodPreference;
import com.ssafy.special.domain.member.MemberRecommend;
import com.ssafy.special.dto.RecentRecommendFoodDto;
import com.ssafy.special.dto.RecentRecommendFoodResult;
import com.ssafy.special.dto.request.FeedbackDto;
import com.ssafy.special.dto.request.UserTasteDto;
import com.ssafy.special.dto.response.RecommendFoodDto;
import com.ssafy.special.dto.response.RecommendFoodResultDto;
import com.ssafy.special.repository.food.FoodIngredientRepository;
import com.ssafy.special.repository.food.FoodRepository;
import com.ssafy.special.repository.member.MemberAllergyRepository;
import com.ssafy.special.repository.member.MemberFoodPreferenceRepository;
import com.ssafy.special.repository.member.MemberRecommendRepository;
import com.ssafy.special.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.persistence.EntityNotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Setter
@Service
@RequiredArgsConstructor
@Transactional
public class MemberRecommendService {

    private final MemberRecommendRepository memberRecommendRepository;
    private final MemberRepository memberRepository;
    private final WebClient webClient;
    private final MemberService memberService;
    private final FoodRepository foodRepository;
    private final MemberFoodPreferenceRepository memberFoodPreferenceRepository;
    private final FoodIngredientRepository foodIngredientRepository;
    private final MemberAllergyRepository memberAllergyRepository;
    private final MemberGoogleAuthService memberGoogleAuthService;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    /* 묵시적 피드백 반영 */
    /*
    차단:0,
	패스:2,
	상세보기:3,
	좋아요:4
//    */
//    피드백시 다음 음식이 어떤건지 프론트에서 알려줌
//    그 음식을 추천받은 DB 추가하기 -> 푸드 레이팅에 1로
//    만약 다음 음식이 없는 경우 아무 처리x
    public void implicitFeedback(String memberEmail, FeedbackDto feedbackDto, Long nextFoodSeq, int googleSteps, String weather) throws Exception {
        boolean isLast = false;

        Optional<Member> memberOptional = memberRepository.findByEmail(memberEmail);

        if (memberOptional.isEmpty()) {
            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }

        Long memberSeq = memberOptional.get().getMemberSeq();
        Long foodSeq = feedbackDto.getFoodSeq();
        Food nowFood = foodRepository.findFoodByFoodSeq(foodSeq);
        int feedback = feedbackDto.getFeedback();
        int lastFoodRating = -1;
        int steps = memberOptional.get().getActivity();
        int totalSteps = steps + googleSteps;
        if(weather == null) {
            weather = "맑음";
        }

        List<MemberRecommend> memberRecommend = memberRecommendRepository
                .findLatestMemberRecommend(memberSeq, foodSeq, PageRequest.of(0,1));

        if(!memberRecommend.isEmpty()){
            lastFoodRating = memberRecommend.get(0).getFoodRating();
        }

        if (nextFoodSeq == 0L) {
            isLast = true;
        }

        if (feedback == 0) {
            lastFoodRating = 0;
            log.info("차단");
//           현재 음식을 차단 리스트에 추가
            Optional<MemberFoodPreference> memberFoodPreferenceList = memberFoodPreferenceRepository.findMemberFoodPreferenceByMember_MemberSeqAndFood_FoodSeq(memberSeq, foodSeq);
            if (memberFoodPreferenceList.isEmpty()) {
                MemberFoodPreference memberFoodPreference = MemberFoodPreference.builder()
                        .member(memberOptional.get())
                        .food(nowFood)
                        .preferenceType(1)
                        .build();
                memberFoodPreferenceRepository.save(memberFoodPreference);
            } else {
                memberFoodPreferenceRepository.updateMemberFoodPreference(1, memberSeq, foodSeq);
            }
        } else if (feedback == 2) {
            lastFoodRating = 2;
            log.info("패스");
        } else if (feedback == 3) {
            lastFoodRating = 3;
            log.info("상세보기");
        } else if (feedback == 4) {
            lastFoodRating = 4;
            log.info("좋아요");
//            현재 음식을 좋아하는 리스트에 추가하기
            Optional<MemberFoodPreference> memberFoodPreferenceList =
                    memberFoodPreferenceRepository.findMemberFoodPreferenceByMember_MemberSeqAndFood_FoodSeq(memberSeq, foodSeq);
            if (memberFoodPreferenceList.isEmpty()) {
                MemberFoodPreference memberFoodPreference = MemberFoodPreference.builder()
                        .member(memberOptional.get())
                        .food(nowFood)
                        .preferenceType(0)
                        .build();
                memberFoodPreferenceRepository.save(memberFoodPreference);
            } else {
                memberFoodPreferenceRepository.updateMemberFoodPreference(0, memberSeq, foodSeq);
            }
        }

//        다음에 볼 음식을 추천받은 리스트에 추가하기
//        상세보기, 좋아요가 아니면서 마지막 추천 음식이 아닌 경우
        if ((feedback != 3 || feedback != 4) && !isLast) {
            Food nextFood = foodRepository.findFoodByFoodSeq(nextFoodSeq);
            MemberRecommend memberRecommend1 = MemberRecommend.builder()
                    .member(memberOptional.get())
                    .food(nextFood)
                    .recommendAt(LocalDateTime.now())
                    .weather(weather)
                    .activityCalorie(totalSteps)
                    .foodRating(1)
                    .build();
            memberRecommendRepository.save(memberRecommend1);
            log.info("다음 음식 추천 히스토리 추가 완료");
        }
//        피드백 반영
//        패스할때 이전에 상세보기였는지, 좋아요였는지,
//        언제 추천받았고 패스를 한건지 시간을 고려해서 패스 반영
        if (!memberRecommend.isEmpty()) {
            if(feedback == 2){
                int lastStatus = memberRecommend.get(0).getFoodRating();
                if(lastStatus == 3){
//                  지난번에 상세를 봤지만 이번에는 패스이므로 pass로 업데이트 -> 일주일을 기준으로 데이터 수정 또는 데이터 추가
                    Duration duration = Duration.between(memberRecommend.get(0).getRecommendAt(), LocalDateTime.now());
                    long differenceInDays = duration.toDays();
                    if(differenceInDays < 8)
                        memberRecommendRepository.updateFoodRating(2, memberRecommend.get(0).getMemberRecommendSeq());
                    else{
                        MemberRecommend memberRecommend2 = MemberRecommend.builder()
                                .member(memberOptional.get())
                                .food(nowFood)
                                .weather(weather)
                                .activityCalorie(totalSteps)
                                .foodRating(2)
                                .recommendAt(LocalDateTime.now())
                                .build();
                        memberRecommendRepository.save(memberRecommend2);
                    }
                }else if(lastStatus == 4){
//                  좋아하는 음식이지만 현재는 패스 -> 좋아한지 일주일 넘었으면 새로운 데이터 추가
                    Duration duration = Duration.between(memberRecommend.get(0).getRecommendAt(), LocalDateTime.now());
                    long differenceInDays = duration.toDays();

                    if(differenceInDays >= 8){
                        MemberRecommend memberRecommend2 = MemberRecommend.builder()
                                .member(memberOptional.get())
                                .food(nowFood)
                                .weather(weather)
                                .activityCalorie(totalSteps)
                                .foodRating(4)
                                .recommendAt(LocalDateTime.now())
                                .build();
                        memberRecommendRepository.save(memberRecommend2);
                    }
                }

            }else{
                Duration duration = Duration.between(memberRecommend.get(0).getRecommendAt(), LocalDateTime.now());
                long differenceInDays = duration.toDays();
                if(differenceInDays < 8)
                    memberRecommendRepository.updateFoodRating(lastFoodRating, memberRecommend.get(0).getMemberRecommendSeq());
                else {
                    MemberRecommend memberRecommend2 = MemberRecommend.builder()
                            .member(memberOptional.get())
                            .food(nowFood)
                            .weather(weather)
                            .activityCalorie(totalSteps)
                            .foodRating(lastFoodRating)
                            .recommendAt(LocalDateTime.now())
                            .build();
                    memberRecommendRepository.save(memberRecommend2);
                }
            }
        }else {
            log.info("현재 보고있는 처음 본 음식 피드백 추가");
            MemberRecommend memberRecommend2 = MemberRecommend.builder()
                    .member(memberOptional.get())
                    .food(nowFood)
                    .weather(weather)
                    .activityCalorie(totalSteps)
                    .foodRating(lastFoodRating)
                    .recommendAt(LocalDateTime.now())
                    .build();
            memberRecommendRepository.save(memberRecommend2);
        }
    }

    private Mono<List<RecommendFoodDto>> sendPostRequestAndReceiveRecommendFoodList(List<RecentRecommendFoodDto> recentlyRecommendedFood) throws JsonProcessingException {

        return webClient.post()
                .uri("/recommend")
                .bodyValue(recentlyRecommendedFood)
                .retrieve()
                .bodyToFlux(RecommendFoodDto.class)
                .collectList();
    }

    //    컨텐츠 기반 필터링
    public List<RecommendFoodResultDto> recommendFood(String memberEmail, int googleSteps, String weather) throws EntityNotFoundException, Exception {
        Optional<Member> memberOptional = memberRepository.findByEmail(memberEmail);
        if (memberOptional.isEmpty()) {
            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }
        Long memberSeq = memberOptional.get().getMemberSeq();
        LocalDateTime now = LocalDateTime.now();
        int steps = memberOptional.get().getActivity();
        int totalSteps = steps + googleSteps;
        if(weather == null) {
            weather = "맑음";
        }


        // 추천 음식 가져오기
        List<RecommendFoodDto> recommendFoodDtoList = getRecommendList(memberSeq, now, memberEmail, googleSteps, weather);
//        log.info("추천 음식: " + recommendFoodDtoList.get(0).getName());
//        1. 첫 번째 음식은 추천받은 히스토리에 추가하기
        Optional<Long> foodSeqOptional = recommendFoodDtoList.stream()
                .findFirst()
                .map(RecommendFoodDto::getFoodSeq);

        if (foodSeqOptional.isPresent()) {
            Food firstFood = foodRepository.findFoodByFoodSeq(recommendFoodDtoList.get(0).getFoodSeq());

            MemberRecommend memberRecommend = MemberRecommend.builder()
                    .member(memberOptional.get())
                    .food(firstFood)
                    .recommendAt(LocalDateTime.now())
                    .weather(weather)
                    .activityCalorie(totalSteps)
                    .foodRating(1)
                    .build();
            memberRecommendRepository.save(memberRecommend);

//        2. 알러지 필터링
            List<MemberAllergy> memberAllergyList = memberAllergyRepository.findMemberAllergiesByMember_MemberSeq(memberSeq);
            recommendFoodDtoList = filteringByAllergy(memberAllergyList, recommendFoodDtoList);
//        3. 차단 필터링
            List<MemberFoodPreference> memberFoodHateList = memberFoodPreferenceRepository
                    .findMemberFoodPreferencesByMember_MemberSeqAndPreferenceType(memberSeq, 1);
            recommendFoodDtoList = filteringByHate(memberFoodHateList, recommendFoodDtoList);
//        4. 최근에 먹음 처리
            List<Long> memberRecommendsWithinOneWeek = memberRecommendRepository.findMemberRecommendsWithinOneWeek(memberSeq, LocalDateTime.now());
            recommendFoodDtoList = filteringByRecently(memberRecommendsWithinOneWeek, recommendFoodDtoList);

//        5. 추천 리스트가 없는 경우 일단 보류
//            추천된 음식에 해당하는 음식 상세정보 합쳐서 프론트로 넘겨주기
//            foodRepository.findFoodByFoodSeq(foodSeq);
            List<RecommendFoodResultDto> RecommendFoodResultList = new ArrayList<>();
            for (RecommendFoodDto recommendFoodDto : recommendFoodDtoList) {
                Food food = foodRepository.findFoodByFoodSeq(recommendFoodDto.getFoodSeq());

                int ingredientSim = (int)(recommendFoodDto.getSimilarity()*100);
//                int nameSim = (int) (recommendFoodDto.getNameSimilarity() * 100);
                RecommendFoodResultDto recommendFoodResultDto = RecommendFoodResultDto.builder()
                        .recommendedFoodSeq(recommendFoodDto.getFoodSeq())
                        .ingredientSimilarity(recommendFoodDto.getSimilarity())
                        .recommendedFoodName(recommendFoodDto.getName())
                        .foodNameSimilarity(recommendFoodDto.getNameSimilarity())
                        .originalFoodName(recommendFoodDto.getOriginName())
                        .type(food.getType())
                        .category(food.getCategory())
                        .cookingMethod(food.getCookingMethod())
                        .img("https://" + bucket + ".s3." + region + ".amazonaws.com/" + food.getImg())
                        .reason(recommendFoodDto.getName()
                                + "은(는) 지난번에 드신 "
                                + recommendFoodDto.getOriginName()
                                + "와(과) 재료 및 조리방식이 "
                                + ingredientSim + "% 유사하여 추천합니다.")
                        .build();
                RecommendFoodResultList.add(recommendFoodResultDto);
            }

            int endIndex = Math.min(RecommendFoodResultList.size(), 50);
            return RecommendFoodResultList.subList(0, endIndex);
        }

        return null;
    }

    public List<RecommendFoodDto> getRecommendList(Long memberSeq, LocalDateTime now,String memberEmail, int googleSteps, String nowWeather) throws JsonProcessingException {
//        지난 1~2주 사이에 추천 받은 음식을 기반으로 추천
        List<RecentRecommendFoodResult> recentlyRecommendedFood = memberRecommendRepository.findRecentlyRecommendedFood(memberSeq, now);

        if(nowWeather == null) {
            nowWeather = "맑음";
        }

        Optional<Member> optionalMember = memberRepository.findByMemberSeq(memberSeq);
        int steps = optionalMember.get().getActivity();
        int totalSteps = steps + googleSteps;




//          좋아하는 음식 리스트
        Set<RecentRecommendFoodDto> recommendedSet = new HashSet<>();
        if(recentlyRecommendedFood.size()>0){
            List<RecentRecommendFoodDto> recentlyRecommendedFoodList = recentlyRecommendedFood.stream()
                    .map(result -> new RecentRecommendFoodDto(result.getFoodSeq(), result.getName()))
                    .collect(Collectors.toList());
            recommendedSet.addAll(recentlyRecommendedFoodList);
        }



        List<UserTasteDto> userFavoriteList = memberService.getUserPreference(memberEmail, 0);
        if(userFavoriteList.size()>0) {
            List<RecentRecommendFoodDto> favoriteList = userFavoriteList.stream()
                    .map(RecentRecommendFoodDto::new)
                    .collect(Collectors.toList());
            recommendedSet.addAll(favoriteList);
        }


        List<RecentRecommendFoodDto> unionedList = recommendedSet.stream().distinct().collect(Collectors.toList());

//        현재상황과 유사한 활동량과 날씨를 추출
        List<RecentRecommendFoodResult> resultList = new ArrayList<>();

        if(totalSteps >= 1000)
            resultList = memberRecommendRepository.findSimilarRecommendedFood(memberSeq, nowWeather, totalSteps-1000, totalSteps+1000);
        else if (resultList.size() == 0 && totalSteps >= 2000) {
            resultList = memberRecommendRepository.findSimilarRecommendedFood(memberSeq, nowWeather, totalSteps-2000, totalSteps+2000);
        }
        else if (resultList.size() == 0 && totalSteps >= 3000) {
            resultList = memberRecommendRepository.findSimilarRecommendedFood(memberSeq, nowWeather, totalSteps -3000, totalSteps+3000);
        }

        List<RecentRecommendFoodDto> similarFoodList = resultList.stream()
                .map(result -> new RecentRecommendFoodDto(result.getFoodSeq(), result.getName()))
                .collect(Collectors.toList());

        Set<RecentRecommendFoodDto> similarAdded = new HashSet<>();
        similarAdded.addAll(unionedList);
        similarAdded.addAll(similarFoodList);

        List<RecentRecommendFoodDto> similarAddedList = new ArrayList<>(similarAdded);

        return sendPostRequestAndReceiveRecommendFoodList(similarAddedList).block();
    }


    public List<RecommendFoodDto> filteringByAllergy(List<MemberAllergy> memberAllergyList, List<RecommendFoodDto> recommendFoodDtoList) {
        if (memberAllergyList.isEmpty()) {
            return recommendFoodDtoList;
        }
        Set<Long> allergyIngredients = memberAllergyList.stream()
                .map(MemberAllergy::getIngredient) // Ingredient 객체를 얻습니다.
                .map(Ingredient::getIngredientSeq) // Ingredient 객체에서 ingredientSeq를 얻습니다.
                .collect(Collectors.toSet());
        return recommendFoodDtoList.stream()
                .filter(recommendFood -> {
                    List<FoodIngredient> foodIngredientList = foodIngredientRepository.findFoodIngredientsByFood_FoodSeqAndIngredient_IsAllergy(recommendFood.getFoodSeq(), 1);

                    log.info("FoodIngredientList for foodSeq {}: {}", recommendFood.getFoodSeq(), foodIngredientList);

                    Set<Long> foodAllergyIngredients = foodIngredientList.stream()
                            .map(FoodIngredient::getIngredient)
                            .map(Ingredient::getIngredientSeq)
                            .collect(Collectors.toSet());

                    log.info("AllergyIngredients: {}", allergyIngredients);
                    log.info("FoodAllergyIngredients for foodSeq {}: {}", recommendFood.getFoodSeq(), foodAllergyIngredients);

                    boolean isDisjoint = Collections.disjoint(allergyIngredients, foodAllergyIngredients);
                    log.info("Is Disjoint for foodSeq {}: {}", recommendFood.getFoodSeq(), isDisjoint);

                    return isDisjoint;
                })
                .collect(Collectors.toList());
    }

    public List<RecommendFoodDto> filteringByHate(List<MemberFoodPreference> memberFoodHateList, List<RecommendFoodDto> recommendFoodDtoList) {
        if (!memberFoodHateList.isEmpty()) {
            return recommendFoodDtoList;
        }
        Set<Long> hatedFoodSeqs = memberFoodHateList.stream()
                .map(memberFoodPreference -> memberFoodPreference.getFood().getFoodSeq())
                .collect(Collectors.toSet());

        return recommendFoodDtoList.stream()
                .filter(recommendFoodDto -> !hatedFoodSeqs.contains(recommendFoodDto.getFoodSeq()))
                .collect(Collectors.toList());
    }
    public List<RecommendFoodDto> filteringByRecently(List<Long> memberRecommendsWithinOneWeek, List<RecommendFoodDto> recommendFoodDtoList) {
        if (memberRecommendsWithinOneWeek.isEmpty()) {
            return recommendFoodDtoList;
        }

        Set<Long> recentlyRecommendedFoodSeqs = new HashSet<>(memberRecommendsWithinOneWeek);

        return recommendFoodDtoList.stream()
                .filter(recommendFoodDto -> !recentlyRecommendedFoodSeqs.contains(recommendFoodDto.getFoodSeq()))
                .collect(Collectors.toList());
    }


}
