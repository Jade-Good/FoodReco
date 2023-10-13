package com.ssafy.special.repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepository {
    // 유저Email를 키로 SseEmitter를 해시맵에 저장할 수 있도록 구현했다.
    private Map<Long, SseEmitter> emitterMap = new HashMap<>();

    public void save(Long memberSeq, SseEmitter sseEmitter) {
        // 이미 해당 memberSeq의 SseEmitter가 존재하는 경우 이전 SseEmitter를 삭제
        if (emitterMap.containsKey(memberSeq)) {
            System.out.println("sse이 ㅇㅆ음");
            SseEmitter previousEmitter = emitterMap.get(memberSeq);
            try {
                previousEmitter.complete(); // 이전 SseEmitter를 종료
            }catch (Exception e){
                System.out.println("에러 발생");
            }
        }
        emitterMap.put(memberSeq, sseEmitter);
    }
    public SseEmitter get(Long memberSeq) {
        return emitterMap.get(memberSeq);
    }

    public void delete(Long memberSeq) {
        // 이미 해당 memberSeq의 SseEmitter가 존재하는 경우 이전 SseEmitter를 삭제
        if (emitterMap.containsKey(memberSeq)) {
            SseEmitter previousEmitter = emitterMap.get(memberSeq);
            try {
                previousEmitter.complete(); // 이전 SseEmitter를 종료
            }catch (Exception e){
                System.out.println("에러 발생");
            }
        }
        emitterMap.remove(memberSeq);
    }

}
