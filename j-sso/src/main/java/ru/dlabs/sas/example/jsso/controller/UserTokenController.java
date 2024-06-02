package ru.dlabs.sas.example.jsso.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dlabs.sas.example.jsso.dto.UserTokenInfoDto;
import ru.dlabs.sas.example.jsso.service.UserTokenService;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-10</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user-token")
@Tag(name = "Контроллер управления выданными токенами пользователя")
public class UserTokenController {

    private final UserTokenService userTokenService;

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('GET_OWN_TOKENS')")
    @Operation(description = "Получение списка выданных токенов текущего пользователя")
    public List<UserTokenInfoDto> searchUserTokens() {
        return userTokenService.getUserTokens();
    }

    @PostMapping("/recall")
    @PreAuthorize("hasAnyAuthority('RECALL_OWN_TOKENS')")
    @Operation(description = "Отозвать токен по его идентификатору")
    public void recallToken(@RequestParam String authorizationId) {
        userTokenService.recallToken(authorizationId);
    }
}
