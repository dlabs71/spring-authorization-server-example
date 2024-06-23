package ru.dlabs.sas.example.jsso.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dlabs.sas.example.jsso.dto.security.AuthorizedUser;
import ru.dlabs.sas.example.jsso.dto.security.AuthorizedUserDto;
import ru.dlabs.sas.example.jsso.utils.SecurityUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/security-session")
@Tag(name = "Контроллер управления контекстом авторизации пользователя")
public class SessionController {

    @GetMapping("/user")
    @Operation(description = "Получение информации об авторизованном пользователе")
    public AuthorizedUserDto getCurrentUser() {
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        return AuthorizedUserDto.build(authorizedUser);
    }
}
