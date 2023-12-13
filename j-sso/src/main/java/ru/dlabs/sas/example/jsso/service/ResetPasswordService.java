package ru.dlabs.sas.example.jsso.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ResetPasswordService {

    void initial(String email, HttpServletResponse response);

    void confirmEmail(String otp, HttpServletRequest request);

    void setNewPassword(String newPasswordHash, HttpServletRequest request);
}
