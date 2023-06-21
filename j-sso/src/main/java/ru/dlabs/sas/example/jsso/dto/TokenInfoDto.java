package ru.dlabs.sas.example.jsso.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import ru.dlabs.sas.example.jsso.utils.JsonInstantSerializer;

import java.net.URL;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
public class TokenInfoDto {

    private Boolean active;
    private String sub;
    private List<String> aud;
    @JsonSerialize(using = JsonInstantSerializer.class)
    private Instant nbf;
    private List<String> scopes;
    private URL iss;
    @JsonSerialize(using = JsonInstantSerializer.class)
    private Instant exp;
    @JsonSerialize(using = JsonInstantSerializer.class)
    private Instant iat;
    private String jti;
    private String clientId;
    private String tokenType;

    private Object principal;
    private Collection<? extends GrantedAuthority> authorities;
}
