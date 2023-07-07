package ru.dlabs.sas.example.jsso.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

@Configuration(proxyBeanMethods = false)
public class SecurityConfigUtilities {

    @Bean
    public OAuth2AuthorizationService oAuth2AuthorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
