package lemonade.challenge.wordcount.repositories;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class WordCountRepositoryImpl implements WordCountRepository {
    private HashOperations<String, String, Long> hashOperations;

    public WordCountRepositoryImpl(RedisTemplate redisTemplate) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer(Long.class));

        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public Long get(String key) {
        Long value = this.hashOperations.get("count", key);
        return Optional.ofNullable(value).orElse(0L);
    }

    @Override
    public void reduce(Map<String, Integer> map) {
        map.forEach((key, value) -> {
            this.hashOperations.increment("count", key, value);
        });
    }
}
