package ru.dlabs.sas.example.jsso.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import ru.dlabs.sas.example.jsso.service.CustomOAuth2UserService;
import ru.dlabs.sas.example.jsso.service.CustomUserDetailsService;

@EnableWebSecurity(debug = true)
@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    public static final String LOGIN_PAGE = "/login";
    public static final String[] PERMIT_ALL_PATTERNS = {
            LOGIN_PAGE,
            "/static/**"
    };

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomUserDetailsService userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
    private final AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        SocialConfigurer socialConfigurer = new SocialConfigurer()
                .oAuth2UserService(customOAuth2UserService)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .formLogin(LOGIN_PAGE);
        http.apply(socialConfigurer);

        http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder);

        http.authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers(PERMIT_ALL_PATTERNS).permitAll()
                        .anyRequest().authenticated()
        );
        return http.formLogin(configurer -> {
            configurer.loginPage(LOGIN_PAGE)
                    .successHandler(successHandler)
                    .failureHandler(failureHandler);
        }).build();
    }
}
