package ru.dlabs.sas.example.jsso.config.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import ru.dlabs.sas.example.jsso.config.security.handler.CustomAuthenticationSuccessHandler;
import ru.dlabs.sas.example.jsso.service.CustomOAuth2UserService;
import ru.dlabs.sas.example.jsso.service.CustomUserDetailsService;

@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    public static final String LOGIN_PAGE = "/client/auth/login";
    public static final String LOGIN_PROCESSING_URL = "/login";
    public static final String[] PERMIT_ALL_PATTERNS = {
            LOGIN_PAGE,
            "/error**",
            "/static/**",
            "/client/**",
            "/registration/**",
            "/reset-password/**"
    };

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomUserDetailsService userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationServerProperties authorizationServerProperties;

    // handlers
    private AuthenticationSuccessHandler oAuth2successHandler;
    private AuthenticationSuccessHandler loginRequestSuccessHandler;
    private AuthenticationFailureHandler failureHandler;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        SocialConfigurer socialConfigurer = new SocialConfigurer()
            .oAuth2UserService(customOAuth2UserService)
            .successHandler(oAuth2successHandler)
            .failureHandler(failureHandler)
            .formLogin(LOGIN_PAGE);
        http.apply(socialConfigurer);
        http.csrf(AbstractHttpConfigurer::disable);

        http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailService)
            .passwordEncoder(passwordEncoder);

        http.authorizeHttpRequests(authorize ->
                                       authorize
                                           .requestMatchers(PERMIT_ALL_PATTERNS).permitAll()
                                           .anyRequest().authenticated()
        );

        // Вместо редиректа на страницу логина при anonymous сессии, будем отдавать 401 ошибку.
        // Это необходимо для корректной обработки негативных сценариев в клиентском приложении j-sso
        http.exceptionHandling(configurer -> {
            configurer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        });
        return http.formLogin(configurer -> {
            configurer.loginPage(LOGIN_PAGE)
                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                .successHandler(loginRequestSuccessHandler)
                .failureHandler(failureHandler);
        }).build();
    }

    @PostConstruct
    private void initializeHandlers() {
        // создаём кастомный AuthenticationSuccessHandler для формы логина
        this.loginRequestSuccessHandler = new CustomAuthenticationSuccessHandler(
            authorizationServerProperties.getAuthenticationSuccessUrl(),
            authorizationServerProperties.getCustomHandlerHeaderName()
        );

        // указываем стандартный AuthenticationSuccessHandler для OAuth2 Client
        SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl(authorizationServerProperties.getAuthenticationSuccessUrl());
        this.oAuth2successHandler = handler;

        this.failureHandler = new SimpleUrlAuthenticationFailureHandler();
    }
}
