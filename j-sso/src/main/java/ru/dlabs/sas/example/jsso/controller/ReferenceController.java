package ru.dlabs.sas.example.jsso.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dlabs.sas.example.jsso.dto.ReferenceDto;
import ru.dlabs.sas.example.jsso.service.ReferenceService;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-05</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/reference")
@Tag(name = "Контроллер получения справочников")
public class ReferenceController {

    private final ReferenceService referenceService;

    @GetMapping("/auth-methods")
    @Operation(description = "Получения методов авторизации OAuth2 клиентов")
    public List<ReferenceDto<String>> getAuthMethods() {
        return referenceService.getAuthMethods();
    }

    @GetMapping("/grant-types")
    @Operation(description = "Получение доступных grant type OAuth2 клиентов")
    public List<ReferenceDto<String>> getGrantTypes() {
        return referenceService.getGrantTypes();
    }

    @GetMapping("/scopes")
    @Operation(description = "Получение доступных scopes OAuth2 клиентов")
    public List<ReferenceDto<String>> getScopes() {
        return referenceService.getScopes();
    }
}
