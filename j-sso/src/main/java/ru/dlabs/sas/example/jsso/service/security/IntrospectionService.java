package ru.dlabs.sas.example.jsso.service.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenIntrospection;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenIntrospectionAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.dlabs.sas.example.jsso.dto.security.AuthorizedUser;
import ru.dlabs.sas.example.jsso.dto.security.IntrospectionPrincipal;
import ru.dlabs.sas.example.jsso.dto.security.TokenInfoDto;
import ru.dlabs.sas.example.jsso.type.SSOScope;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-16</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class IntrospectionService {

    public final static String principalAttributeKey = "java.security.Principal";

    private final OAuth2AuthorizationService oAuth2AuthorizationService;
    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    public void introspectionResponse(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        TokenInfoDto tokenInfoDto = this.createTokenDto((OAuth2TokenIntrospectionAuthenticationToken) authentication);

        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        mappingJackson2HttpMessageConverter.write(
            tokenInfoDto,
            null,
            httpResponse
        );
    }

    public TokenInfoDto createTokenDto(OAuth2TokenIntrospectionAuthenticationToken introspectionAuthenticationToken) {
        TokenInfoDto.TokenInfoDtoBuilder tokenInfoDtoBuilder = TokenInfoDto.builder().active(false);
        if (introspectionAuthenticationToken.getTokenClaims().isActive()) {
            OAuth2TokenIntrospection claims = introspectionAuthenticationToken.getTokenClaims();
            tokenInfoDtoBuilder.active(true)
                .sub(claims.getSubject())
                .aud(claims.getAudience())
                .nbf(claims.getNotBefore())
                .scopes(claims.getScopes())
                .iss(claims.getIssuer())
                .exp(claims.getExpiresAt())
                .iat(claims.getIssuedAt())
                .jti(claims.getId())
                .clientId(claims.getClientId())
                .tokenType(claims.getTokenType());

            this.upgradeDtoByPrincipal(
                claims.getScopes(),
                introspectionAuthenticationToken.getToken(),
                tokenInfoDtoBuilder
            );
        }
        return tokenInfoDtoBuilder.build();
    }

    private void upgradeDtoByPrincipal(
        List<String> clientScopes,
        String accessTokenValue,
        TokenInfoDto.TokenInfoDtoBuilder tokenInfoDtoBuilder
    ) {
        if (clientScopes != null && !clientScopes.isEmpty()) {
            AuthorizedUser authorizedUser = this.extractAuthorizedUserByAccessToken(accessTokenValue);
            IntrospectionPrincipal introspectionPrincipal = this.buildIntrospectionPrincipal(
                authorizedUser,
                clientScopes
            );
            tokenInfoDtoBuilder.principal(introspectionPrincipal);
        }
    }

    private AuthorizedUser extractAuthorizedUserByAccessToken(String accessTokenValue) {
        OAuth2Authorization tokenAuth = oAuth2AuthorizationService.findByToken(
            accessTokenValue,
            OAuth2TokenType.ACCESS_TOKEN
        );
        if (tokenAuth != null) {
            Authentication attributeAuth = tokenAuth.getAttribute(principalAttributeKey);
            if (attributeAuth != null) {
                if (attributeAuth.getPrincipal() instanceof AuthorizedUser authorizedUser) {
                    return authorizedUser;
                } else {
                    throw new RuntimeException(
                        "Principal class = "
                            + attributeAuth.getPrincipal().getClass().getSimpleName()
                            + " is not supported");
                }
            }
        }
        return null;
    }

    private IntrospectionPrincipal buildIntrospectionPrincipal(
        AuthorizedUser authorizedUser,
        List<String> clientScopes
    ) {
        if (authorizedUser == null) {
            return null;
        }

        var builder = IntrospectionPrincipal.builder();
        if (clientScopes.contains(SSOScope.USER_AUTHORITIES.getDatabaseCode())) {
            List<String> authorities = Collections.emptyList();
            if (authorizedUser.getAuthorities() != null) {
                authorities = authorizedUser.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            }
            builder.authorities(authorities);
        }

        if (clientScopes.contains(SSOScope.USER_IDENTIFICATION.getDatabaseCode())) {
            builder.id(authorizedUser.getId())
                .email(authorizedUser.getEmail());
        }

        if (clientScopes.contains(SSOScope.USER_PROFILE_INFO.getDatabaseCode())) {
            builder.firstName(authorizedUser.getFirstName())
                .lastName(authorizedUser.getLastName())
                .middleName(authorizedUser.getMiddleName())
                .birthday(authorizedUser.getBirthday());
        }

        return builder.build();
    }
}
