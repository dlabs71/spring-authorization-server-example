package ru.dlabs.sas.example.jsso.dao.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dlabs.sas.example.jsso.dao.entity.SystemOauth2Client;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DaoRegisteredClientRepository implements RegisteredClientRepository {

    private final SystemOauth2ClientRepository systemOauth2ClientRepository;

    @Override
    @Transactional
    public void save(RegisteredClient dto) {
        SystemOauth2Client entity = new SystemOauth2Client();
        if (dto.getId() != null) {
            entity = systemOauth2ClientRepository.getReferenceById(Long.parseLong(dto.getId()));
        }
        this.map(dto, entity);
        systemOauth2ClientRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public RegisteredClient findById(String id) {
        SystemOauth2Client entity = systemOauth2ClientRepository.getReferenceById(Long.parseLong(id));
        return this.map(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public RegisteredClient findByClientId(String clientId) {
        SystemOauth2Client entity = systemOauth2ClientRepository.getByClientId(clientId);
        return this.map(entity);
    }

    private RegisteredClient map(SystemOauth2Client entity) {
        return RegisteredClient.withId(entity.getId().toString())
                .clientId(entity.getClientId())
                .clientSecret(entity.getClientSecret())
                .clientIdIssuedAt(entity.getClientIdIssueAt().toInstant(ZoneOffset.UTC))
                .clientSecretExpiresAt(entity.getClientSecretExpiresAt().toInstant(ZoneOffset.UTC))
                .clientName(entity.getClientName())
                .clientAuthenticationMethods(clientAuthenticationMethods -> clientAuthenticationMethods.addAll(entity.clientAuthenticationMethods()))
                .authorizationGrantTypes(authorizationGrantTypes -> authorizationGrantTypes.addAll(entity.authorizationGrantTypes()))
                .redirectUris(redirectUris -> redirectUris.addAll(entity.redirectUris()))
                .scopes(scopes -> scopes.addAll(entity.scopes()))
                .tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE).build())
                .build();
    }

    private void map(RegisteredClient dto, SystemOauth2Client entity) {
        entity.setClientId(dto.getClientId())
                .setClientIdIssueAt(dto.getClientIdIssuedAt() != null ? LocalDateTime.ofInstant(dto.getClientIdIssuedAt(), ZoneOffset.UTC) : null)
                .setClientSecret(dto.getClientSecret())
                .setClientSecretExpiresAt(dto.getClientSecretExpiresAt() != null ? LocalDateTime.ofInstant(dto.getClientSecretExpiresAt(), ZoneOffset.UTC) : null)
                .setClientName(dto.getClientName())
                .setClientAuthenticationMethods(dto.getClientAuthenticationMethods().stream()
                        .map(ClientAuthenticationMethod::getValue)
                        .collect(Collectors.joining(","))
                )
                .setAuthorizationGrantTypes(dto.getAuthorizationGrantTypes().stream()
                        .map(AuthorizationGrantType::getValue)
                        .collect(Collectors.joining(","))
                )
                .setRedirectUris(String.join(",", dto.getRedirectUris()))
                .setScopes(String.join(",", dto.getScopes()));
    }
}
