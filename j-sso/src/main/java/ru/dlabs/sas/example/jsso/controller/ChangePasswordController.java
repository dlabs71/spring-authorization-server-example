package ru.dlabs.sas.example.jsso.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import ru.dlabs.sas.example.jsso.service.ChangePasswordService;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-01</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/change-password")
@Tag(name = "Контроллер изменения пароля авторизованного пользователя")
public class ChangePasswordController {

    private final ChangePasswordService changePasswordService;

    @PostMapping(value = "/init", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("hasAnyAuthority('CHANGE_OWN_PASSWORD')")
    @Operation(description = "Инициализация установления нового пароля. Отсылается OTP код подтверждения")
    public void initChangePassword(@RequestPart("password") String password, HttpServletResponse response) {
        changePasswordService.init(password, response);
    }

    @PostMapping(value = "/confirm", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("hasAnyAuthority('CHANGE_OWN_PASSWORD')")
    @Operation(description = "Подтверждение кода OTP полученного на шаге инициализации. Смена пароля пользователя")
    public void confirmChangePassword(@RequestPart("otp") String otp, HttpServletRequest request) {
        changePasswordService.confirmChange(otp, request);
    }
}
