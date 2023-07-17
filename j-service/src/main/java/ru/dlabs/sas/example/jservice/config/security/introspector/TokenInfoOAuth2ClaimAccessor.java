package ru.dlabs.sas.example.jservice.config.security.introspector;

import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimAccessor;
import ru.dlabs.sas.example.jservice.dto.TokenInfoDto;

import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public abstract class TokenInfoOAuth2ClaimAccessor implements OAuth2TokenIntrospectionClaimAccessor {

    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    // добавим абстрактный метод, для получения объекта TokenInfoDto
    abstract TokenInfoDto getTokenInfo();

    @Override
    public boolean isActive() {
        return this.getTokenInfo().getActive();
    }

    @Override
    public String getUsername() {
        return this.getTokenInfo().getSub();
    }

    @Override
    public String getClientId() {
        return this.getTokenInfo().getClientId();
    }

    @Override
    public List<String> getScopes() {
        return this.getTokenInfo().getScopes();
    }

    @Override
    public String getTokenType() {
        return this.getTokenInfo().getTokenType();
    }

    @Override
    public Instant getExpiresAt() {
        return this.getTokenInfo().getExp();
    }

    @Override
    public Instant getIssuedAt() {
        return this.getTokenInfo().getIat();
    }

    @Override
    public Instant getNotBefore() {
        return this.getTokenInfo().getNbf();
    }

    @Override
    public String getSubject() {
        return this.getTokenInfo().getSub();
    }

    @Override
    public List<String> getAudience() {
        return this.getTokenInfo().getAud();
    }

    @Override
    public URL getIssuer() {
        return this.getTokenInfo().getIss();
    }

    @Override
    public String getId() {
        return this.getTokenInfo().getJti();
    }
}
