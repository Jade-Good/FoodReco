package com.ssafy.special.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;

    // key로 value 얻기
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    //  duration 동안 (key, value)를 저장
    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofMillis(duration);
        valueOperations.set(key, value, expireDuration);
    }

    // 데이터 삭제
    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}