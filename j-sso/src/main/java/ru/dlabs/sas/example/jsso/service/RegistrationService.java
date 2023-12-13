package ru.dlabs.sas.example.jsso.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.dlabs.sas.example.jsso.dto.RegistrationDto;

public interface RegistrationService {

    /**
     * Сохранение регистрационных данных, генерация OTP, отправка по email OTP.
     */
    void register(RegistrationDto registrationDto, HttpServletResponse response);

    /**
     * Валидация OTP и создание пользователя
     */
    void checkOtp(String otp, HttpServletRequest request);
}
