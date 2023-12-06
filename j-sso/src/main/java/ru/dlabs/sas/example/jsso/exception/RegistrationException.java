package ru.dlabs.sas.example.jsso.exception;

public class RegistrationException extends RuntimeException {

    public RegistrationException(String description) {
        super(description);
    }

    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
