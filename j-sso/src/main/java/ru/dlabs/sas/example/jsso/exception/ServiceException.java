package ru.dlabs.sas.example.jsso.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class ServiceException extends AuthenticationException {

    public ServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ServiceException(String msg) {
        super(msg);
    }
}
