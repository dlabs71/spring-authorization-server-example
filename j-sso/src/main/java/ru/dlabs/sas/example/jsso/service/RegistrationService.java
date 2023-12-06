package ru.dlabs.sas.example.jsso.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.dlabs.sas.example.jsso.dto.RegistrationDto;

public interface RegistrationService {

    void register(RegistrationDto registrationDto, HttpServletResponse response);

    void checkOtp(String otp, HttpServletRequest request);
}
