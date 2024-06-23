package ru.dlabs.sas.example.jsso.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import ru.dlabs.sas.example.jsso.dao.entity.common.VersionedBusinessEntity;

@Getter
@Setter
@Entity
@Table(schema = "sso", name = "system_oauth2_clients_v2")
public class SystemOauth2Client extends VersionedBusinessEntity<String> {

    @Id
    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "client_secret_expires_at")
    private LocalDateTime clientSecretExpiresAt;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "client_authentication_methods", nullable = false, columnDefinition = "text[]")
    private String[] clientAuthenticationMethods;

    @Column(name = "authorization_grant_types", nullable = false, columnDefinition = "text[]")
    private String[] authorizationGrantTypes;

    @Column(name = "redirect_uris", columnDefinition = "text[]")
    private String[] redirectUris;

    @Column(name = "scopes", nullable = false, columnDefinition = "text[]")
    private String[] scopes;

    @Column(name = "delete_notify_uris", columnDefinition = "text[]")
    private String[] deleteNotifyUris;

    public SystemOauth2Client() {
    }

    @Override
    public String getId() {
        return this.getClientId();
    }

    @Override
    public void setId(String id) {
        this.setClientId(id);
    }

    public Set<ClientAuthenticationMethod> getClientAuthenticationMethods() {
        if (this.clientAuthenticationMethods == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(this.clientAuthenticationMethods)
            .map(item -> new ClientAuthenticationMethod(item.trim()))
            .collect(Collectors.toSet());
    }

    public Set<AuthorizationGrantType> getAuthorizationGrantTypes() {
        if (this.authorizationGrantTypes == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(this.authorizationGrantTypes)
            .map(item -> new AuthorizationGrantType(item.trim()))
            .collect(Collectors.toSet());
    }

    public Set<String> getRedirectUris() {
        if (this.redirectUris == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(this.redirectUris)
            .map(String::trim)
            .collect(Collectors.toSet());
    }

    public Set<String> getScopes() {
        if (this.scopes == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(this.scopes)
            .map(String::trim)
            .collect(Collectors.toSet());
    }

    public Set<String> getDeleteNotifyUris() {
        if (this.deleteNotifyUris == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(this.deleteNotifyUris)
            .map(String::trim)
            .collect(Collectors.toSet());
    }

    public void setClientAuthenticationMethods(Collection<ClientAuthenticationMethod> authMethods) {
        if (authMethods == null || authMethods.isEmpty()) {
            this.clientAuthenticationMethods = null;
        } else {
            this.clientAuthenticationMethods = authMethods.stream()
                .map(ClientAuthenticationMethod::getValue)
                .toArray(String[]::new);
        }
    }

    public void setAuthorizationGrantTypes(Collection<AuthorizationGrantType> grantTyps) {
        if (grantTyps == null || grantTyps.isEmpty()) {
            this.authorizationGrantTypes = null;
        } else {
            this.authorizationGrantTypes = grantTyps.stream()
                .map(AuthorizationGrantType::getValue)
                .toArray(String[]::new);
        }
    }

    public void setRedirectUris(Collection<String> redirectUris) {
        if (redirectUris == null || redirectUris.isEmpty()) {
            this.redirectUris = null;
        } else {
            this.redirectUris = redirectUris.toArray(String[]::new);
        }
    }

    public void setScopes(Collection<String> scopes) {
        if (scopes == null || scopes.isEmpty()) {
            this.scopes = null;
        } else {
            this.scopes = scopes.toArray(String[]::new);
        }
    }

    public void setDeleteNotifyUris(Collection<String> deleteNotifyUris) {
        if (deleteNotifyUris == null || deleteNotifyUris.isEmpty()) {
            this.deleteNotifyUris = null;
        } else {
            this.deleteNotifyUris = deleteNotifyUris.toArray(String[]::new);
        }
    }
}
