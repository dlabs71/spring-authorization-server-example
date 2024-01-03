package ru.dlabs.sas.example.jsso.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dlabs.sas.example.jsso.dto.AuthorizedUser;
import ru.dlabs.sas.example.jsso.dto.AuthorizedUserDto;
import ru.dlabs.sas.example.jsso.utils.SecurityUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/security-session")
public class SessionController {

    @GetMapping("/user")
    public AuthorizedUserDto getCurrentUser() {
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        return AuthorizedUserDto.build(authorizedUser);
    }
}
