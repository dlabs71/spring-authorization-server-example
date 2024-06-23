package ru.dlabs.sas.example.jsso.config.security.granttype.password;

import java.security.Principal;
import java.util.Collections;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example </div>
 * <div><strong>Creation date:</strong> 2024-06-17 </div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class OAuth2PasswordTokenAuthenticationProvider implements AuthenticationProvider {

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";
    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2PasswordAuthenticationToken passwordAuthentication = (OAuth2PasswordAuthenticationToken) authentication;

        // получим клиента системы
        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClient(passwordAuthentication);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        if (registeredClient == null) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
        }

        // проведём аутентификацию пользователя
        Authentication userAuthentication = this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                passwordAuthentication.getUsername(),
                passwordAuthentication.getPassword()
            )
        );

        // Создадим объект OAuth2Authorization который сохраним в Redis
        // На этом этапе объект ещё не является завершенным и сохраняется в спец. список. Смотри описание RedisOAuth2AuthorizationService
        OAuth2Authorization authorization = OAuth2Authorization.withRegisteredClient(registeredClient)
            .id(UUID.randomUUID().toString())
            .principalName(passwordAuthentication.getUsername())
            .authorizationGrantType(AuthorizationGrantType.PASSWORD)
            .authorizedScopes(passwordAuthentication.getScopes())
            .attribute(Principal.class.getName(), userAuthentication)
            .build();
        this.authorizationService.save(authorization);

        // для наглядности оставил так, чтобы было понятно откуда берётся principal
        Authentication principal = authorization.getAttribute(Principal.class.getName());
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
            .registeredClient(registeredClient)
            .principal(principal)
            .authorizationServerContext(AuthorizationServerContextHolder.getContext())
            .authorization(authorization)
            .authorizedScopes(authorization.getAuthorizedScopes())
            .authorizationGrantType(AuthorizationGrantType.PASSWORD)
            .authorizationGrant(passwordAuthentication);

        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.from(authorization);

        // генерируем и указываем в билдер access token
        OAuth2AccessToken accessToken = this.generateAndSetAccessToken(tokenContextBuilder, authorizationBuilder);

        // генерируем refresh token
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)) {
            refreshToken = this.generateAndSetRefreshToken(tokenContextBuilder, authorizationBuilder);
        }

        // Строим новый объект OAuth2Authorization и сохраняем его.
        // На этом этапе он становится завершенным (смотри RedisOAuth2AuthorizationService)
        authorization = authorizationBuilder.build();
        this.authorizationService.save(authorization);

        log.debug("Saved authorization");

        return new OAuth2AccessTokenAuthenticationToken(
            registeredClient,
            clientPrincipal,
            accessToken,
            refreshToken,
            Collections.emptyMap()
        );
    }

    /**
     * Генерируем access token и устанавливаем его в OAuth2Authorization.Builder
     * Код заимствован из {@link org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationProvider}
     */
    private OAuth2AccessToken generateAndSetAccessToken(
        DefaultOAuth2TokenContext.Builder tokenContextBuilder,
        OAuth2Authorization.Builder authorizationBuilder
    ) {
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(
                OAuth2ErrorCodes.SERVER_ERROR,
                "The token generator failed to generate the access token.",
                ERROR_URI
            );
            throw new OAuth2AuthenticationException(error);
        }

        log.debug("Generated access token");

        OAuth2AccessToken accessToken = new OAuth2AccessToken(
            OAuth2AccessToken.TokenType.BEARER,
            generatedAccessToken.getTokenValue(),
            generatedAccessToken.getIssuedAt(),
            generatedAccessToken.getExpiresAt(),
            tokenContext.getAuthorizedScopes()
        );
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken, (metadata) ->
                metadata.put(
                    OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
                    ((ClaimAccessor) generatedAccessToken).getClaims()
                ));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }
        return accessToken;
    }

    /**
     * Генерируем refresh token
     * Код заимствован из {@link org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationProvider}
     */
    private OAuth2RefreshToken generateAndSetRefreshToken(
        DefaultOAuth2TokenContext.Builder tokenContextBuilder,
        OAuth2Authorization.Builder authorizationBuilder
    ) {
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
        OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
        if (generatedRefreshToken != null) {
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken refreshToken)) {
                OAuth2Error error = new OAuth2Error(
                    OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate a valid refresh token.",
                    ERROR_URI
                );
                throw new OAuth2AuthenticationException(error);
            }

            log.debug("Generated refresh token");

            authorizationBuilder.refreshToken(refreshToken);
            return refreshToken;
        }
        return null;
    }

    /**
     * Код заимствован из класса OAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient()
     */
    private OAuth2ClientAuthenticationToken getAuthenticatedClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2PasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
