package ru.dlabs.sas.example.jsso.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ChangePasswordService {

    /**
     * Инициализируем смену пароля. Запоминаем введённое значение пользователем и высылаем код подтверждения.
     */
    void init(String newPassword, HttpServletResponse response);

    /**
     * Подтверждаем смену пароля. Меняем пароль в БД
     */
    void confirmChange(String otp, HttpServletRequest request);
}
