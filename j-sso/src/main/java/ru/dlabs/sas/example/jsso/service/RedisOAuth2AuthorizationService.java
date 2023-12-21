package ru.dlabs.sas.example.jsso.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.Assert;

public final class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    /**
     * Префикс ключа для объектов OAuth2Authorization, для которых уже существует токен доступа.
     * Т.е. являющихся завершёнными
     */
    private final static String COMPLETE_KEY_PREFIX = "oauth2_authorization_complete:";

    /**
     * Префикс ключа для объектов OAuth2Authorization, для которых процесс авторизации ещё не завершился
     * и токен доступа ещё нет. Данная ситуация может возникнуть при authorization code flow,
     * на этапе работы с authorization code. До запроса на получение токенов доступа.
     */
    private final static String INIT_KEY_PREFIX = "oauth2_authorization_init:";

    private final RedisTemplate<String, OAuth2Authorization> redisTemplate;
    private final ValueOperations<String, OAuth2Authorization> authorizations;
    private final long ttl;

    /**
     * Конструктор класса
     *
     * @param redisTemplate клиент для Redis через который мы будем работать с ним
     * @param ttlInMs       Time To Live для записи
     */
    public RedisOAuth2AuthorizationService(RedisTemplate<String, OAuth2Authorization> redisTemplate, long ttlInMs) {
        this.redisTemplate = redisTemplate;
        this.authorizations = redisTemplate.opsForValue();
        this.ttl = ttlInMs;
    }

    /**
     * Сохранение
     */
    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        String key;
        if (isComplete(authorization)) {
            key = COMPLETE_KEY_PREFIX + authorization.getId();

            // удалим старые данные используемые на этапе инициализации
            String initKey = INIT_KEY_PREFIX + authorization.getId();
            if (Boolean.TRUE.equals(this.redisTemplate.hasKey(initKey))) {
                redisTemplate.delete(initKey);
            }
        } else {
            key = INIT_KEY_PREFIX + authorization.getId();
        }
        this.authorizations.set(key, authorization, this.ttl, TimeUnit.MILLISECONDS);
    }

    /**
     * Удаление
     */
    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        String key;
        if (isComplete(authorization)) {
            key = COMPLETE_KEY_PREFIX + authorization.getId();
        } else {
            key = INIT_KEY_PREFIX + authorization.getId();
        }
        this.redisTemplate.delete(key);
    }

    /**
     * Поиск по идентификатору авторизации
     */
    @Nullable
    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        OAuth2Authorization completeAuthorization = this.authorizations.get(COMPLETE_KEY_PREFIX + id);
        return completeAuthorization != null
            ? completeAuthorization
            : this.authorizations.get(INIT_KEY_PREFIX + id);
    }

    /**
     * Поиск по токену доступа
     */
    @Nullable
    @Override
    public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        OAuth2Authorization authorization = this.findByToken(token, tokenType, COMPLETE_KEY_PREFIX);

        if (authorization == null) {
            authorization = this.findByToken(token, tokenType, INIT_KEY_PREFIX);
        }
        return authorization;
    }

    /**
     * Поиск объекта класса OAuth2Authorization по токену доступа и префиксу ключа.
     * Префиксы ключей находятся в константах данного класса.
     */
    private OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType, String prefixKey) {
        Set<String> allInitKeys = redisTemplate.keys(prefixKey + "*");
        if (allInitKeys != null) {
            for (String authorizationKey : allInitKeys) {
                OAuth2Authorization authorization = this.authorizations.get(authorizationKey);
                if (hasToken(authorization, token, tokenType)) {
                    return authorization;
                }
            }
        }
        return null;
    }

    private static boolean isComplete(OAuth2Authorization authorization) {
        return authorization.getAccessToken() != null;
    }

    private static boolean hasToken(
        OAuth2Authorization authorization,
        String token,
        @Nullable OAuth2TokenType tokenType
    ) {
        if (tokenType == null) {
            return matchesState(authorization, token) ||
                matchesAuthorizationCode(authorization, token) ||
                matchesAccessToken(authorization, token) ||
                matchesIdToken(authorization, token) ||
                matchesRefreshToken(authorization, token) ||
                matchesDeviceCode(authorization, token) ||
                matchesUserCode(authorization, token);
        } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            return matchesState(authorization, token);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            return matchesAuthorizationCode(authorization, token);
        } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            return matchesAccessToken(authorization, token);
        } else if (OidcParameterNames.ID_TOKEN.equals(tokenType.getValue())) {
            return matchesIdToken(authorization, token);
        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            return matchesRefreshToken(authorization, token);
        } else if (OAuth2ParameterNames.DEVICE_CODE.equals(tokenType.getValue())) {
            return matchesDeviceCode(authorization, token);
        } else if (OAuth2ParameterNames.USER_CODE.equals(tokenType.getValue())) {
            return matchesUserCode(authorization, token);
        }
        return false;
    }

    private static boolean matchesState(OAuth2Authorization authorization, String token) {
        return token.equals(authorization.getAttribute(OAuth2ParameterNames.STATE));
    }

    private static boolean matchesAuthorizationCode(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
            authorization.getToken(OAuth2AuthorizationCode.class);
        return authorizationCode != null && authorizationCode.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesAccessToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
            authorization.getToken(OAuth2AccessToken.class);
        return accessToken != null && accessToken.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesRefreshToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
            authorization.getToken(OAuth2RefreshToken.class);
        return refreshToken != null && refreshToken.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesIdToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OidcIdToken> idToken =
            authorization.getToken(OidcIdToken.class);
        return idToken != null && idToken.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesDeviceCode(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode =
            authorization.getToken(OAuth2DeviceCode.class);
        return deviceCode != null && deviceCode.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesUserCode(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2UserCode> userCode =
            authorization.getToken(OAuth2UserCode.class);
        return userCode != null && userCode.getToken().getTokenValue().equals(token);
    }
}
