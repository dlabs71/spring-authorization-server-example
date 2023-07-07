package ru.dlabs.sas.example.jservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TokenInfoDto {

    private Boolean active;
    private String sub;
    private List<String> aud;
    private Instant nbf;
    private List<String> scopes;
    private URL iss;
    private Instant exp;
    private Instant iat;
    private String jti;
    private String clientId;
    private String tokenType;

    private IntrospectionPrincipal principal;
}
