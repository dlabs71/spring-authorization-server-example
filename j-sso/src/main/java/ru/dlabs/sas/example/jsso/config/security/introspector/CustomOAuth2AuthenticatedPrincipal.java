package ru.dlabs.sas.example.jsso.config.security.introspector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import ru.dlabs.sas.example.jsso.dto.security.TokenInfoDto;
import ru.dlabs.sas.example.jsso.dto.security.AuthorizedUser;

/**
 * Объект principal для Resource Server SSO.
 * {@link AUTHORITY_PREFIX} - необходим так как в Spring Security 6 был убран класс {@link OAuth2MethodSecurityExpressionHandler},
 * который предназначался для поддержки специальной SpEL переменной OAuth2.
 * Теперь чтобы была возможность указать scope как ограничение авторизации на endpoint-е,
 * необходимо добавить префикс SCOPE_ к ней и поместить её в authorities.
 *
 * @see <a href="https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide#simplified-spel">OAuth 2.0 Migration Guide</a>
 */
public class CustomOAuth2AuthenticatedPrincipal extends TokenInfoOAuth2ClaimAccessor
    implements OAuth2AuthenticatedPrincipal, Serializable {

    private static final String AUTHORITY_PREFIX = "SCOPE_";

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
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (this.delegate != null) {
            authorities.addAll(delegate.getAuthorities());
        }
        if (this.tokenInfo != null && this.tokenInfo.getScopes() != null) {
            authorities.addAll(
                this.tokenInfo.getScopes()
                    .stream()
                    .map(item -> new SimpleGrantedAuthority(AUTHORITY_PREFIX + item))
                    .toList()
            );
        }
        return authorities;
    }

    /**
     * Если пришедший токен вне контекста пользователя (Client Credential Grant Type), то возвращаем client_id
     */
    public String getName() {
        if (this.delegate == null) {
            return this.tokenInfo.getClientId();
        }
        return this.delegate.getName();
    }

    @Override
    TokenInfoDto getTokenInfo() {
        return this.tokenInfo;
    }


}

