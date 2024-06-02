package ru.dlabs.sas.example.jsso.mapper;

import lombok.experimental.UtilityClass;
import ru.dlabs.sas.example.jsso.dao.entity.SystemOauth2Client;
import ru.dlabs.sas.example.jsso.dto.OAuth2ClientDto;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-02</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@UtilityClass
public class OAuth2ClientMapper {

    public OAuth2ClientDto map(SystemOauth2Client entity) {
        return OAuth2ClientDto.builder()
            .clientId(entity.getClientId())
            .clientSecret(entity.getClientSecret())
            .clientName(entity.getClientName())
            .clientSecretExpiresAt(
                entity.getClientSecretExpiresAt() != null ? entity.getClientSecretExpiresAt().toLocalDate() : null
            )
            .clientAuthenticationMethods(entity.getClientAuthenticationMethods())
            .authorizationGrantTypes(entity.getAuthorizationGrantTypes())
            .redirectUris(entity.getRedirectUris())
            .scopes(entity.getScopes())
            .deleteNotifyUris(entity.getDeleteNotifyUris())
            .build();
    }
}
