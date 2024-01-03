package ru.dlabs.sas.example.jsso.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ResetPasswordService {

    /**
     * Инициализация сброса пароля. Указываем email. Если такой email в БД существует отправляем сообщение с OTP.
     */
    void initial(String email, HttpServletResponse response);

    /**
     * Подтверждаем указанный на первом шаге email. Отправляем сообщение со ссылкой на форму сброса пароля.
     */
    void confirmEmail(String otp, HttpServletRequest request);

    /**
     * Меняем пароль для пользователя, email которого указали на первом шаге.
     */
    void setNewPassword(String newPassword, HttpServletRequest request);
}
