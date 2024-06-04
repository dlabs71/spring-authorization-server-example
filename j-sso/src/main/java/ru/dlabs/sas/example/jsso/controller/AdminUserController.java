package ru.dlabs.sas.example.jsso.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dlabs.sas.example.jsso.dto.AdminUserDto;
import ru.dlabs.sas.example.jsso.dto.PageableResponseDto;
import ru.dlabs.sas.example.jsso.service.AdminUserService;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-09</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin-user")
@Tag(name = "Контроллер управления администраторами SSO")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('GET_ADMIN_USER_DATA')")
    @Operation(description = "Поиск по администраторам SSO")
    public PageableResponseDto<AdminUserDto> searchUsers(
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
        @RequestParam(value = "email", required = false) String email
    ) {
        return adminUserService.searchUsers(page, pageSize, email);
    }

    @PostMapping("/assign-admin")
    @PreAuthorize("hasAnyAuthority('ASSIGN_ADMIN_ROLE')")
    @Operation(description = "Назначить роль администратора SSO пользователю с указанным email")
    public void assignAdminRole(@RequestParam String email) {
        adminUserService.assignAdmin(email);
    }

    @PostMapping("/dismiss/{userId}")
    @PreAuthorize("hasAnyAuthority('ASSIGN_ADMIN_ROLE')")
    @Operation(description = "Снять роль администратора SSO с пользователя с указанным ID")
    public void dismissAdmin(@PathVariable UUID userId) {
        adminUserService.dismissAdmin(userId);
    }

    @GetMapping(value = "/avatar/{userId}")
    @PreAuthorize("hasAnyAuthority('GET_ADMIN_USER_DATA')")
    @Operation(description = "Получить аватарку пользователя-администратора")
    public ResponseEntity<byte[]> downloadAdminAvatar(@PathVariable UUID userId) {
        return adminUserService.getAvatar(userId);
    }
}
