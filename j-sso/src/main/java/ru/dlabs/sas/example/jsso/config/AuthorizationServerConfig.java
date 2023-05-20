package ru.dlabs.sas.example.jsso.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenIntrospectionAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.dlabs.sas.example.jsso.dto.TokenInfoDto;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

    private final static String principalAttributeKey = "java.security.Principal";

    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
    private final AuthorizationServerProperties authorizationServerProperties;
    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        authorizationServerConfigurer.tokenIntrospectionEndpoint((config) -> {
            config.introspectionResponseHandler(this::introspectionResponse);
        });

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        http.securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
                .apply(authorizationServerConfigurer);
        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        return new InMemoryRegisteredClientRepository(
                RegisteredClient.withId("test-client-id")
                        .clientName("Test Client")
                        .clientId("test-client")
                        .clientSecret("{noop}test-client")
                        .redirectUri("http://127.0.0.1:8080/code")
                        .scope("read.scope")
                        .scope("write.scope")
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .tokenSettings(TokenSettings.builder()
                                .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                                .accessTokenTimeToLive(Duration.of(30, ChronoUnit.MINUTES))
                                .refreshTokenTimeToLive(Duration.of(120, ChronoUnit.MINUTES))
                                .reuseRefreshTokens(false)
                                .authorizationCodeTimeToLive(Duration.of(30, ChronoUnit.SECONDS))
                                .build())
                        .build()
        );
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(authorizationServerProperties.getIssuerUrl())
                .tokenIntrospectionEndpoint(authorizationServerProperties.getIntrospectionEndpoint())
                .build();
    }

    private void introspectionResponse(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2TokenIntrospectionAuthenticationToken introspectionAuthenticationToken = (OAuth2TokenIntrospectionAuthenticationToken) authentication;
        TokenInfoDto tokenInfoDto = TokenInfoDto.builder().active(false).build();
        if (introspectionAuthenticationToken.getTokenClaims().isActive()) {
            String token = introspectionAuthenticationToken.getToken();
            OAuth2Authorization tokenAuth = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
            Authentication attributeAuth = tokenAuth.getAttribute(principalAttributeKey);

            tokenInfoDto = TokenInfoDto.builder()
                    .active(true)
                    .principal(attributeAuth.getPrincipal())
                    .authorities(authentication.getAuthorities())
                    .build();
        }

        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        mappingJackson2HttpMessageConverter.write(tokenInfoDto, null, httpResponse);
    }
}
