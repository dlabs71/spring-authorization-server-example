package ru.dlabs.sas.example.jsso.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.authorizationserver")
public class AuthorizationServerProperties {

    private String issuerUrl;
    private String introspectionEndpoint;
    private String authenticationSuccessUrl;
    private String customHandlerHeaderName;
    private String resetPasswordEndpoint;
}
