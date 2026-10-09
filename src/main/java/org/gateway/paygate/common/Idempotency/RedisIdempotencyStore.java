package org.gateway.paygate.common.Idempotency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisIdempotencyStore implements IdempotencyStore{

    private static final String PREFIX  = "idempotency:";
    private final StringRedisTemplate redis;

    @Override
    public boolean setIfAbsent(String key, Duration ttl) {
        try{
            Boolean set = redis.opsForValue().setIfAbsent(PREFIX + key, IN_PROGRESS, ttl);
            return set != null && set;
        }catch (Exception e){
            log.warn("Redis unavailable, skipping idempotency check for key: {}", key, e);
           return true;
        }

    }

    @Override
    public void store(String key, String value, Duration ttl) {
        try {
            redis.opsForValue().set(PREFIX + key, value, ttl);
        }
        catch (DataAccessException e) {
            log.warn("Redis unavailable, skipping storing idempotency result for key: {}", key, e);

        }
    }

    @Override
    public Optional<String> get(String key) {
        try{
            return Optional.ofNullable(redis.opsForValue().get(PREFIX + key));
        }
        catch (DataAccessException e){
            log.warn("Redis unavailable, skipping retrieving idempotency result for key: {}", key, e);
            return Optional.empty();
        }
    }

    @Override
    public void delete(String key) {
        try{
            redis.delete(PREFIX + key);
        }
        catch (DataAccessException e){
            log.warn("Redis unavailable, skipping deleting idempotency result for key: {}", key, e);
        }
    }
}
