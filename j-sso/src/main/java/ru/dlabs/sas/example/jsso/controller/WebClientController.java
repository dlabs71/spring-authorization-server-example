package ru.dlabs.sas.example.jsso.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Tag(name = "Контроллер управления WEB клиентом")
public class WebClientController {

    @GetMapping("/client/**")
    @Operation(description = "Endpoint получения WEB клиента (SPA приложение)")
    public String index() {
        return "index";
    }

    @RequestMapping
    @Operation(description = "Обработка запроса на корень приложения")
    public void root(HttpServletResponse response) {
        response.setHeader(HttpHeaders.LOCATION, "/client/");
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
    }

}
