package ru.dlabs.sas.example.jsso.service;

import java.util.List;
import ru.dlabs.sas.example.jsso.dto.UserTokenInfoDto;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-10</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public interface UserTokenService {

    List<UserTokenInfoDto> getUserTokens();

    void recallToken(String authenticationId);

    void recallAllCurrentUserTokens();
}
