package ru.dlabs.sas.example.jsso.service.impl;

import com.google.common.collect.ImmutableMap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.dlabs.sas.example.jsso.components.ConfirmationStore;
import ru.dlabs.sas.example.jsso.components.OTPStore;
import ru.dlabs.sas.example.jsso.dto.security.AuthorizedUser;
import ru.dlabs.sas.example.jsso.exception.ChangePasswordException;
import ru.dlabs.sas.example.jsso.exception.InformationException;
import ru.dlabs.sas.example.jsso.service.ChangePasswordService;
import ru.dlabs.sas.example.jsso.service.MessageService;
import ru.dlabs.sas.example.jsso.service.UserService;
import ru.dlabs.sas.example.jsso.utils.SecurityUtils;
import ru.dlabs71.library.email.DEmailSender;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-01</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class DefaultChangePasswordService implements ChangePasswordService {

    private final static String PASSWORD_KEY = "password";

    private final OTPStore otpStore;
    private final ConfirmationStore changePasswordStore;
    private final DEmailSender emailSender;
    private final MessageService messageService;
    private final UserService userService;

    @Override
    public void init(String newPassword, HttpServletResponse response) {
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        OTPStore.GenerationResult generationResult = otpStore.generate(response);
        try {
            ConfirmationStore.StoreItem storeItem = new ConfirmationStore.StoreItem(
                authorizedUser.getEmail(),
                generationResult.otp(),
                Map.of(PASSWORD_KEY, newPassword)
            );
            changePasswordStore.save(storeItem, generationResult.sessionId());
        } catch (Exception e) {
            throw InformationException.builder("$happened.unexpected.error").build();
        }

        emailSender.sendHtmlTemplated(
            authorizedUser.getEmail(),
            messageService.getMessage("email.subject.init.reset.password"),
            "classpath:mail-templates/reset-password-from-account.html",
            ImmutableMap.<String, Object>builder()
                .put("firstName", authorizedUser.getFirstName())
                .put("otp", generationResult.otp())
                .build()
        );
    }

    @Override
    public void confirmChange(String otp, HttpServletRequest request) {
        otp = otp.trim();
        if (!otpStore.validate(otp, request)) {
            throw new ChangePasswordException("$opt.incorrect");
        }

        String sessionId = otpStore.getSessionId(request);
        ConfirmationStore.StoreItem storeItem;
        try {
            storeItem = changePasswordStore.take(sessionId);
        } catch (Exception e) {
            throw InformationException.builder("$happened.unexpected.error").build();
        }

        Map<String, String> extraData = storeItem.extraData();
        if (extraData == null
            || !extraData.containsKey(PASSWORD_KEY)
            || StringUtils.isEmpty(extraData.get(PASSWORD_KEY))) {
            throw new ChangePasswordException("$data.not.found");
        }

        userService.changePassword(storeItem.email(), extraData.get(PASSWORD_KEY));
    }
}
