package ru.dlabs.sas.example.jsso.components;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface OTPStore {

    String cookieName = "dl-sso-temporary-session";
    String domain = "localhost";
    Integer maxAge = 180;

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

    record GenerationResult(String sessionId, String otp) {

    }
}
