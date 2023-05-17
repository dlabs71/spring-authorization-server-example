package ru.dlabs.sas.example.jsso.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import ru.dlabs.sas.example.jsso.service.CustomOAuth2UserService;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        SocialConfigurer socialConfigurer = new SocialConfigurer()
                .oAuth2UserService(customOAuth2UserService);

        http.apply(socialConfigurer);
        http.authorizeHttpRequests(authorize ->
                authorize.anyRequest().authenticated()
        );
        return http.formLogin(withDefaults()).build();
    }
}
