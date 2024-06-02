package ru.dlabs.sas.example.jsso.config.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import ru.dlabs.sas.example.jsso.dao.type.UserEventType;
import ru.dlabs.sas.example.jsso.service.UserEventService;

/**
 * Это всё тот же стандартный SavedRequestAwareAuthenticationSuccessHandler,
 * только добавлено сохранение события успешной аутентификации.
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-22</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public class CustomOauthAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserEventService eventService;
    private final RequestCache requestCache = new HttpSessionRequestCache();

    public CustomOauthAuthenticationSuccessHandler(
        String authSuccessUrl,
        UserEventService eventService
    ) {
        this.eventService = eventService;
        this.setDefaultTargetUrl(authSuccessUrl);
        this.setRequestCache(requestCache);
    }

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws ServletException, IOException {
        super.onAuthenticationSuccess(request, response, authentication);
        SavedRequest savedRequest = this.requestCache.getRequest(request, response);
        if (savedRequest != null) {
            this.requestCache.removeRequest(request, response);
        }

        String clientId = HandlerUtils.getClientId(savedRequest);
        eventService.createEvent(UserEventType.USER_LOGIN, clientId, request);
    }
}
