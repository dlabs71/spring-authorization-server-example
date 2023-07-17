package ru.dlabs.sas.example.jsso.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Тестовый контроллер")
public class TestController {

    @GetMapping("/test")
    @PreAuthorize("hasAnyAuthority('GET_OWN_DATA')")
    @Operation(description = "Тестовый метод. Возвращает параметр name из principal объект авторизованного пользователя")
    public String test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
