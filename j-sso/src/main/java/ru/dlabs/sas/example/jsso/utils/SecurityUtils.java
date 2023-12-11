package ru.dlabs.sas.example.jsso.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.dlabs.sas.example.jsso.dto.AuthorizedUser;
import ru.dlabs.sas.example.jsso.exception.ServiceException;

@UtilityClass
public class SecurityUtils {

    public UsernamePasswordAuthenticationToken getAuthorization() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UsernamePasswordAuthenticationToken authenticationImpl) {
            return authenticationImpl;
        }
        throw ServiceException.builder("Authentication type not supported").build();
    }

    public AuthorizedUser getAuthUser() {
        Object principal = getAuthorization().getPrincipal();
        if (principal instanceof AuthorizedUser authorizedUser) {
            return authorizedUser;
        }
        throw new RuntimeException("Principal class = " + principal.getClass().getSimpleName() + " is not supported");
    }

}
