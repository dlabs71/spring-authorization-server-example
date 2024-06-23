package ru.dlabs.sas.example.jsso.service.security;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;

public final class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private final static String KEY_PREFIX = "oauth2_authorization_consent:";

    private final RedisTemplate<String, OAuth2AuthorizationConsent> redisTemplate;
    private final ValueOperations<String, OAuth2AuthorizationConsent> authorizationConsents;
    private final long ttl;

    public RedisOAuth2AuthorizationConsentService(
        RedisTemplate<String, OAuth2AuthorizationConsent> redisTemplate,
        long ttlInMs
    ) {
        this.redisTemplate = redisTemplate;
        this.authorizationConsents = redisTemplate.opsForValue();
        this.ttl = ttlInMs;
    }

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        String id = getId(authorizationConsent);
        this.authorizationConsents.set(KEY_PREFIX + id, authorizationConsent, this.ttl, TimeUnit.MILLISECONDS);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        String id = getId(authorizationConsent);
        this.redisTemplate.delete(KEY_PREFIX + id);
    }

    @Override
    @Nullable
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        String id = getId(registeredClientId, principalName);
        return this.authorizationConsents.get(id);
    }

    private static String getId(String registeredClientId, String principalName) {
        return String.valueOf(Objects.hash(registeredClientId, principalName));
    }

    private static String getId(OAuth2AuthorizationConsent authorizationConsent) {
        return getId(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
    }
}
