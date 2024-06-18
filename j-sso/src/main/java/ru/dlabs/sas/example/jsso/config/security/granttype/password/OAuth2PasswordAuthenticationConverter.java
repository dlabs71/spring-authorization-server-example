package ru.dlabs.sas.example.jsso.config.security.granttype.password;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example </div>
 * <div><strong>Creation date:</strong> 2024-06-17 </div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public class OAuth2PasswordAuthenticationConverter implements AuthenticationConverter {

    private static final String ACCESS_TOKEN_REQUEST_ERROR_URI =
        "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";
    private final static String USERNAME_PARAM = "username";
    private final static String PASSWORD_PARAM = "password";

    @Override
    public Authentication convert(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = this.getFormParameters(request);

        // проверяем параметр grant_type
        String grantType = parameters.getFirst(OAuth2ParameterNames.GRANT_TYPE);
        if (!AuthorizationGrantType.PASSWORD.getValue().equals(grantType)) {
            return null;
        }

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        // получаем параметр username из запроса
        String username = parameters.getFirst(USERNAME_PARAM);
        if (!StringUtils.hasText(username) || parameters.get(USERNAME_PARAM).size() != 1) {
            this.throwError(OAuth2ErrorCodes.INVALID_REQUEST, USERNAME_PARAM, ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        // получаем параметр password из запроса
        String password = parameters.getFirst(PASSWORD_PARAM);
        if (!StringUtils.hasText(password) || parameters.get(PASSWORD_PARAM).size() != 1) {
            this.throwError(OAuth2ErrorCodes.INVALID_REQUEST, PASSWORD_PARAM, ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        // получаем параметр scope из запроса
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            this.throwError(
                OAuth2ErrorCodes.INVALID_REQUEST,
                OAuth2ParameterNames.SCOPE,
                ACCESS_TOKEN_REQUEST_ERROR_URI
            );
        }
        Set<String> requestedScopes = null;
        if (StringUtils.hasText(scope)) {
            requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }

        // дополнительные параметры, если такие существуют
        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                !key.equals(USERNAME_PARAM) &&
                !key.equals(PASSWORD_PARAM) &&
                !key.equals(OAuth2ParameterNames.SCOPE)
            ) {
                additionalParameters.put(key, (value.size() == 1) ? value.get(0) : value.toArray(new String[0]));
            }
        });

        return new OAuth2PasswordAuthenticationToken(
            username,
            password,
            requestedScopes,
            AuthorizationGrantType.PASSWORD,
            clientPrincipal,
            additionalParameters
        );
    }

    /**
     * Получение параметров запроса в виде ассоциативного списка
     * Код заимствован из {@link org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2EndpointUtils}
     */
    private MultiValueMap<String, String> getFormParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameterMap.forEach((key, values) -> {
            String queryString = StringUtils.hasText(request.getQueryString()) ? request.getQueryString() : "";
            // If not query parameter then it's a form parameter
            if (!queryString.contains(key) && values.length > 0) {
                for (String value : values) {
                    parameters.add(key, value);
                }
            }
        });
        return parameters;
    }

    private void throwError(String errorCode, String parameterName, String errorUri) {
        OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, errorUri);
        throw new OAuth2AuthenticationException(error);
    }
}
