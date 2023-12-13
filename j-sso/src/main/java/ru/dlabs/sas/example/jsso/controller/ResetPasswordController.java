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

    @PostMapping("/init")
    public void initResetPassword(@RequestPart("email") String email, HttpServletResponse response) {
        resetPasswordService.initial(email, response);
    }

    @PostMapping("/confirm")
    public void confirm(@RequestPart("otp") String otp, HttpServletRequest request) {
        resetPasswordService.confirmEmail(otp, request);
    }

    @PostMapping("/set")
    public void setPassword(@RequestPart("password") String password, HttpServletRequest request) {
        resetPasswordService.setNewPassword(password, request);
    }
}
