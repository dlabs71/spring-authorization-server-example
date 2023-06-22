package ru.dlabs.sas.example.jservice.config.introspector;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import ru.dlabs.sas.example.jservice.dto.AuthorizedUser;
import ru.dlabs.sas.example.jservice.dto.TokenInfoDto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOAuth2AuthenticatedPrincipal extends TokenInfoOAuth2ClaimAccessor implements OAuth2AuthenticatedPrincipal, Serializable {

    private final AuthorizedUser delegate;
    private final TokenInfoDto tokenInfo;

    public CustomOAuth2AuthenticatedPrincipal(TokenInfoDto tokenInfo) {
        this.delegate = AuthorizedUser.build(tokenInfo.getPrincipal());
        this.tokenInfo = tokenInfo;
    }

    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.delegate.getAuthorities();
    }

    public String getName() {
        return this.delegate.getName();
    }

    @Override
    TokenInfoDto getTokenInfo() {
        return this.tokenInfo;
    }
}

