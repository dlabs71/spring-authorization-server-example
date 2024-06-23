package ru.dlabs.sas.example.jsso.service.impl;

import com.google.common.collect.ImmutableMap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dlabs.sas.example.jsso.components.OTPStore;
import ru.dlabs.sas.example.jsso.components.RegistrationStore;
import ru.dlabs.sas.example.jsso.dto.RegistrationDto;
import ru.dlabs.sas.example.jsso.exception.InformationException;
import ru.dlabs.sas.example.jsso.exception.RegistrationException;
import ru.dlabs.sas.example.jsso.service.MessageService;
import ru.dlabs.sas.example.jsso.service.RegistrationService;
import ru.dlabs.sas.example.jsso.service.UserService;
import ru.dlabs71.library.email.DEmailSender;

@Service
@RequiredArgsConstructor
public class DefaultRegistrationService implements RegistrationService {

    private final UserService userService;
    private final DEmailSender emailSender;
    private final OTPStore otpStore;
    private final RegistrationStore registrationStore;
    private final MessageService messageService;

    @Override
    public void register(RegistrationDto registrationDto, HttpServletResponse response) {
        // проверяем что пользователь с таким email ещё не существует
        if (userService.existByEmail(registrationDto.getEmail())) {
            throw InformationException.builder("$account.already.exist").build();
        }

        // Создаём OTP
        OTPStore.GenerationResult generationResult = otpStore.generate(response);

        // Сохраняем данные во временное хранилище
        try {
            registrationStore.save(registrationDto, generationResult.sessionId());
        } catch (Exception e) {
            throw InformationException.builder("$happened.unexpected.error").build();
        }

        // отправляем OTP по email
        emailSender.sendHtmlTemplated(
            registrationDto.getEmail(),
            messageService.getMessage("email.subject.confirm.registration"),
            "classpath:mail-templates/registration-confirmed.html",
            ImmutableMap.<String, Object>builder()
                .put("firstName", registrationDto.getFirstName())
                .put("otp", generationResult.otp())
                .build()
        );
    }

    @Override
    @Transactional
    public void checkOtp(String otp, HttpServletRequest request) {
        if (!otpStore.validate(otp, request)) {
            throw new RegistrationException("$opt.incorrect");
        }

        String sessionId = otpStore.getSessionId(request);
        RegistrationDto registrationDto;
        try {
            registrationDto = registrationStore.take(sessionId);
        } catch (Exception e) {
            throw InformationException.builder("$happened.unexpected.error").build();
        }
        userService.saveAndActivateUser(registrationDto);
    }
}
