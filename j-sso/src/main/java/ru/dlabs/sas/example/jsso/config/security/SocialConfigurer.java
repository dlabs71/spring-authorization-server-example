package ru.dlabs.sas.example.jsso.config.security;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Setter
@Accessors(chain = true, fluent = true)
public class SocialConfigurer extends AbstractHttpConfigurer<SocialConfigurer, HttpSecurity> {

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;
    private AuthenticationFailureHandler failureHandler;
    private AuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.oauth2Login(oauth2Login -> {
            if (this.oAuth2UserService != null) {
                oauth2Login.userInfoEndpoint(customizer -> {
                    customizer.userService(this.oAuth2UserService);
                });
            }
            if (this.successHandler != null) {
                oauth2Login.successHandler(this.successHandler);
            }
            if (this.failureHandler != null) {
                oauth2Login.failureHandler(this.failureHandler);
            }
        });
    }
}
