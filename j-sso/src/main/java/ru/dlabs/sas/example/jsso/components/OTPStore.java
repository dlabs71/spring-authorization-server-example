package ru.dlabs.sas.example.jsso.components;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface OTPStore {

    /**
     * Генерация OTP
     */
    GenerationResult generate(HttpServletResponse response);

    /**
     * Валидация OTP
     */
    boolean validate(String otp, HttpServletRequest request);

    /**
     * Получение session Id из HttpServletRequest
     */
    String getSessionId(HttpServletRequest request);

    /**
     * Возвращает актуальную конфигурацию
     */
    Config getConfig();

    /**
     * Результат генерации OTP. Содержит созданную ID сессии и сам OTP.
     */
    record GenerationResult(String sessionId, String otp) { }

    /**
     * Конфигурация стора
     *
     * @param cookieName   наименование cookie
     * @param cookieDomain домен cookie
     * @param cookieMaxAge время жизни куки cookie и самого OTP
     */
    record Config(String cookieName, String cookieDomain, int cookieMaxAge) { }
}
