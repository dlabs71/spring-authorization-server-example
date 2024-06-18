package ru.dlabs.sas.example.jsso.config.security;

import static ru.dlabs.sas.example.jsso.config.security.SecurityConfig.LOGIN_PAGE;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.dlabs.sas.example.jsso.config.security.granttype.password.OAuth2PasswordAuthenticationConverter;
import ru.dlabs.sas.example.jsso.config.security.granttype.password.OAuth2PasswordTokenAuthenticationProvider;
import ru.dlabs.sas.example.jsso.config.security.properties.AuthorizationServerProperties;
import ru.dlabs.sas.example.jsso.service.security.IntrospectionService;

/**
 * Конфигурация SecurityFilterChain сервера авторизации по протоколу OAuth2.
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

    private final IntrospectionService introspectionService;
    private final AuthorizationServerProperties authorizationServerProperties;
    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    private final AuthenticationManager authenticationManager;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        authorizationServerConfigurer.tokenIntrospectionEndpoint((config) -> {
            config.introspectionResponseHandler(introspectionService::introspectionResponse);
        });

        // настроим OAuth2TokenEndpointConfigurer. Добавим поддержку password grant type
        authorizationServerConfigurer.tokenEndpoint(customizer -> {
            customizer.accessTokenRequestConverter(new OAuth2PasswordAuthenticationConverter());
            customizer.authenticationProvider(new OAuth2PasswordTokenAuthenticationProvider(
                authorizationService,
                tokenGenerator,
                authenticationManager
            ));
        });

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        http.securityMatcher(endpointsMatcher)
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))

            .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(
                new LoginUrlAuthenticationEntryPoint(LOGIN_PAGE)
            ))
            .with(authorizationServerConfigurer, Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
            .issuer(authorizationServerProperties.getIssuerUrl())
            .tokenIntrospectionEndpoint(authorizationServerProperties.getIntrospectionEndpoint())
            .build();
    }
}
