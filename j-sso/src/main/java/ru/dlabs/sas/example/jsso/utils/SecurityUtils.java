package ru.dlabs.sas.example.jsso.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.dlabs.sas.example.jsso.dto.AuthorizedUser;
import ru.dlabs.sas.example.jsso.exception.ServiceException;

@UtilityClass
public class SecurityUtils {

    public AuthorizedUser getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw ServiceException.builder("Authentication type not supported").build();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof AuthorizedUser authorizedUser) {
            return authorizedUser;
        }
        throw ServiceException.builder(
            "Principal class = " + principal.getClass().getSimpleName() + " is not supported").build();
    }

}
