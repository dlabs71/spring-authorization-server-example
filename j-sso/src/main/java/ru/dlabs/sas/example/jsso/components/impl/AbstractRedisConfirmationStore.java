package ru.dlabs.sas.example.jsso.components.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import ru.dlabs.sas.example.jsso.components.ConfirmationStore;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-01</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public abstract class AbstractRedisConfirmationStore implements ConfirmationStore {

    private final int expiresIn;
    private final StringRedisTemplate redisTemplate;
    private final ValueOperations<String, String> store;
    private final ObjectMapper objectMapper;

    public AbstractRedisConfirmationStore(int expiresIn, StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.store = redisTemplate.opsForValue();
        this.objectMapper = objectMapper;
        this.expiresIn = expiresIn;
    }

    public abstract String getKeyPrefix();

    @Override
    public void save(StoreItem item, String sessionId) throws Exception {
        String stringDto = objectMapper.writeValueAsString(item);
        store.set(this.getKeyPrefix() + sessionId, stringDto, expiresIn, TimeUnit.SECONDS);
    }

    @Override
    public StoreItem take(String sessionId) throws Exception {
        String stringDto = store.get(this.getKeyPrefix() + sessionId);
        if (stringDto == null) {
            return null;
        }
        redisTemplate.delete(this.getKeyPrefix() + sessionId);
        return objectMapper.readValue(stringDto, StoreItem.class);
    }
}
