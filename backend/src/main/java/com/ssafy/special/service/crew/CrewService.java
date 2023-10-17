package com.ssafy.special.service.crew;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.special.domain.crew.*;
import com.ssafy.special.domain.food.Food;
import com.ssafy.special.domain.member.Member;
import com.ssafy.special.dto.request.*;
import com.ssafy.special.dto.response.*;
import com.ssafy.special.repository.EmitterRepository;
import com.ssafy.special.repository.crew.*;
import com.ssafy.special.repository.food.FoodRepository;
import com.ssafy.special.repository.member.MemberRepository;
import com.ssafy.special.service.etc.SseService;
import com.ssafy.special.service.food.FoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CrewService {
    private final SseService sseService;
    private final FoodRepository foodRepository;
    private final MemberRepository memberRepository;
    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final CrewRecommendRepository crewRecommendRepository;
    private final CrewRecommendVoteRepository crewRecommendVoteRepository;
    private final CrewRecommendFoodRepository crewRecommendFoodRepository;
    private final EmitterRepository emitterRepository;

    private final FoodService foodService;
    // S3 버킷 정보. (버킷 - S3 저장소 이름이라고 생각하면 됨)
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;

    private final AmazonS3Client amazonS3Client;

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
            Crew memberCrew = c.getCrew();
            if(c.getStatus() == -1){
                continue;
            }
            int cnt = 0;
            for(CrewMember m : memberCrew.getCrewMembers()){
                if(m.getStatus()!=-1){cnt++;}
            }
            CrewRecommend crewRecommend = crewRecommendRepository.findFirstByCrewOrderByRecommendAtDesc(memberCrew);
            Long daysDifference;
            if(crewRecommend!=null){
                LocalDateTime startDateTime = crewRecommend.getRecommendAt();

                // LocalDate로 변환
                LocalDate startDate = startDateTime.toLocalDate();
                LocalDate endDate = LocalDate.now();
                // 날짜 차이 계산
                daysDifference= ChronoUnit.DAYS.between(startDate, endDate);
            }else{
                daysDifference = -1L;
            }

            CrewDto crew = CrewDto.builder()
                    .crewSeq(c.getCrew().getCrewSeq())
                    .name(c.getCrew().getName())
                    .img("https://" + bucket + ".s3." + region + ".amazonaws.com/" + c.getCrew().getImg())
                    .status(c.getStatus()==0?"미반응":"수락")
                    .crewCnt(cnt)
                    .recentRecommend(daysDifference)
                    .build();
            crews.add(crew);
        }
        return crews;
    }

    @Transactional
    public void registCrewforMember(CrewSignUpDto crewSignUpDto, String memberEmail)
            throws IllegalArgumentException, EntityNotFoundException, JsonProcessingException {
        if(crewSignUpDto.getCrewMembers().isEmpty()) {
            throw new IllegalArgumentException("그룹은 최소 2명 이상이여야 합니다.");
        }
        log.info(crewSignUpDto.getCrewName());
        log.info("testDTod" + crewSignUpDto.getCrewMembers());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(crewSignUpDto.getCrewMembers());

        List<Long> memberSeqs = new ArrayList<>();
        // JsonNode를 순회하며 데이터 추출
        for (JsonNode node : jsonNode) {
            Long memberSeq = node.get("memberSeq").asLong();
            memberSeqs.add(memberSeq);
        }

        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
        String S3_fileName="";
        if(crewSignUpDto.getCrewImg() != null){
            S3_fileName = "crewImg/" + foodService.getRandomFileName();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(crewSignUpDto.getCrewImg().getContentType());
            metadata.setContentLength(crewSignUpDto.getCrewImg().getSize());
            try {
                amazonS3Client.putObject(bucket, S3_fileName, crewSignUpDto.getCrewImg().getInputStream(), metadata);
            }catch (Exception e) {
                e.printStackTrace();
                throw new IllegalStateException("이미지 저장 중 에러 발생");
            }
        }

        // Crew 생성
        Crew crew = Crew.builder()
                .name(crewSignUpDto.getCrewName())
                .img(S3_fileName)
                .status("투표전")
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .build();
        crew = crewRepository.save(crew);

        for (Long memberSeq:memberSeqs) {
            CrewMember crewMember = CrewMember.builder()
                    .crew(crew)
                    .member(
                            Member.builder()
                                    .memberSeq(memberSeq)
                                    .build()
                    )
                    .checkVote(1)
                    .status(0)
                    .build();
            crewMemberRepository.save(crewMember);
        }
        CrewMember crewMember = CrewMember.builder()
                .crew(crew)
                .member(member)
                .status(1)
                .checkVote(1)
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
                            if(joinDto.getCrewJoinType() == 0 ){
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

    @Transactional
    public CrewDetailDto getDetailInfo(Long crewSeq, String memberEmail)
            throws EntityNotFoundException,IllegalArgumentException {
        Crew crew = crewRepository.findByCrewSeq(crewSeq)
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹을 찾을 수 없습니다."));
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 그룹원 데이터 가져오기
        log.info("그룹 및 구성원 데이터");
        List<CrewMembersDto> crewMembers = new ArrayList<>();
        String memberStatus="";
        String memberCheckVote="";
        for (CrewMember c : crew.getCrewMembers()) {
            Member m = c.getMember();
            if(c.getStatus() == -1){continue;}
            if (m.getEmail().equals(memberEmail)) {
                memberStatus = c.getStatus() == 0 ? "미응답" : "수락";
                memberCheckVote = c.getCheckVote()==0 ?"미확인":"확인";
            }else{
                crewMembers.add(CrewMembersDto.builder()
                        .memberSeq(m.getMemberSeq())
                        .memberName(m.getNickname())
                        .memberStatus(c.getStatus() == 0 ? "미응답" : (c.getStatus() == -1 ? "거절" : "수락"))
                        .memberImg("https://" + bucket + ".s3." + region + ".amazonaws.com/" + m.getImg())
                        .build()
                );
            }
        }
        crewMemberRepository.findByMemberAndCrew(member,crew)
                .ifPresent(crewmember->{
                    if(crewmember.getCheckVote() == 0){
                        crewmember.setCheckVote(1);
                        crewMemberRepository.save(crewmember);
                    }
                });

        // 추천 히스트리 가져오기
        log.info("추천 History 가져오기");
        List<CrewRecommendHistoryDto> histories = new ArrayList<>();
        // 추천 받은 기록
        List<CrewRecommend> crewRecommends = crewRecommendRepository.findAllByCrewOrderByRecommendAtDesc(crew);
        VoteRecommendDto voteRecommendDto = null;
        // 추천 받은 기록 내역에서 리스트 가져오기
        for (CrewRecommend crewRecommend: crewRecommends) {
            boolean isF = true;
            int crewMemberCount = 0;
            for(CrewMember c : crew.getCrewMembers()){
                if(c.getStatus() == 1) {
                    crewMemberCount++;
                }
            }
            Food fff=null;
            List<CrewRecommendHistoryByFoodDto> historiesByRecommend = new ArrayList<>();
            List<CrewRecommendFood> crewFoods = crewRecommendFoodRepository.findAllByCrewRecommend(crewRecommend);
            Map<Food, List<Member>> m = new HashMap<>();
            for (CrewRecommendFood f: crewFoods) {
                List<Member> memberList = m.getOrDefault(f.getFood(),new ArrayList<>());
                List<CrewRecommendVote> votes = crewRecommendVoteRepository.findAllByCrewRecommendFood(f);
                for (CrewRecommendVote v: votes) {
                    if(v.getMember().getEmail().equals(memberEmail)){
                        fff = v.getCrewRecommendFood().getFood();
                    }
                    memberList.add(v.getMember());
                    crewMemberCount--;
                }
                m.put(f.getFood(),memberList);
            }
            boolean isT = false;
            for (Food food: m.keySet()) {
                isT = food.equals(fff);
                historiesByRecommend.add(CrewRecommendHistoryByFoodDto.builder()
                        .foodSeq(food.getFoodSeq())
                        .foodName(food.getName())
                        .foodImg("https://" + bucket + ".s3." + region + ".amazonaws.com/" + food.getImg())
                        .foodVoteCount(m.get(food).size())
                        .isVote(isT)
                        .build());
                if(isT){
                    isF = false;
                }
            }
            // 미응답 인원
            historiesByRecommend.add(CrewRecommendHistoryByFoodDto.builder()
                    .foodSeq(0L)
                    .foodName("미투표")
                    .foodImg("/favicon.ico")
                    .foodVoteCount(crewMemberCount)
                    .isVote(isF)
                    .build());
            Collections.sort(historiesByRecommend, Comparator.comparingLong(CrewRecommendHistoryByFoodDto::getFoodSeq));

            if(crew.getStatus().equals("투표중") && voteRecommendDto ==null){
                voteRecommendDto = VoteRecommendDto.builder()
                        .crewRecommendSeq(crewRecommend.getCrewRecommendSeq())
                        .foodList(historiesByRecommend)
                        .crewRecommendTime(crewRecommend.getRecommendAt())
                        .build();
                continue;
            }
            if(crew.getStatus().equals("분석중")){
                continue;
            }
            histories.add(CrewRecommendHistoryDto.builder()
                            .crewRecommendSeq(crewRecommend.getCrewRecommendSeq())
                            .foodList(historiesByRecommend)
                            .crewRecommendTime(crewRecommend.getRecommendAt())
                            .build());
        }

        if(memberStatus.equals("")){
            throw new IllegalArgumentException("그룹 초대가 되지 않은 사용자입니다.");
        }
        CrewDetailDto crewDetailDto = CrewDetailDto.builder()
                .crewSeq(crew.getCrewSeq())
                .crewName(crew.getName())
                .crewImg("https://" + bucket + ".s3." + region + ".amazonaws.com/" + crew.getImg())
                .crewStatus(crew.getStatus())
                .memberStatus(memberStatus)
                .crewMembers(crewMembers)
                .memberSeq(member.getMemberSeq())
                .memberCheckVote(memberCheckVote)
                .histories(histories)
                .voteRecommend(voteRecommendDto)
                .build();
        crewDetailDto.setCrewMembers(crewMembers);

        emitterRepository.delete(member.getMemberSeq());
        return crewDetailDto;
    }

    @Transactional
    public void updateCrew(CrewUpdateDto crewUpdateDto)
            throws EntityNotFoundException, IllegalArgumentException,IllegalStateException {
        crewRepository.findByCrewSeq(crewUpdateDto.getCrewSeq())
                .ifPresentOrElse(
                        selectCrew->{
                            if(!selectCrew.getStatus().equals("투표전")){
                                throw new IllegalArgumentException(selectCrew.getStatus()+ "에는 변경이 불가 합니다.");
                            }
                            selectCrew.setName(crewUpdateDto.getName());
                            if(crewUpdateDto.getImg() != null){
                                String S3_fileName = "crewImg/" + foodService.getRandomFileName();
                                ObjectMetadata metadata = new ObjectMetadata();
                                metadata.setContentType(crewUpdateDto.getImg().getContentType());
                                metadata.setContentLength(crewUpdateDto.getImg().getSize());
                                try {
                                    amazonS3Client.putObject(bucket, S3_fileName, crewUpdateDto.getImg().getInputStream(), metadata);
                                    selectCrew.setImg(S3_fileName);
                                }catch (Exception e) {
                                    e.printStackTrace();
                                    throw new IllegalStateException("이미지 저장 중 에러 발생");
                                }
                            }
                            crewRepository.save(selectCrew);

                        },
                        () -> new EntityNotFoundException("해당 그룹을 찾을 수 없습니다.")
                );
    }


    @Transactional
    public void vote(VoteDto voteDto, String memberEmail) {
        Crew crew = crewRepository.findByCrewSeq(voteDto.getCrewSeq())
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹을 찾을 수 없습니다."));
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        if(!crew.getStatus().equals("투표중")){
            throw new IllegalArgumentException("투표 기간이 아닙니다.");
        }

        CrewRecommend crewRecommend = crewRecommendRepository.findByCrewRecommendSeq(voteDto.getCrewRecommendSeq());
        Food food = foodRepository.findFoodByFoodSeq(voteDto.getFoodSeq());
        CrewRecommendFood crewRecommendFood = crewRecommendFoodRepository.findByCrewRecommendAndFood(crewRecommend,food);

        List<CrewRecommendFood> crewRecommendFoods = crewRecommendFoodRepository.findAllByCrewRecommend(crewRecommend);
        boolean isVote = false;
        for(CrewRecommendFood c : crewRecommendFoods){
            CrewRecommendVote crewRecommendVote = crewRecommendVoteRepository.findByCrewRecommendFoodAndMember(c,member)
                    .orElse(null);
            if(crewRecommendVote != null){
                crewRecommendVote.setCrewRecommendFood(crewRecommendFood);
                crewRecommendVoteRepository.save(crewRecommendVote);
                isVote = true;
                break;
            }
        }
        if(!isVote){
            crewRecommendVoteRepository.save(CrewRecommendVote.builder()
                            .member(member)
                            .crewRecommendFood(crewRecommendFood)
                    .build());
        }
        log.info("voteONE1");
        for(CrewMember m : crew.getCrewMembers()){
            log.info("voteONE2");
            VoteRecommendDto voteRecommendDto = getVoteList(crewRecommend,crew,m.getMember());
            sseService.voteToOne(m.getMember().getMemberSeq(),voteRecommendDto);

        }
    }

    @Transactional
    public VoteRecommendDto getVoteList(CrewRecommend crewRecommend,Crew crew,Member member){
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
        boolean isF = true;
        for (Food food: m.keySet()) {
            boolean isT = false;
            for(Member mmm :m.get(food)){
                if (mmm.getMemberSeq().equals(member.getMemberSeq())){
                    isT = true;
                    isF = false;
                }
            }
            historiesByRecommend.add(CrewRecommendHistoryByFoodDto.builder()
                    .foodSeq(food.getFoodSeq())
                    .foodName(food.getName())
                    .foodImg("https://" + bucket + ".s3." + region + ".amazonaws.com/" + food.getImg())
                    .foodVoteCount(m.get(food).size())
                    .isVote(isT)
                    .build());
        }
        // 미응답 인원
        historiesByRecommend.add(CrewRecommendHistoryByFoodDto.builder()
                .foodSeq(0L)
                .foodName("미투표")
                .foodImg("/favicon.ico")
                .isVote(isF)
                .foodVoteCount(crewMemberCount)
                .build());
        VoteRecommendDto voteRecommendDto = null;

        Collections.sort(historiesByRecommend, Comparator.comparingLong(CrewRecommendHistoryByFoodDto::getFoodSeq));

        if(crew.getStatus().equals("투표중") && voteRecommendDto ==null){
            voteRecommendDto = VoteRecommendDto.builder()
                    .crewRecommendSeq(crewRecommend.getCrewRecommendSeq())
                    .foodList(historiesByRecommend)
                    .crewRecommendTime(crewRecommend.getRecommendAt())
                    .build();
        }
        return voteRecommendDto;

    }
}
