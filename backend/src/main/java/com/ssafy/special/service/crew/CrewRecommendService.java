package com.ssafy.special.service.crew;

import com.ssafy.special.domain.crew.Crew;
import com.ssafy.special.domain.crew.CrewMember;
import com.ssafy.special.domain.crew.CrewRecommend;
import com.ssafy.special.domain.crew.CrewRecommendFood;
import com.ssafy.special.domain.food.Food;
import com.ssafy.special.domain.member.Member;
import com.ssafy.special.domain.member.MemberAllergy;
import com.ssafy.special.domain.member.MemberFoodPreference;
import com.ssafy.special.dto.response.CrewRecommendHistoryByFoodDto;
import com.ssafy.special.dto.response.RecommendFoodDto;
import com.ssafy.special.dto.response.RecommendFoodResultDto;
import com.ssafy.special.dto.response.VoteRecommendDto;
import com.ssafy.special.repository.crew.CrewMemberRepository;
import com.ssafy.special.repository.crew.CrewRecommendFoodRepository;
import com.ssafy.special.repository.crew.CrewRecommendRepository;
import com.ssafy.special.repository.crew.CrewRepository;
import com.ssafy.special.repository.food.FoodRepository;
import com.ssafy.special.repository.member.MemberAllergyRepository;
import com.ssafy.special.repository.member.MemberFoodPreferenceRepository;
import com.ssafy.special.repository.member.MemberRecommendRepository;
import com.ssafy.special.repository.member.MemberRepository;
import com.ssafy.special.service.etc.SseService;
import com.ssafy.special.service.member.MemberRecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrewRecommendService {
    private final MemberRepository memberRepository;
    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final CrewRecommendRepository crewRecommendRepository;
    private final MemberRecommendService memberRecommendService;
    private final MemberRecommendRepository memberRecommendRepository;
    private final MemberAllergyRepository memberAllergyRepository;
    private final MemberFoodPreferenceRepository memberFoodPreferenceRepository;
    private final CrewRecommendFoodRepository crewRecommendFoodRepository;
    private final FoodRepository foodRepository;
    private final TaskScheduler taskScheduler;
    private final SseService sseService;


    @org.springframework.beans.factory.annotation.Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;

    public void recommendFood(Long crewSeq) throws EntityNotFoundException, Exception {
        Crew crew = crewRepository.findByCrewSeq(crewSeq)
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹을 찾을 수 없습니다."));

        // 분석 시작
        crew.setStatus("분석중");
        crewRepository.save(crew);

        /*
         * 여기에서 멤버 별 추천 리스트를 가져옴
         * RecommendFoodDto.builder().member(Member.class).recommendFoods(List<Food.class>).build
         * DB에 저장
         */
        CrewRecommend crewRecommend = CrewRecommend.builder()
                .crew(crew)
                .recommendAt(LocalDateTime.now())
                .build();
        crewRecommend = crewRecommendRepository.save(crewRecommend);

        List<RecommendFoodDto> crewRecommendFoodList = new ArrayList<>();
        List<MemberAllergy> crewAllergyList = new ArrayList<>();
        List<MemberFoodPreference> crewMemberPreferenceList = new ArrayList<>();
        List<Long> crewMemberRecommendFoodWithinOneWeekList = new ArrayList<>();
        for(CrewMember c : crew.getCrewMembers()){
            crewRecommendFoodList.addAll(memberRecommendService.getRecommendList(c.getMember().getMemberSeq(),LocalDateTime.now(),c.getMember().getEmail(), c.getMember().getActivity(), "맑음"));
            crewAllergyList.addAll(memberAllergyRepository.findMemberAllergiesByMember_MemberSeq(c.getMember().getMemberSeq()));
            crewMemberPreferenceList.addAll(memberFoodPreferenceRepository
                    .findMemberFoodPreferencesByMember_MemberSeqAndPreferenceType(c.getMember().getMemberSeq(), 1));
            crewMemberRecommendFoodWithinOneWeekList.addAll(memberRecommendRepository.findMemberRecommendsWithinOneWeek(c.getMember().getMemberSeq(), LocalDateTime.now()));
        }
//        2. 알러지 필터링
        crewRecommendFoodList = memberRecommendService.filteringByAllergy(crewAllergyList, crewRecommendFoodList);

//        3. 차단 필터링
        crewRecommendFoodList = memberRecommendService.filteringByHate(crewMemberPreferenceList, crewRecommendFoodList);
//        4. 최근에 먹음 처리
        crewRecommendFoodList = memberRecommendService.filteringByRecently(crewMemberRecommendFoodWithinOneWeekList, crewRecommendFoodList);
        Map<Long, Integer> cnt = new HashMap<>();
        for(RecommendFoodDto food : crewRecommendFoodList){
            cnt.put(food.getFoodSeq(),cnt.getOrDefault(food.getFoodSeq(),0) + 1);
        }

        // Map을 List로 변환
        List<Map.Entry<Long, Integer>> list = new ArrayList<>(cnt.entrySet());

        // List를 값으로 정렬 (내림차순)
        Collections.sort(list, new Comparator<Map.Entry<Long, Integer>>() {
            @Override
            public int compare(Map.Entry<Long, Integer> o1, Map.Entry<Long, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue()); // 내림차순
            }
        });
        int foodCnt = 0;
        List<CrewRecommendHistoryByFoodDto> recommendList = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : list) {
            if(foodCnt > 9) break;
            Food food = foodRepository.findFoodByFoodSeq(entry.getKey());
            crewRecommendFoodRepository.save(CrewRecommendFood.builder()
                            .food(food)
                            .crewRecommend(crewRecommend)
                    .build());
            foodCnt++;

            CrewRecommendHistoryByFoodDto crewRecommendHistoryByFoodDto = CrewRecommendHistoryByFoodDto.builder()
                    .foodSeq(food.getFoodSeq())
                    .foodImg("https://" + bucket + ".s3." + region + ".amazonaws.com/" + food.getImg())
                    .foodName(food.getName())
                    .foodVoteCount(0)
                    .isVote(false)
                    .build();
            recommendList.add(crewRecommendHistoryByFoodDto);
        }
        Collections.sort(recommendList, Comparator.comparingLong(CrewRecommendHistoryByFoodDto::getFoodSeq));
        VoteRecommendDto voteRecommendDto =VoteRecommendDto.builder()
                .crewRecommendSeq(crewRecommend.getCrewRecommendSeq())
                .foodList(recommendList)
                .crewRecommendTime(LocalDateTime.now())
                .build();


        // sse로 투표 시작이라는 알림 전송 + FCM 으로 백그라운드의 그룹원들에게 제공
        sseService.chageVote(crewSeq,"start",voteRecommendDto);
        crew.setStatus("투표중");
        crewRepository.save(crew);

        // 5분뒤 종료하는 스케줄러 실행
        taskScheduler.schedule(() -> endVote(crew), Instant.now().plusSeconds(60));
    }

    public void endVote(Crew crew) {
        log.info("투표 종료");
        crew.setStatus("투표전");
        crewRepository.save(crew);
        for(CrewMember c : crew.getCrewMembers()){
            c.setCheckVote(0);
            crewMemberRepository.save(c);
        }
        // 그룹원들에게 종료되었다는 sse 알림 + 백그라운 그룹원들에게 FCM 알림

        sseService.chageVote(crew.getCrewSeq(),"end",null);

    }




}
