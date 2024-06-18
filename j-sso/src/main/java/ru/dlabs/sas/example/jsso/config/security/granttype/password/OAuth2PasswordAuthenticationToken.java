package ru.dlabs.sas.example.jsso.config.security.granttype.password;

import java.util.Map;
import java.util.Set;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example </div>
 * <div><strong>Creation date:</strong> 2024-06-17 </div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Getter
public class OAuth2PasswordAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    private final String username;
    private final String password;
    private final Set<String> scopes;


    protected OAuth2PasswordAuthenticationToken(
        String username,
        String password,
        Set<String> scopes,
        AuthorizationGrantType authorizationGrantType,
        Authentication clientPrincipal,
        Map<String, Object> additionalParameters
    ) {
        super(authorizationGrantType, clientPrincipal, additionalParameters);
        this.username = username;
        this.password = password;
        this.scopes = scopes;
    }
}
