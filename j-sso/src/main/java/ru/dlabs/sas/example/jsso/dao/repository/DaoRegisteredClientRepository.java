package ru.dlabs.sas.example.jsso.dao.repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dlabs.sas.example.jsso.dao.entity.SystemOauth2Client;

@Repository
@RequiredArgsConstructor
public class DaoRegisteredClientRepository implements RegisteredClientRepository {

    private final SystemOAuth2ClientRepository systemOauth2ClientRepository;

    @Override
    @Transactional
    public void save(RegisteredClient dto) {
        SystemOauth2Client entity = new SystemOauth2Client();
        if (dto.getId() != null) {
            entity = systemOauth2ClientRepository.getReferenceById(dto.getId());
        }
        this.map(dto, entity);
        systemOauth2ClientRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public RegisteredClient findById(String id) {
        SystemOauth2Client entity = systemOauth2ClientRepository.getReferenceById(id);
        return this.map(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public RegisteredClient findByClientId(String clientId) {
        SystemOauth2Client entity = systemOauth2ClientRepository.getByClientId(clientId);
        return this.map(entity);
    }

    private RegisteredClient map(SystemOauth2Client entity) {
        return RegisteredClient.withId(entity.getId())
            .id(entity.getId())
            .clientId(entity.getClientId())
            .clientSecret(entity.getClientSecret())
            .clientIdIssuedAt(entity.getCreationDate().toInstant(ZoneOffset.UTC))
            .clientSecretExpiresAt(entity.getClientSecretExpiresAt() != null ?
                                       entity.getClientSecretExpiresAt().toInstant(ZoneOffset.UTC) : null)
            .clientName(entity.getClientName())
            .clientAuthenticationMethods(clientAuthenticationMethods -> clientAuthenticationMethods.addAll(entity.getClientAuthenticationMethods()))
            .authorizationGrantTypes(authorizationGrantTypes -> authorizationGrantTypes.addAll(entity.getAuthorizationGrantTypes()))
            .redirectUris(redirectUris -> redirectUris.addAll(entity.getRedirectUris()))
            .scopes(scopes -> scopes.addAll(entity.getScopes()))
            .tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE).build())
            .build();
    }

    private void map(RegisteredClient dto, SystemOauth2Client entity) {
        entity.setClientId(dto.getClientId());
        entity.setClientSecret(dto.getClientSecret());
        entity.setClientSecretExpiresAt(dto.getClientSecretExpiresAt() != null ?
                                            LocalDateTime.ofInstant(dto.getClientSecretExpiresAt(), ZoneOffset.UTC) :
                                            null);
        entity.setClientName(dto.getClientName());
        entity.setClientAuthenticationMethods(dto.getClientAuthenticationMethods());
        entity.setAuthorizationGrantTypes(dto.getAuthorizationGrantTypes());
        entity.setRedirectUris(dto.getRedirectUris());
        entity.setScopes(dto.getScopes());
    }
}
