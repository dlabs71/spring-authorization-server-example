package ru.dlabs.sas.example.jsso.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(schema = "sso", name = "system_oauth2_clients")
@SequenceGenerator(schema = "sso", name = "system_oauth2_clients_s", sequenceName = "sso.system_oauth2_clients_s", allocationSize = 1)
public class SystemOauth2Client {

    @Id
    @Column(name = "system_client_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "system_oauth2_clients_s")
    private Long id;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "client_id_issued_at", nullable = false)
    private LocalDateTime clientIdIssueAt;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "client_secret_expires_at")
    private LocalDateTime clientSecretExpiresAt;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "client_authentication_methods", nullable = false)
    private String clientAuthenticationMethods;

    @Column(name = "authorization_grant_types", nullable = false)
    private String authorizationGrantTypes;

    @Column(name = "redirect_uris")
    private String redirectUris;

    @Column(name = "scopes", nullable = false)
    private String scopes;

    @Column(name = "client_settings", nullable = false)
    private String clientSettings;

    @Column(name = "token_settings", nullable = false)
    private String tokenSettings;

    public SystemOauth2Client() {
    }

    @Transient
    public Set<ClientAuthenticationMethod> clientAuthenticationMethods() {
        if (this.clientAuthenticationMethods == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(this.clientAuthenticationMethods.split(","))
                .map(item -> new ClientAuthenticationMethod(item.trim()))
                .collect(Collectors.toSet());
    }

    @Transient
    public Set<AuthorizationGrantType> authorizationGrantTypes() {
        if (this.authorizationGrantTypes == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(this.authorizationGrantTypes.split(","))
                .map(item -> new AuthorizationGrantType(item.trim()))
                .collect(Collectors.toSet());
    }

    @Transient
    public Set<String> redirectUris() {
        if (this.redirectUris == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(this.redirectUris.split(","))
                .collect(Collectors.toSet());
    }

    @Transient
    public Set<String> scopes() {
        if (this.scopes == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(this.scopes.split(","))
                .collect(Collectors.toSet());
    }
}
