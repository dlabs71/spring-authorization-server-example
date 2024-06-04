package ru.dlabs.sas.example.jsso.service.impl;

import java.util.Comparator;
import java.util.List;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dlabs.sas.example.jsso.dao.entity.SystemOauth2Client;
import ru.dlabs.sas.example.jsso.dao.repository.SystemOAuth2ClientRepository;
import ru.dlabs.sas.example.jsso.dto.security.AuthorizationInfo;
import ru.dlabs.sas.example.jsso.dto.security.AuthorizedUser;
import ru.dlabs.sas.example.jsso.dto.UserTokenInfoDto;
import ru.dlabs.sas.example.jsso.service.ReferenceService;
import ru.dlabs.sas.example.jsso.service.UserTokenService;
import ru.dlabs.sas.example.jsso.service.security.RedisOAuth2AuthorizationService;
import ru.dlabs.sas.example.jsso.utils.SecurityUtils;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-10</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Service
public class DefaultUserTokenService implements UserTokenService {

    private final RedisOAuth2AuthorizationService authorizationService;
    private final SystemOAuth2ClientRepository systemOauth2ClientRepository;
    private final ReferenceService referenceService;

    public DefaultUserTokenService(
        OAuth2AuthorizationService authorizationService,
        SystemOAuth2ClientRepository systemOauth2ClientRepository,
        ReferenceService referenceService
    ) {
        this.authorizationService = (RedisOAuth2AuthorizationService) authorizationService;
        this.systemOauth2ClientRepository = systemOauth2ClientRepository;
        this.referenceService = referenceService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserTokenInfoDto> getUserTokens() {
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        List<AuthorizationInfo> userTokens = authorizationService.findInfoByUserId(authorizedUser.getId());
        return userTokens.stream()
            .map(this::mapToDto)
            .sorted(Comparator.comparing(UserTokenInfoDto::getLastRefreshDate).reversed())
            .toList();
    }

    public UserTokenInfoDto mapToDto(AuthorizationInfo entity) {
        SystemOauth2Client client = systemOauth2ClientRepository.getByClientId(entity.getClientId());
        AuthorizationGrantType grantType = entity.getAuthorizationGrantType();

        return UserTokenInfoDto.builder()
            .authorizationId(entity.getAuthorizationId())
            .grantTypeName(referenceService.getGrantTypeName(grantType))
            .lastRefreshDate(entity.getLastRefreshDate())
            .startDate(entity.getStartDate())
            .scopeNames(referenceService.getScopeNames(entity.getScopes()))
            .clientName(client.getClientName())
            .clientId(entity.getClientId())
            .clientRedirectUri(entity.getRedirectUri())
            .build();
    }

    @Override
    public void recallToken(String authenticationId) {
        OAuth2Authorization authorization = authorizationService.findById(authenticationId);
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        if (authorization != null && authorization.getPrincipalName().equals(authorizedUser.getUsername())) {
            authorizationService.remove(authenticationId);
        }
    }

    @Override
    public void recallAllCurrentUserTokens() {
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        List<AuthorizationInfo> userTokens = authorizationService.findInfoByUserId(authorizedUser.getId());
        for (AuthorizationInfo authInfo : userTokens) {
            authorizationService.remove(authInfo.getAuthorizationId());
        }
    }
}
