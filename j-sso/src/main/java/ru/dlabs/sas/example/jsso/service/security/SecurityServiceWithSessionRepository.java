package ru.dlabs.sas.example.jsso.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import ru.dlabs.sas.example.jsso.dto.security.AuthorizedUser;

/**
 * Сервис управления Security Context. Пример того же SecurityService,
 * только вместо SecurityContextRepository используется SessionRepository.
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
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SecurityServiceWithSessionRepository {

    private final SessionRepository sessionRepository;

    private final static String SECURITY_CONTEXT_ATTR = "SPRING_SECURITY_CONTEXT";

    /**
     * Обновление информации о пользователе в Security Context на основе подготовленной DTO.
     */
    public void reloadSecurityContext(AuthorizedUser authorizedUser) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        this.reloadAuthenticationWithNewPrincipal(authorizedUser);

        Session session = sessionRepository.findById(sessionId);
        session.setAttribute(SECURITY_CONTEXT_ATTR, SecurityContextHolder.getContext());
        sessionRepository.save(session);
    }

    /**
     * Обновление объекта Authentication в Security Context используя новую информацию о пользователе из указанной DTO.
     */
    private void reloadAuthenticationWithNewPrincipal(AuthorizedUser principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UnsupportedOperationException("Authentication object is null");
        }

        if (authentication instanceof UsernamePasswordAuthenticationToken auth) {
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                principal,
                auth.getCredentials(),
                auth.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);
            return;
        }
        if (authentication instanceof OAuth2AuthenticationToken auth) {
            Authentication newAuth = new OAuth2AuthenticationToken(
                principal,
                auth.getAuthorities(),
                auth.getAuthorizedClientRegistrationId()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);
            return;
        }
        throw new UnsupportedOperationException(
            "Authentication type " + authentication.getClass() + " is not supported");
    }
}
