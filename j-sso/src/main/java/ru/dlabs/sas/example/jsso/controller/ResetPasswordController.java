package ru.dlabs.sas.example.jsso.controller;

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
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    /**
     * Инициализация смены пароля. Пользователь указываем для какого email нам нужно сменить пароль.
     * Если пароль не найден, то будет выброшено исключение.
     * Если пароль найден, то будет отправлено сообщение с OTP
     */
    @PostMapping("/init")
    public void initResetPassword(@RequestPart("email") String email, HttpServletResponse response) {
        resetPasswordService.initial(email, response);
    }

    /**
     * Подтверждение email. Пользователь присылает OTP код отправленный ему на email в первом endpoint-е.
     * Если подтверждение успешно прошло, то пользователю высылается сообщение со ссылкой на форму смены пароля.
     */
    @PostMapping("/confirm")
    public void confirm(@RequestPart("otp") String otp, HttpServletRequest request) {
        resetPasswordService.confirmEmail(otp, request);
    }

    /**
     * Смена пароля пользователя. В заголовках должен быть указан 'reset-password-session'
     */
    @PostMapping("/set")
    public void setPassword(@RequestPart("password") String password, HttpServletRequest request) {
        resetPasswordService.setNewPassword(password, request);
    }
}
