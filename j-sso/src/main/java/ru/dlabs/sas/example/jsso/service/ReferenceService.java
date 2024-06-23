package ru.dlabs.sas.example.jsso.service;

import java.util.Collection;
import java.util.List;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import ru.dlabs.sas.example.jsso.dto.ReferenceDto;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-05</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public interface ReferenceService {

    List<ReferenceDto<String>> getAuthMethods();

    List<ReferenceDto<String>> getGrantTypes();

    List<ReferenceDto<String>> getScopes();

    String getGrantTypeName(AuthorizationGrantType grantType);

    String getScopeName(String scope);

    List<String> getScopeNames(Collection<String> scopeCodes);
}
