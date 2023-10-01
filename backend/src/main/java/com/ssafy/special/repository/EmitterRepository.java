package com.ssafy.special.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepository {

    // 유저Email를 키로 SseEmitter를 해시맵에 저장할 수 있도록 구현했다.
    private Map<Long, SseEmitter> emitterMap = new HashMap<>();

    public SseEmitter save(Long memberSeq, SseEmitter sseEmitter) {
        emitterMap.put(memberSeq, sseEmitter);
        return sseEmitter;
    }

    public Optional<SseEmitter> get(Long memberSeq) {
        return Optional.ofNullable(emitterMap.get(memberSeq));
    }

    public void delete(Long memberSeq) {
        emitterMap.remove(memberSeq);
    }

}
