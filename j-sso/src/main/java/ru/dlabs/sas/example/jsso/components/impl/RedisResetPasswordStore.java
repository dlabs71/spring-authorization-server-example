package ru.dlabs.sas.example.jsso.components.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import ru.dlabs.sas.example.jsso.components.ResetPasswordStore;

public class RedisResetPasswordStore implements ResetPasswordStore {

    private final static String SESSION_ID_TO_RESET_DATA = "reset_password_store:session_id_to_reset_data:";

    private final int expiresIn;
    private final StringRedisTemplate redisTemplate;
    private final ValueOperations<String, String> store;
    private final ObjectMapper objectMapper;

    public RedisResetPasswordStore(int expiresIn, StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.store = redisTemplate.opsForValue();
        this.objectMapper = objectMapper;
        this.expiresIn = expiresIn;
    }

    @Override
    public void save(StoreItem item, String sessionId) throws Exception {
        String stringDto = objectMapper.writeValueAsString(item);
        store.set(SESSION_ID_TO_RESET_DATA + sessionId, stringDto, expiresIn, TimeUnit.SECONDS);
    }

    @Override
    public StoreItem take(String sessionId) throws Exception {
        String stringDto = store.get(SESSION_ID_TO_RESET_DATA + sessionId);
        if (stringDto == null) {
            return null;
        }
        redisTemplate.delete(SESSION_ID_TO_RESET_DATA + sessionId);
        return objectMapper.readValue(stringDto, StoreItem.class);
    }
}
