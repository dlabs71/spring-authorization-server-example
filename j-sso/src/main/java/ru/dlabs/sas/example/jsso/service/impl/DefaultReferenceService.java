package ru.dlabs.sas.example.jsso.service.impl;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Service;
import ru.dlabs.sas.example.jsso.dao.entity.ScopeVWEntity;
import ru.dlabs.sas.example.jsso.dao.repository.ScopeVWRepository;
import ru.dlabs.sas.example.jsso.dto.ReferenceDto;
import ru.dlabs.sas.example.jsso.service.MessageService;
import ru.dlabs.sas.example.jsso.service.ReferenceService;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-05</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class DefaultReferenceService implements ReferenceService {

    private final MessageService messageService;
    private final ScopeVWRepository scopeVWRepository;

    @Override
    public List<ReferenceDto<String>> getAuthMethods() {
        String clientSecretBasicValue = ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue();
        String clientSecretPostValue = ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue();
        String clientSecretJwtValue = ClientAuthenticationMethod.CLIENT_SECRET_JWT.getValue();
        return List.of(
            new ReferenceDto<>(
                clientSecretBasicValue,
                messageService.getMessage("auth-methods." + clientSecretBasicValue)
            ),
            new ReferenceDto<>(
                clientSecretPostValue,
                messageService.getMessage("auth-methods." + clientSecretPostValue)
            ),
            new ReferenceDto<>(
                clientSecretJwtValue,
                messageService.getMessage("auth-methods." + clientSecretJwtValue)
            )
        );
    }

    @Override
    public List<ReferenceDto<String>> getGrantTypes() {
        String authorizationCode = AuthorizationGrantType.AUTHORIZATION_CODE.getValue();
        String refreshTokenValue = AuthorizationGrantType.REFRESH_TOKEN.getValue();
        String clientCredentialsValue = AuthorizationGrantType.CLIENT_CREDENTIALS.getValue();
        String passwordValue = AuthorizationGrantType.PASSWORD.getValue();
        String jwtBearerValue = AuthorizationGrantType.JWT_BEARER.getValue();
        String deviceCodeValue = AuthorizationGrantType.DEVICE_CODE.getValue();
        return List.of(
            new ReferenceDto<>(
                authorizationCode,
                messageService.getMessage("grant-types." + authorizationCode)
            ),
            new ReferenceDto<>(
                refreshTokenValue,
                messageService.getMessage("grant-types." + refreshTokenValue)
            ),
            new ReferenceDto<>(
                clientCredentialsValue,
                messageService.getMessage("grant-types." + clientCredentialsValue)
            ),
            new ReferenceDto<>(
                passwordValue,
                messageService.getMessage("grant-types." + passwordValue)
            ),
            new ReferenceDto<>(
                jwtBearerValue,
                messageService.getMessage("grant-types.jwt_bearer")
            ),
            new ReferenceDto<>(
                deviceCodeValue,
                messageService.getMessage("grant-types.device_code")
            )
        );
    }

    @Override
    public List<ReferenceDto<String>> getScopes() {
        return scopeVWRepository.findAll().stream().map(item -> new ReferenceDto<>(
            item.getUniqueCode(),
            item.getDescription()
        )).toList();
    }

    @Override
    public String getGrantTypeName(AuthorizationGrantType grantType) {
        if (AuthorizationGrantType.JWT_BEARER.equals(grantType)) {
            return messageService.getMessage("grant-types.jwt_bearer");
        }
        if (AuthorizationGrantType.DEVICE_CODE.equals(grantType)) {
            return messageService.getMessage("grant-types.device_code");
        }
        return messageService.getMessage("grant-types." + grantType.getValue());
    }

    @Override
    public String getScopeName(String scopeCode) {
        ScopeVWEntity entity = scopeVWRepository.findByUniqueCode(scopeCode);
        if (entity != null) {
            return entity.getDescription();
        }
        return null;
    }

    @Override
    public List<String> getScopeNames(Collection<String> scopeCodes) {
        return scopeVWRepository.findAllByUniqueCodeIn(scopeCodes)
            .stream()
            .map(ScopeVWEntity::getDescription)
            .toList();
    }
}
