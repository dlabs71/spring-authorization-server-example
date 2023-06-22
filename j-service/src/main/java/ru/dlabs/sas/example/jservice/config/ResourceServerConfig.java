package ru.dlabs.sas.example.jservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import ru.dlabs.sas.example.jservice.config.introspector.CustomSpringTokenIntrospection;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class ResourceServerConfig {

    private final OAuth2ResourceOpaqueProperties resourceProperties;
    private final MappingJackson2HttpMessageConverter messageConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // выключаем поддержку сессий
        http.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(customizer -> {
                    customizer.anyRequest().authenticated();
                });

        // подключаем поддержку OAuth2 Resource Server с Opaque Token.
        // добавляем в качестве introspector нашу реализацию
        http.oauth2ResourceServer(configurer -> {
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
