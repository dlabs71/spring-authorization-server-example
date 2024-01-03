package ru.dlabs.sas.example.jsso.service;

import com.google.common.collect.ImmutableMap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.dlabs.sas.example.jsso.components.OTPStore;
import ru.dlabs.sas.example.jsso.components.ResetPasswordStore;
import ru.dlabs.sas.example.jsso.config.security.AuthorizationServerProperties;
import ru.dlabs.sas.example.jsso.dao.entity.UserEntity;
import ru.dlabs.sas.example.jsso.exception.InformationException;
import ru.dlabs.sas.example.jsso.exception.ResetPasswordException;
import ru.dlabs.sas.example.jsso.utils.CryptoUtils;
import ru.dlabs71.library.email.DEmailSender;

@Service
@RequiredArgsConstructor
public class DefaultResetPasswordService implements ResetPasswordService {

    private static final String SESSION_ID_HEADER = "reset-password-session";

    private final DEmailSender emailSender;
    private final OTPStore otpStore;
    private final ResetPasswordStore resetPasswordStore;
    private final AuthorizationServerProperties authorizationServerProperties;
    private final UserService userService;
    private final MessageService messageService;

    @Override
    public void initial(String email, HttpServletResponse response) {
        email = email.trim().toLowerCase();
        if (!userService.existByEmail(email)) {
            throw InformationException.builder("$email.not.found").build();
        }

        OTPStore.GenerationResult generationResult = otpStore.generate(response);
        try {
            resetPasswordStore.save(
                new ResetPasswordStore.StoreItem(email, generationResult.otp()),
                generationResult.sessionId()
            );
        } catch (Exception e) {
            throw InformationException.builder("$happened.unexpected.error").build();
        }
        UserEntity user = userService.findByEmail(email);
        emailSender.sendHtmlTemplated(
            email,
            messageService.getMessage("email.subject.init.reset.password"),
            "classpath:mail-templates/reset-password-confirmed.html",
            ImmutableMap.<String, Object>builder()
                .put("firstName", user.getFirstName())
                .put("otp", generationResult.otp())
                .build()
        );
    }

    @Override
    public void confirmEmail(String otp, HttpServletRequest request) {
        otp = otp.trim();
        if (!otpStore.validate(otp, request)) {
            throw new ResetPasswordException("$opt.incorrect");
        }

        // по идентификатору по OTPStore получаем данные из resetPasswordStore. Там находиться email пользователя.
        // Он был сохранён на первом шаге.
        String sessionId = otpStore.getSessionId(request);
        ResetPasswordStore.StoreItem storeItem;
        try {
            storeItem = resetPasswordStore.take(sessionId);
        } catch (Exception e) {
            throw InformationException.builder("$happened.unexpected.error").build();
        }

        // Генерируем специальный идентификатор сессии, который укажем в email сообщении.
        String resetPasswordSessionId = CryptoUtils.hash(sessionId + "-" + otp);
        try {
            resetPasswordStore.save(storeItem, resetPasswordSessionId);
        } catch (Exception e) {
            throw InformationException.builder("$happened.unexpected.error").build();
        }

        // Находим пользователя по email в БД. Он нам нужен, для того чтобы добавить в сообщение имя пользователя.
        UserEntity user = userService.findByEmail(storeItem.email());

        // отправляем email сообщение.
        emailSender.sendHtmlTemplated(
            storeItem.email(),
            messageService.getMessage("email.subject.reset.password"),
            "classpath:mail-templates/reset-password.html",
            ImmutableMap.<String, Object>builder()
                .put("firstName", user.getFirstName())
                .put("resetPasswordUrl", this.getResetPasswordUrl(resetPasswordSessionId))
                .build()
        );
    }

    /**
     * Генерация URL на форму сброса пароля
     *
     * @param sessionId специальный идентификатор сессии
     */
    private String getResetPasswordUrl(String sessionId) {
        String httpUrl = authorizationServerProperties.getIssuerUrl()
            + authorizationServerProperties.getResetPasswordEndpoint();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpUrl);
        builder.queryParam("resetSessionId", sessionId);
        return builder.build().toUriString();
    }

    @Override
    public void setNewPassword(String newPassword, HttpServletRequest request) {
        // Проверяем существует ли в запросе специальный заголовок
        if (request.getHeader(SESSION_ID_HEADER) == null) {
            throw new ResetPasswordException("$reset.password.broke");
        }

        // Пытаемся получить значение специального заголовка.
        String resetPasswordSessionId = request.getHeader(SESSION_ID_HEADER);

        // Пытаемся получить данные из resetPasswordStore по значению из заголовка
        ResetPasswordStore.StoreItem storeItem;
        try {
            storeItem = resetPasswordStore.take(resetPasswordSessionId);
        } catch (Exception e) {
            throw InformationException.builder("$happened.unexpected.error").build();
        }

        // Если данных нет, то выбрасываем ошибку
        if (storeItem == null) {
            throw new ResetPasswordException("$reset.password.broke");
        }

        // Если данные есть, то меняем пароль у пользователя. Email берём тот который получили из resetPasswordStore
        userService.changePassword(storeItem.email(), newPassword);
    }
}
