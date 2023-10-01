package com.ssafy.special.service.crew;

import com.ssafy.special.domain.crew.*;
import com.ssafy.special.domain.food.Food;
import com.ssafy.special.domain.member.Member;
import com.ssafy.special.dto.request.CrewDto;
import com.ssafy.special.dto.request.CrewJoinDto;
import com.ssafy.special.dto.request.CrewSignUpDto;
import com.ssafy.special.dto.response.*;
import com.ssafy.special.repository.crew.*;
import com.ssafy.special.repository.food.FoodRepository;
import com.ssafy.special.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CrewService {
    private final MemberRepository memberRepository;
    private final CrewRepository crewRepository;
    private final FoodRepository foodRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final CrewRecommendRepository crewRecommendRepository;
    private final CrewRecommendVoteRepository crewRecommendVoteRepository;
    private final CrewRecommendFoodRepository crewRecommendFoodRepository;
    private final TaskScheduler taskScheduler;
    private final SseService sseService;

    /*
     * 사용자 Email으로 자신이 속한 crew List을 출력하는 메소드
     * 사용자 정보를 가져온 후 Crew 내용을 제공한다.
     */
    public List<CrewDto> getCrewListforMemeber(String memberEmail)
            throws EntityNotFoundException{
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
        // 회원이 속한 모든 크루를 가져옵니다.
        List<CrewDto> crews = new ArrayList<>();
        for (CrewMember c:member.getCrewMembers()) {
            CrewDto crew = CrewDto.builder()
                    .crewSeq(c.getCrew().getCrewSeq())
                    .name(c.getCrew().getName())
                    .img(c.getCrew().getImg())
                    .status(c.getStatus()==0?"미반응":(c.getStatus()==-1?"거절":"수락"))
                    .build();
            crews.add(crew);
        }
        return crews;
    }

    @Transactional
    public void registCrewforMember(CrewSignUpDto crewSignUpDto, String memberEmail)
            throws IllegalArgumentException, EntityNotFoundException {
        if(crewSignUpDto.getCrewMembers().isEmpty()) {
            throw new IllegalArgumentException("그룹은 최소 2명 이상이여야 합니다.");
        }
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
        // Crew 생성
        Crew crew = Crew.builder()
                .name(crewSignUpDto.getCrewName())
                .img(crewSignUpDto.getCrewImg())
                .status("투표전")
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .build();
        crew = crewRepository.save(crew);

        for (Long memberSeq:crewSignUpDto.getCrewMembers()) {
            CrewMember crewMember = CrewMember.builder()
                    .crew(crew)
                    .member(
                            Member.builder()
                                    .memberSeq(memberSeq)
                                    .build()
                    )
                    .build();
            crewMemberRepository.save(crewMember);
        }
        CrewMember crewMember = CrewMember.builder()
                .crew(crew)
                .member(member)
                .status(1)
                .build();
        crewMemberRepository.save(crewMember);
        log.info("crew 생성 완료");
    }

    @Transactional
    public void joinCrewforMember(CrewJoinDto joinDto, String memberEmail)
                    throws EntityNotFoundException,IllegalArgumentException{
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
        CrewId crewId = CrewId.builder()
                        .crew(joinDto.getCrewSeq())
                        .member(member.getMemberSeq())
                        .build();
        crewMemberRepository.findById(crewId)
                .ifPresentOrElse(
                        crewJoin ->{
                            if(crewJoin.getStatus() == 1 && joinDto.getCrewJoinType() == crewJoin.getStatus()){
                                throw new IllegalArgumentException("이미 가입한 그룹입니다.");
                            }
                            if(crewJoin.getStatus() == -1 && joinDto.getCrewJoinType() == crewJoin.getStatus()){
                                throw new IllegalArgumentException("이미 거절한 그룹입니다.");
                            }
                            if(crewJoin.getStatus() == 0){
                                throw new IllegalArgumentException("잘못된 요청입니다.");
                            }
                            crewJoin.setStatus(joinDto.getCrewJoinType());
                            crewMemberRepository.save(crewJoin);
                        } ,
                        ()->{
                            throw new IllegalArgumentException("해당하는 그룹이 없습니다.");
                        }
                );
    }

    public CrewDetailDto getDetailInfo(Long crewSeq, String memberEmail)
            throws EntityNotFoundException,IllegalArgumentException {
        Crew crew = crewRepository.findByCrewSeq(crewSeq)
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹을 찾을 수 없습니다."));
        // sse 연동
        log.info("sse 연결");
        sseService.connectVote(memberEmail);

        // 그룹원 데이터 가져오기
        log.info("그룹 및 구성원 데이터");
        List<CrewMembersDto> crewMembers = new ArrayList<>();
        String memberStatus="";
        for (CrewMember c : crew.getCrewMembers()) {
            Member m = c.getMember();
            if (m.getEmail().equals(memberEmail)) {
                memberStatus = c.getStatus() == 0 ? "미응답" : (c.getStatus() == -1 ? "거절" : "수락");
            }else{
                crewMembers.add(CrewMembersDto.builder()
                        .memberSeq(m.getMemberSeq())
                        .memberName(m.getNickname())
                        .memberStatus(c.getStatus() == 0 ? "미응답" : (c.getStatus() == -1 ? "거절" : "수락"))
                        .memberImg(m.getImg())
                        .build()
                );
            }
        }

        // 추천 히스트리 가져오기
        log.info("추천 History 가져오기");
        List<CrewRecommendHistoryDto> histories = new ArrayList<>();
        // 추천 받은 기록
        List<CrewRecommend> crewRecommends = crewRecommendRepository.findAllByCrew(crew);

        // 추천 받은 기록 내역에서 리스트 가져오기
        for (CrewRecommend crewRecommend: crewRecommends) {
            int crewMemberCount = 0;
            for(CrewMember c : crew.getCrewMembers()){
                if(c.getStatus() == 1) {
                    crewMemberCount++;
                }
            }
            List<CrewRecommendHistoryByFoodDto> historiesByRecommend = new ArrayList<>();
            List<CrewRecommendFood> crewFoods = crewRecommendFoodRepository.findAllByCrewRecommend(crewRecommend);
            Map<Food, List<Member>> m = new HashMap<>();
            for (CrewRecommendFood f: crewFoods) {
                List<Member> memberList = m.getOrDefault(f.getFood(),new ArrayList<>());
                List<CrewRecommendVote> votes = crewRecommendVoteRepository.findAllByCrewRecommendFood(f);
                for (CrewRecommendVote v: votes) {
                    memberList.add(v.getMember());
                    crewMemberCount--;
                }
                m.put(f.getFood(),memberList);
            }
            for (Food food: m.keySet()) {
                historiesByRecommend.add(CrewRecommendHistoryByFoodDto.builder()
                        .foodSeq(food.getFoodSeq())
                        .foodName(food.getName())
                        .foodImg(food.getImg())
                        .foodVoteCount(m.get(food).size())
                        .build());
            }
            // 미응답 인원
            historiesByRecommend.add(CrewRecommendHistoryByFoodDto.builder()
                    .foodSeq(0L)
                    .foodName("미투표")
                    .foodImg("")
                    .foodVoteCount(crewMemberCount)
                    .build());

            histories.add(CrewRecommendHistoryDto.builder()
                            .crewRecommendSeq(crewRecommend.getCrewRecommendSeq())
                            .foodList(historiesByRecommend)
                            .build());
        }

        if(memberStatus.equals("")){
            throw new IllegalArgumentException("그룹 초대가 되지 않은 사용자입니다.");
        }
        CrewDetailDto crewDetailDto = CrewDetailDto.builder()
                .crewSeq(crew.getCrewSeq())
                .crewName(crew.getName())
                .crewImg(crew.getImg())
                .crewStatus(crew.getStatus())
                .memberStatus(memberStatus)
                .crewMembers(crewMembers)
                .histories(histories)
                .build();
        crewDetailDto.setCrewMembers(crewMembers);
        return crewDetailDto;
    }

    @Transactional
    public void updateCrew(CrewDto crewDto)
            throws EntityNotFoundException, IllegalArgumentException {
        crewRepository.findByCrewSeq(crewDto.getCrewSeq())
                .ifPresentOrElse(
                        selectCrew->{
                            if(!selectCrew.getStatus().equals("투표전")){
                                throw new IllegalArgumentException(selectCrew.getStatus()+ "에는 변경이 불가 합니다.");
                            }
                            selectCrew.setName(crewDto.getName());
                            selectCrew.setImg(crewDto.getImg());
                        },
                        () -> new EntityNotFoundException("해당 그룹을 찾을 수 없습니다.")
                );
    }


//    @Transactional
//    public void recommendFood(Long crewSeq, String memberEmail) {
//        Member member = memberRepository.findByEmail(memberEmail)
//                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
//        Crew crew = crewRepository.findByCrewSeq(crewSeq)
//                .orElseThrow(() -> new EntityNotFoundException("해당 그룹을 찾을 수 없습니다."));
//
//        // 분석 시작
//        // sse로 분석 중이라는 알림 전송
//        crew.setStatus("분석중");
//        crewRepository.save(crew);
//
//        /*
//         * 여기에서 멤버 별 추천 리스트를 가져옴
//         * RecommendFoodDto.builder().member(Member.class).recommendFoods(List<Food.class>).build
//         * DB에 저장
//         */
//        // 임시 하드 코딩
//        List<RecommendFoodDto> foods = new ArrayList<>();
//
//        CrewRecommend crewRecommend = CrewRecommend.builder()
//                .crew(crew)
//                .recommendAt(LocalDateTime.now())
//                .build();
//        crewRecommend = crewRecommendRepository.save(crewRecommend);
//
//
//
//        // sse로 투표 시작이라는 알림 전송 + FCM 으로 백그라운드의 그룹원들에게 제공
//        crew.setStatus("투표중");
//        crewRepository.save(crew);
//
//        // 5분뒤 종료하는 스케줄러 실행
//        taskScheduler.schedule(() -> endVote(crew), Instant.now().plusSeconds(30));
//    }

    public void endVote(Crew crew) {
        log.info("투표 종료");
        List<CrewMember> crewMembers = crew.getCrewMembers();
        // 그룹원들에게 종료되었다는 sse 알림 + 백그라운 그룹원들에게 FCM 알림

    }
}
