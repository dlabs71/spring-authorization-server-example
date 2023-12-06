package ru.dlabs.sas.example.jsso.components.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.codec.Hex;
import ru.dlabs.sas.example.jsso.components.OTPStore;
import ru.dlabs.sas.example.jsso.utils.CryptoUtils;

@Slf4j
public class RedisOTPStore implements OTPStore {

    private final static String SESSION_ID_TO_OTP = "otp_store:session_id_to_otp:";

    private final StringRedisTemplate redisTemplate;
    private final ValueOperations<String, String> store;

    public RedisOTPStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.store = redisTemplate.opsForValue();
    }

    @Override
    public GenerationResult generate(HttpServletResponse response) {
        String otp = RandomStringUtils.randomNumeric(6);
        String sessionId = this.generateSessionId();
        log.info("Generate OTP = " + otp + ". Generate sessionId = " + sessionId);

        store.set(SESSION_ID_TO_OTP + sessionId, otp, maxAge, TimeUnit.SECONDS);

        Cookie cookie = new Cookie(cookieName, sessionId);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(domain);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        log.info("Add cookie to response = " + cookie);
        return new GenerationResult(sessionId, otp);
    }

    @Override
    public boolean validate(String otp, HttpServletRequest request) {
        String sessionId = this.getSessionId(request);
        log.info("Start validate OTP with sessionId = " + sessionId + " and OTP = " + otp);
        if (sessionId != null) {
            String storageOtp = store.get(SESSION_ID_TO_OTP + sessionId);
            if (storageOtp != null) {
                log.info("Storage OTP = " + storageOtp);
                if (storageOtp.equals(otp)) {
                    redisTemplate.delete(SESSION_ID_TO_OTP + sessionId);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getSessionId(HttpServletRequest request) {
        Cookie sessionCookie = Arrays.stream(request.getCookies())
            .filter(item -> cookieName.equals(item.getName()))
            .findFirst()
            .orElse(null);
        if (sessionCookie != null) {
            return sessionCookie.getValue();
        }
        return null;
    }

    private String generateSessionId() {
        UUID uuid = UUID.randomUUID();
        String salt = RandomStringUtils.randomAlphanumeric(8);
        return new String(Hex.encode(CryptoUtils.pbkdf(
            uuid.toString(),
            salt.getBytes(StandardCharsets.UTF_8),
            256,
            2048
        )));
    }
}
