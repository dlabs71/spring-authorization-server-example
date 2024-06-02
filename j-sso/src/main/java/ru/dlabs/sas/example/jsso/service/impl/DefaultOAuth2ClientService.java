package ru.dlabs.sas.example.jsso.service.impl;

import jakarta.persistence.criteria.Predicate;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.dlabs.sas.example.jsso.dao.entity.SystemOauth2Client;
import ru.dlabs.sas.example.jsso.dao.repository.SystemOAuth2ClientRepository;
import ru.dlabs.sas.example.jsso.dto.OAuth2ClientDto;
import ru.dlabs.sas.example.jsso.dto.PageableResponseDto;
import ru.dlabs.sas.example.jsso.exception.ServiceException;
import ru.dlabs.sas.example.jsso.mapper.OAuth2ClientMapper;
import ru.dlabs.sas.example.jsso.service.MessageService;
import ru.dlabs.sas.example.jsso.service.OAuth2ClientService;
import ru.dlabs.sas.example.jsso.service.UserClientService;
import ru.dlabs.sas.example.jsso.utils.CryptoUtils;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-02</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultOAuth2ClientService implements OAuth2ClientService {

    private final SystemOAuth2ClientRepository systemOAuth2ClientsRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestClient serviceRestClient;
    private final UserClientService userClientService;
    private final MessageService messageService;

    @Override
    @Transactional(readOnly = true)
    public OAuth2ClientDto getByClientId(String clientId) {
        SystemOauth2Client entity = systemOAuth2ClientsRepository.getReferenceById(clientId);
        return OAuth2ClientMapper.map(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponseDto<OAuth2ClientDto> searchClients(
        int page,
        int pageSize,
        String clientId,
        String clientName
    ) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "creationDate"));
        Specification<SystemOauth2Client> specification = this.getSearchSpecification(clientId, clientName);
        Page<SystemOauth2Client> entitiesPage = systemOAuth2ClientsRepository.findAll(specification, pageRequest);
        List<OAuth2ClientDto> dtoList = entitiesPage.get().map(OAuth2ClientMapper::map).toList();

        return PageableResponseDto.build(
            dtoList,
            page < entitiesPage.getTotalPages(),
            entitiesPage.getTotalElements()
        );
    }

    private Specification<SystemOauth2Client> getSearchSpecification(String clientId, String clientName) {
        return (root, query, builder) -> {
            Predicate predicate = builder.equal(builder.literal("1"), builder.literal("1"));

            if (StringUtils.isNotEmpty(clientId)) {
                predicate = builder.and(builder.like(
                    builder.lower(root.get("clientId")),
                    "%" + clientId.toLowerCase() + "%"
                ));
            }

            if (StringUtils.isNotEmpty(clientName)) {
                predicate = builder.and(builder.like(
                    builder.lower(root.get("clientName")),
                    "%" + clientName.toLowerCase() + "%"
                ));
            }
            return predicate;
        };
    }

    @Override
    @Transactional
    public OAuth2ClientDto save(OAuth2ClientDto dto) {
        SystemOauth2Client entity = systemOAuth2ClientsRepository.findById(dto.getClientId())
            .orElse(new SystemOauth2Client());
        if (entity.getClientId() == null) {
            entity.setClientId(dto.getClientId());
        }
        entity.setClientName(dto.getClientName());
        entity.setClientSecretExpiresAt(
            dto.getClientSecretExpiresAt() != null ? dto.getClientSecretExpiresAt().atTime(LocalTime.MIN) : null
        );
        entity.setClientAuthenticationMethods(dto.getClientAuthenticationMethods());
        entity.setAuthorizationGrantTypes(dto.getAuthorizationGrantTypes());
        entity.setRedirectUris(dto.getRedirectUris());
        entity.setScopes(dto.getScopes());
        entity.setDeleteNotifyUris(dto.getDeleteNotifyUris());

        entity = systemOAuth2ClientsRepository.save(entity);
        return OAuth2ClientMapper.map(entity);
    }

    @Override
    @Transactional
    public String generateSecret(String clientId) {
        Optional<SystemOauth2Client> entityWrapper = systemOAuth2ClientsRepository.findById(clientId);
        if (entityWrapper.isEmpty()) {
            throw ServiceException.builder(messageService.getMessage("oauth.client.not.found", clientId)).build();
        }

        SystemOauth2Client entity = entityWrapper.get();
        String clientSecret = this.generateClientSecret();
        String encodedClientSecret = this.passwordEncoder.encode(clientSecret);
        entity.setClientSecret(encodedClientSecret);

        systemOAuth2ClientsRepository.save(entity);
        return clientSecret;
    }

    @Override
    @Transactional
    public void delete(String clientId) {
        systemOAuth2ClientsRepository.deleteById(clientId);
    }

    @Override
    @Transactional
    public void notifyClientsAndClear() {
        Map<String, Set<UUID>> clientId2UserId = userClientService.getDeletedClientsAndUserIds();
        if (clientId2UserId.isEmpty()) {
            return;
        }
        clientId2UserId.forEach(this::notifyClientForDeleteAccount);
        userClientService.deleteRowsMarkedDeleted();
    }

    @Override
    @Transactional(readOnly = true)
    public void notifyClientForDeleteAccount(String clientId, Set<UUID> userIds) {
        Optional<SystemOauth2Client> entityWrapper = systemOAuth2ClientsRepository.findById(clientId);
        if (entityWrapper.isEmpty()) {
            return;
        }

        SystemOauth2Client client = entityWrapper.get();
        if (client.getDeleteNotifyUris() == null || client.getDeleteNotifyUris().isEmpty()) {
            return;
        }

        for (String uri : client.getDeleteNotifyUris()) {
            URI createdUri = UriComponentsBuilder
                .fromHttpUrl(uri)
                .queryParam("userIds", userIds)
                .build()
                .toUri();
            serviceRestClient.post()
                .uri(createdUri)
                .retrieve()
                .onStatus(status -> status.value() != 200, (request, response) -> {
                    log.warn("Request to client = "
                                 + clientId + " by uri = "
                                 + uri + " returned response status = " + response.getStatusCode());
                });
        }
    }

    private String generateClientSecret() {
        UUID uuid = UUID.randomUUID();
        String salt = RandomStringUtils.randomAlphanumeric(8);
        return new String(Hex.encode(CryptoUtils.pbkdf(
            uuid.toString(),
            salt.getBytes(StandardCharsets.UTF_8),
            256,
            2048
        )));
    }
}
