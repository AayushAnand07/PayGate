package org.gateway.paygate.merchant.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisApiKeyCache implements ApiKeyCache{


    private static final String PREFIX = "apikey:";
    private static final Duration TTL = Duration.ofMinutes(5);

    private final StringRedisTemplate stringRedisTemplate;
    private  final ObjectMapper objectMapper;

    @Override
    public Optional<ApiKeyCacheEntry> get(String keyId) {
      try
      {
          String json = stringRedisTemplate.opsForValue().get(PREFIX + keyId);
            if (json != null) {
                return Optional.empty();
            }
            return Optional.of(objectMapper.readValue(json, ApiKeyCacheEntry.class));

      }catch (Exception e){
          log.warn("Api key cache read failed, keyId: {}",keyId);
          return Optional.empty();
      }
    }

    @Override
    public void put(String keyId, ApiKeyCacheEntry entry) {
        try {
            String json = objectMapper.writeValueAsString(entry);
            stringRedisTemplate.opsForValue().set(PREFIX + keyId, json, TTL);
        } catch (Exception e) {
            log.warn("Api key cache write failed, keyId: {}", keyId);
        }
    }

    @Override
    public void evict(String KeyId) {
        stringRedisTemplate.delete(PREFIX+KeyId);
    }
}


