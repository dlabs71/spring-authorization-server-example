package ru.dlabs.sas.example.jsso.service.security;

import static ru.dlabs.sas.example.jsso.service.security.IntrospectionService.principalAttributeKey;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.Assert;
import ru.dlabs.sas.example.jsso.dto.security.AuthorizationInfo;
import ru.dlabs.sas.example.jsso.dto.security.AuthorizedUser;

@Slf4j
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

    /**
     * Префикс ключа расширенной информации об объекте OAuth2Authorization.
     */
    private final static String INFO_KEY_PREFIX = "oauth2_authorization_info:";

    private final RedisTemplate<String, OAuth2Authorization> redisTemplate;
    private final RedisTemplate<String, AuthorizationInfo> redisTemplateAuthInfo;
    private final ValueOperations<String, OAuth2Authorization> authorizations;
    private final ValueOperations<String, AuthorizationInfo> authInfoByUser;
    private final long ttl;
    private final Consumer<AuthorizationInfo> onSaveHandler;
    private final Consumer<AuthorizationInfo> onRemoveHandler;

    /**
     * Конструктор класса
     *
     * @param redisTemplate         клиент для Redis через который будут сохраняться объекты OAuth2Authorization
     * @param redisTemplateAuthInfo клиент для Redis через который будут сохраняться объекты AuthorizationInfo
     * @param onSaveHandler         callback метод успешного создания токена (complete)
     * @param onRemoveHandler       callback метод успешного удаления токена
     * @param ttlInMs               Time To Live для записи
     */
    public RedisOAuth2AuthorizationService(
        RedisTemplate<String, OAuth2Authorization> redisTemplate,
        RedisTemplate<String, AuthorizationInfo> redisTemplateAuthInfo,
        Consumer<AuthorizationInfo> onSaveHandler,
        Consumer<AuthorizationInfo> onRemoveHandler,
        long ttlInMs
    ) {
        this.redisTemplate = redisTemplate;
        this.authorizations = redisTemplate.opsForValue();
        this.redisTemplateAuthInfo = redisTemplateAuthInfo;
        this.authInfoByUser = redisTemplateAuthInfo.opsForValue();
        this.ttl = ttlInMs;
        this.onSaveHandler = onSaveHandler;
        this.onRemoveHandler = onRemoveHandler;
    }

    /**
     * Сохранение
     */
    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        if (isComplete(authorization)) {
            String key = COMPLETE_KEY_PREFIX + authorization.getId();
            AuthorizationInfo info = this.saveAuthInfo(authorization);

            // удалим старые данные используемые на этапе инициализации
            String initKey = INIT_KEY_PREFIX + authorization.getId();
            if (Boolean.TRUE.equals(this.redisTemplate.hasKey(initKey))) {
                redisTemplate.delete(initKey);
            }
            this.authorizations.set(key, authorization, this.ttl, TimeUnit.MILLISECONDS);

            // оповещаем о выдаче нового токена
            this.onSaveHandler.accept(info);
        } else {
            String key = INIT_KEY_PREFIX + authorization.getId();
            this.authorizations.set(key, authorization, this.ttl, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Сохранение расширенной информации об объекте OAuth2Authorization.
     */
    private AuthorizationInfo saveAuthInfo(OAuth2Authorization authorization) {
        AuthorizedUser authorizedUser = extractPrincipal(authorization);

        String redirectUri = null;
        OAuth2AuthorizationRequest authRequest = authorization.getAttribute(OAuth2AuthorizationRequest.class.getName());
        if (authRequest != null) {
            redirectUri = authRequest.getRedirectUri();
        }

        String key = INFO_KEY_PREFIX
            + (authorizedUser != null ? authorizedUser.getId().toString() : authorization.getPrincipalName())
            + ":" + authorization.getId();

        boolean keyAlreadyExists = Boolean.TRUE.equals(redisTemplateAuthInfo.hasKey(key));
        AuthorizationInfo lastAuthInfo = null;
        if (keyAlreadyExists) {
            lastAuthInfo = this.authInfoByUser.get(key);
        }

        AuthorizationInfo tokenDto = AuthorizationInfo.builder()
            .clientId(authorization.getRegisteredClientId())
            .startDate(lastAuthInfo != null ? lastAuthInfo.getStartDate() : LocalDateTime.now())
            .lastRefreshDate(LocalDateTime.now())
            .scopes(authorization.getAuthorizedScopes())
            .authorizationGrantType(authorization.getAuthorizationGrantType())
            .authorizationId(authorization.getId())
            .userId(authorizedUser != null ? authorizedUser.getId() : null)
            .username(authorizedUser != null ? authorizedUser.getUsername() : null)
            .redirectUri(redirectUri)
            .build();
        this.authInfoByUser.set(key, tokenDto, this.ttl, TimeUnit.MILLISECONDS);
        return tokenDto;
    }

    /**
     * Удаление информации об авторизации. Только объектов OAuth2Authorization,
     * для которых завершился процесс авторизации.
     */
    public void remove(String authorizationId) {
        String key = COMPLETE_KEY_PREFIX + authorizationId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            OAuth2Authorization completeAuthorization = this.authorizations.get(key);
            this.remove(completeAuthorization);
        }
    }

    /**
     * Удаление информации об авторизации.
     */
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        if (isComplete(authorization)) {
            String key = COMPLETE_KEY_PREFIX + authorization.getId();
            AuthorizationInfo info = this.deleteAuthorizationInfo(authorization);

            // оповещаем об удалении токена
            this.onRemoveHandler.accept(info);
            this.redisTemplate.delete(key);
        } else {
            String key = INIT_KEY_PREFIX + authorization.getId();
            this.redisTemplate.delete(key);
        }
    }

    /**
     * Удаление дополнительной информации об авторизации.
     */
    private AuthorizationInfo deleteAuthorizationInfo(OAuth2Authorization authorization) {
        AuthorizedUser authorizedUser = extractPrincipal(authorization);
        String key = INFO_KEY_PREFIX
            + (authorizedUser != null ? authorizedUser.getId().toString() : authorization.getPrincipalName())
            + ":" + authorization.getId();
        AuthorizationInfo tokenDto = this.authInfoByUser.get(key);
        this.redisTemplateAuthInfo.delete(key);
        return tokenDto;
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

    /**
     * Поиск информации о токенах пользователя по его идентификатору.
     */
    public List<AuthorizationInfo> findInfoByUserId(UUID userId) {
        Set<String> allKeys = redisTemplateAuthInfo.keys(INFO_KEY_PREFIX + userId + ":*");
        List<AuthorizationInfo> result = new ArrayList<>();
        if (allKeys != null) {
            for (String key : allKeys) {
                AuthorizationInfo info = this.authInfoByUser.get(key);
                result.add(info);
            }
        }
        return result;
    }

    /**
     * Получение Principal объекта из {@link OAuth2Authorization}
     */
    private static AuthorizedUser extractPrincipal(OAuth2Authorization authorization) {
        AuthorizedUser authorizedUser = null;
        if (authorization.getAttributes().containsKey(principalAttributeKey)) {
            Authentication userAuthentication = authorization.getAttribute(principalAttributeKey);
            if (userAuthentication.getPrincipal() != null) {
                if (userAuthentication.getPrincipal() instanceof AuthorizedUser principal) {
                    authorizedUser = principal;
                } else {
                    log.warn("Principal object of type "
                                 + userAuthentication.getPrincipal().getClass().getName() + " isn't supported");
                }
            }
        }
        return authorizedUser;
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
