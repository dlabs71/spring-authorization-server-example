package ru.dlabs.sas.example.jsso.components.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import ru.dlabs.sas.example.jsso.components.RegistrationStore;
import ru.dlabs.sas.example.jsso.dto.RegistrationDto;

public class RedisRegistrationStore implements RegistrationStore {

    private final static String SESSION_ID_TO_REG_DATA = "registration_store:session_id_to_reg_data:";

    private final int expiresIn;
    private final StringRedisTemplate redisTemplate;
    private final ValueOperations<String, String> store;
    private final ObjectMapper objectMapper;

    public RedisRegistrationStore(int expiresIn, StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.store = redisTemplate.opsForValue();
        this.objectMapper = objectMapper;
        this.expiresIn = expiresIn;
    }

    @Override
    public void save(RegistrationDto dto, String sessionId) throws Exception {
        String stringDto = objectMapper.writeValueAsString(dto);
        store.set(SESSION_ID_TO_REG_DATA + sessionId, stringDto, expiresIn, TimeUnit.SECONDS);
    }

    @Override
    public RegistrationDto take(String sessionId) throws Exception {
        String stringDto = store.get(SESSION_ID_TO_REG_DATA + sessionId);
        if (stringDto == null) {
            return null;
        }
        redisTemplate.delete(SESSION_ID_TO_REG_DATA + sessionId);
        return objectMapper.readValue(stringDto, RegistrationDto.class);
    }
}
