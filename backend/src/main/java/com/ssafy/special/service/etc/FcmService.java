package com.ssafy.special.service.etc;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.ssafy.special.domain.member.Member;
import com.ssafy.special.dto.request.FcmMessageDto;
import com.ssafy.special.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

//    @Value("{firebase.service.key.path}")
//    private String FIREBASE_SEND_API_URI;
    private final FirebaseMessaging firebaseMessaging;
    private final MemberRepository memberRepository;

    public void sendNotification(FcmMessageDto message){
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
                e.printStackTrace();
                log.info("메세지 전송 실패");
            }
        }else{
            log.info("알림 받을 사용자가 존재하지 않습니다.");
        }

    }
}
