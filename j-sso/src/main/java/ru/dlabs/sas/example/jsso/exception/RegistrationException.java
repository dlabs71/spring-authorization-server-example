package ru.dlabs.sas.example.jsso.exception;

import ru.dlabs.sas.example.jsso.type.ErrorLevel;

public class RegistrationException extends InformationException {

    public RegistrationException(String description) {
        super(description, null, ErrorLevel.ERROR);
    }

    public RegistrationException(String message, Throwable cause) {
        super(message, cause, ErrorLevel.ERROR);
    }
}
