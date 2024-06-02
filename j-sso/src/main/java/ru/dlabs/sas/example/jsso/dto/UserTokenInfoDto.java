package ru.dlabs.sas.example.jsso.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-10</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenInfoDto {

    private String authorizationId;
    private LocalDateTime startDate;
    private LocalDateTime lastRefreshDate;
    private String clientId;
    private String clientName;
    private List<String> scopeNames;
    private String grantTypeName;
    private String clientRedirectUri;
}
