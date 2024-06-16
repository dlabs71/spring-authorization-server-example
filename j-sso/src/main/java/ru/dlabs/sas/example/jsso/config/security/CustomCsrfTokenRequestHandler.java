package ru.dlabs.sas.example.jsso.config.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

import java.util.function.Supplier;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example </div>
 * <div><strong>Creation date:</strong> 2024-06-14 </div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public class CustomCsrfTokenRequestHandler implements CsrfTokenRequestHandler {

    private final XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();

    private static final String COOKIE_NAME = "XSRF-TOKEN";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
        this.delegate.handle(request, response, csrfToken);

        // получаем обновлённый токен
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        // устанавливаем его в куки ответа
        Cookie cookie = new Cookie(COOKIE_NAME, token.getToken());
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
    }

    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        return this.delegate.resolveCsrfTokenValue(request, csrfToken);
    }
}
