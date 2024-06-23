package ru.dlabs.sas.example.jsso.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import ru.dlabs.sas.example.jsso.service.ResetPasswordService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reset-password")
@Tag(name = "Контроллер функции 'Забыли пароль'")
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @PostMapping("/init")
    @Tag(name = "Этап инициализации сброса пароля. Отправка OTP кода подтверждения email")
    public void initResetPassword(@RequestPart("email") String email, HttpServletResponse response) {
        resetPasswordService.initial(email, response);
    }

    @PostMapping("/confirm")
    @Tag(name = "Этап подтверждения OTP кода, полученного на этапе инициализации")
    public void confirm(@RequestPart("otp") String otp, HttpServletRequest request) {
        resetPasswordService.confirmEmail(otp, request);
    }

    @PostMapping("/set")
    @Tag(name = "Получение нового пароля и установление его для пользователя")
    public void setPassword(@RequestPart("password") String password, HttpServletRequest request) {
        resetPasswordService.setNewPassword(password, request);
    }
}
