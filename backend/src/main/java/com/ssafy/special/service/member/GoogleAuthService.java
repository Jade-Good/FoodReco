package com.ssafy.special.service.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.special.domain.member.Member;
import com.ssafy.special.dto.FitnessDto;
import com.ssafy.special.dto.request.GoogleTokenDto;
import com.ssafy.special.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GoogleAuthService {
    private final MemberRepository memberRepository;
    @Autowired
    private RestTemplate restTemplate;
    private String accessToken;
    public String getAccessTokens(String memberEmail, String code) throws EntityNotFoundException, JsonProcessingException{
        Optional<Member> member = memberRepository.findByEmail(memberEmail);
        if (member.isEmpty()) {
            throw new EntityNotFoundException("해당 회원 존재하지 않음");
        }


        String url = "https://oauth2.googleapis.com/token";

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", "195561660115-6gse0lsa1ggdm3t9jplps3sodm7e735n.apps.googleusercontent.com");
        requestBody.add("client_secret", "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");
        requestBody.add("code", code);
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("redirect_uri", "http://localhost:3000/mypage/1");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        log.info("헤더 구성 완료");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        log.info(requestEntity.toString());

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        log.info("응답 완료");
        return response.getBody();
    }
    public void storeAuthTokens(String memberEmail, String tokens) throws EntityNotFoundException, JsonProcessingException {
        Optional<Member> member = memberRepository.findByEmail(memberEmail);
        if (member.isEmpty()) {
            throw new EntityNotFoundException("해당 회원 존재하지 않음");
        }
        Long memberSeq = member.get().getMemberSeq();
        ObjectMapper objectMapper = new ObjectMapper();

        GoogleTokenDto googleTokenDto = objectMapper.readValue(tokens, GoogleTokenDto.class);

        memberRepository.updateMemberRefreshToken(memberSeq, googleTokenDto.getRefresh_token());
        accessToken = googleTokenDto.getAccess_token();
    }

    public void getActivityFromGoogle(String memberEmail) throws Exception{
        Optional<Member> member = memberRepository.findByEmail(memberEmail);
        if (member.isEmpty()) {
            throw new EntityNotFoundException("해당 회원 존재하지 않음");
        }

        String url = "https://www.googleapis.com/fitness/v1/users/me/dataset:aggregate";

        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, String>> aggregateByList = new ArrayList<>();
        int hour = LocalDateTime.now().getHour();
        long duration = 0L;
        long startTimeMillis = -1;
        long endTimeMillis = -1;
        if (21 < hour && hour <=24 || 0<= hour && hour <=8){
//            야식 및 아침
//            일일 활동량
            duration = 86_400_000L;
            startTimeMillis = System.currentTimeMillis() - duration;
            endTimeMillis = System.currentTimeMillis();
        }  else if (8 < hour && hour <= 15) {
//            점심
//            오전 활동량(4시간)
            duration = 14_400_000L;
            startTimeMillis = System.currentTimeMillis() - duration;
            endTimeMillis = System.currentTimeMillis();
        } else if (15 < hour && hour <= 21) {
//            저녁
//            오후 활동량
            duration = 21_600_000L;
            startTimeMillis = System.currentTimeMillis() - duration;
            endTimeMillis = System.currentTimeMillis();
        }
        if(endTimeMillis == -1 || startTimeMillis == -1){
            throw new Exception("현재시간을 가져오지 못함");
        }

        Map<String, String> aggregateBy = new HashMap<>();
        aggregateBy.put("dataTypeName", "com.google.step_count.delta");
        aggregateBy.put("dataSourceId", "derived:com.google.step_count.delta:com.google.android.gms:estimated_steps");
        aggregateByList.add(aggregateBy);

        requestBody.put("aggregateBy", aggregateByList);
        requestBody.put("bucketByTime", Collections.singletonMap("durationMillis", duration));
        requestBody.put("startTimeMillis", startTimeMillis);
        requestBody.put("endTimeMillis", endTimeMillis);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        log.info("헤더 구성 완료");
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        log.info(requestEntity.toString());
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();

            FitnessDto fitnessDto = objectMapper.readValue(response.getBody(), FitnessDto.class);

            log.info(fitnessDto
                    .getBucket().get(0)
                    .getDataset().get(0)
                    .getPoint().get(0)
                    .getValue().get(0)
                    .getIntVal().toString());
            memberRepository.updateMemberActivity(member.get().getMemberSeq(),
                    Integer.parseInt(fitnessDto
                    .getBucket().get(0)
                    .getDataset().get(0)
                    .getPoint().get(0)
                    .getValue().get(0)
                    .getIntVal().toString()));
        } catch (HttpClientErrorException e) {
//            토큰 만료시 return값으로 controller에서 제어
            log.error("HTTP Error: " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            e.printStackTrace();
        }

    }

//    refresh 토큰으로 accesstoken 재발급
    public void issueNewAccessToken(String memberEmail) throws EntityNotFoundException, JsonProcessingException{
        Optional<Member> member = memberRepository.findByEmail(memberEmail);
        if (member.isEmpty()) {
            throw new EntityNotFoundException("해당 회원 존재하지 않음");
        }
        Long memberSeq = member.get().getMemberSeq();

        String refreshToken = member.get().getGoogleRefreshToken();
        String url = "https://oauth2.googleapis.com";

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", "195561660115-6gse0lsa1ggdm3t9jplps3sodm7e735n.apps.googleusercontent.com");
        requestBody.add("client_secret", "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");
        requestBody.add("refresh_token", refreshToken);
        requestBody.add("grant_type", "refresh_token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        log.info("헤더 구성 완료");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        log.info(requestEntity.toString());

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        log.info("응답 완료");
        String tokens = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();

        GoogleTokenDto googleTokenDto = objectMapper.readValue(tokens, GoogleTokenDto.class);

        accessToken = googleTokenDto.getAccess_token();
    }


}
