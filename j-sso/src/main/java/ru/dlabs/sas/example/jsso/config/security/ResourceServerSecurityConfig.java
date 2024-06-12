package ru.dlabs.sas.example.jsso.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import ru.dlabs.sas.example.jsso.config.security.introspector.CustomSpringTokenIntrospection;
import ru.dlabs.sas.example.jsso.config.security.properties.OAuth2ResourceOpaqueProperties;

/**
 * Конфигурация SecurityFilterChain ресурс сервера SSO. Ресурс сервер SSO предназначен для предоставления информации
 * об аккаунте пользователя.
 * Предназначена для endpoint-ов соответствующих шаблонам из константы {@link RESOURCE_SERVER_PATTERNS}
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class ResourceServerSecurityConfig {

    private final static String[] RESOURCE_SERVER_PATTERNS = new String[]{
            "/resource/**"
    };

    private final OAuth2ResourceOpaqueProperties resourceProperties;
    private final MappingJackson2HttpMessageConverter messageConverter;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher(RESOURCE_SERVER_PATTERNS)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers(RESOURCE_SERVER_PATTERNS))
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                ));

        // Таким образом разрешаем передачу access token в параметрах HTTP запроса
        DefaultBearerTokenResolver tokenResolver = new DefaultBearerTokenResolver();
        tokenResolver.setAllowUriQueryParameter(true);

        http.oauth2ResourceServer(configurer -> {
            configurer.bearerTokenResolver(tokenResolver);

            configurer.opaqueToken(customizer -> {
                customizer.introspector(new CustomSpringTokenIntrospection(
                        resourceProperties.getIntrospectionUri(),
                        resourceProperties.getClientId(),
                        resourceProperties.getClientSecret(),
                        messageConverter
                ));
            });
        });
        return http.build();
    }
}
