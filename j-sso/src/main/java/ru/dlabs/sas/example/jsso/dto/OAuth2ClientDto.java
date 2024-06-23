package ru.dlabs.sas.example.jsso.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import ru.dlabs.sas.example.jsso.dto.converters.AuthMethodJsonDeserializer;
import ru.dlabs.sas.example.jsso.dto.converters.AuthMethodJsonSerializer;
import ru.dlabs.sas.example.jsso.dto.converters.GrantTypeJsonDeserializer;
import ru.dlabs.sas.example.jsso.dto.converters.GrantTypeJsonSerializer;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-02</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2ClientDto {

    @NotNull
    private String clientId;
    private String clientSecret;
    private LocalDate clientSecretExpiresAt;
    private String clientName;

    @JsonSerialize(contentUsing = AuthMethodJsonSerializer.class)
    @JsonDeserialize(contentUsing = AuthMethodJsonDeserializer.class)
    private Set<ClientAuthenticationMethod> clientAuthenticationMethods;

    @JsonSerialize(contentUsing = GrantTypeJsonSerializer.class)
    @JsonDeserialize(contentUsing = GrantTypeJsonDeserializer.class)
    private Set<AuthorizationGrantType> authorizationGrantTypes;
    private Set<String> redirectUris;
    private Set<String> scopes;
    private Set<String> deleteNotifyUris;
}
