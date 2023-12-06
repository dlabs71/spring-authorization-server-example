package ru.dlabs.sas.example.jsso.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dlabs.sas.example.jsso.dto.RegistrationDto;
import ru.dlabs.sas.example.jsso.service.RegistrationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/init")
    public void registerNewUser(@RequestBody RegistrationDto dto, HttpServletResponse response) {
        registrationService.register(dto, response);
    }

    @PostMapping("/confirm")
    public void checkOtp(@RequestParam("otp") String otp, HttpServletRequest request) {
        registrationService.checkOtp(otp, request);
    }
}
