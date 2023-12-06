package ru.dlabs.sas.example.jsso.components;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface OTPStore {

    String cookieName = "dl-sso-temporary-session";
    String domain = "localhost";
    Integer maxAge = 180;

    GenerationResult generate(HttpServletResponse response);

    boolean validate(String otp, HttpServletRequest request);

    String getSessionId(HttpServletRequest request);

    record GenerationResult(String sessionId, String otp) {

    }
}
