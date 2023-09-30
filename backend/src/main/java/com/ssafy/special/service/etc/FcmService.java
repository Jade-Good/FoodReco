package com.ssafy.special.service.etc;

import com.google.firebase.FirebaseException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.ssafy.special.domain.crew.Crew;
import com.ssafy.special.domain.crew.CrewMember;
import com.ssafy.special.domain.member.Member;
import com.ssafy.special.dto.request.FcmMessageDto;
import com.ssafy.special.repository.crew.CrewRepository;
import com.ssafy.special.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    private final FirebaseMessaging firebaseMessaging;
    private final MemberRepository memberRepository;
    private final CrewRepository crewRepository;

    public void sendNotificationToMember(FcmMessageDto message){
        Member targetMember =memberRepository.findByMemberSeq(message.getTargetSeq())
                .orElseThrow(()-> new EntityNotFoundException("존재하는 사용자가 아닙니다."));

        if(targetMember.getFcmToken() != null){
            Notification notification = Notification.builder()
                    .setTitle(message.getTitle())
                    .setBody(message.getBody())
                    .setImage(message.getImage())
                    .build();
            Message sendMessage = Message.builder()
                    .setToken(targetMember.getFcmToken())
                    .setNotification(notification)
                    .putAllData(message.getData())
                    .build();
            try{
                log.info("메세지 전송 시작");
                firebaseMessaging.send(sendMessage);
                log.info("메세지 전송 성공");
            }catch (FirebaseMessagingException e){
                log.info("메세지 전송 실패");
                throw new IllegalArgumentException("FCM 메세지 전송 실패");
            }
        }else{
            log.info("알림 받을 사용자가 존재하지 않습니다.");
        }

    }


    public void sendNotificationToCrew(FcmMessageDto message){
        Crew targetCrew =crewRepository.findByCrewSeq(message.getTargetSeq())
                .orElseThrow(()-> new EntityNotFoundException("존재하는 그룹이 아닙니다."));

        List<CrewMember> crewMember = targetCrew.getCrewMembers();
        for (CrewMember c: crewMember) {
            if(c.getMember().getFcmToken() == null){
                log.info("알림 받을 사용자가 존재하지 않습니다.");
                continue;
            }
            if(c.getStatus() != 1){
                log.info(c.getMember().getNickname() +"님은 그룹에 가입하지 않았습니다.");
                continue;
            }
            Notification notification = Notification.builder()
                    .setTitle(message.getTitle())
                    .setBody(message.getBody())
                    .setImage(message.getImage())
                    .build();
            Message sendMessage = Message.builder()
                    .setToken(c.getMember().getFcmToken())
                    .setNotification(notification)
                    .putAllData(message.getData())
                    .build();
            try{
                log.info("메세지 전송 시작");
                firebaseMessaging.send(sendMessage);
                log.info("메세지 전송 성공");
            }catch (FirebaseMessagingException e){
                log.info("메세지 전송 실패");
                throw new IllegalArgumentException("FCM 메세지 전송 실패");
            }
        }
    }
}
