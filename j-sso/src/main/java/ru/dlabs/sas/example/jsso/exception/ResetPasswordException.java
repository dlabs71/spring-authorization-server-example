package ru.dlabs.sas.example.jsso.exception;

import ru.dlabs.sas.example.jsso.type.ErrorLevel;

public class ResetPasswordException extends InformationException {

    public ResetPasswordException(String description) {
        super(description, null, ErrorLevel.ERROR);
    }

    public ResetPasswordException(String message, Throwable cause) {
        super(message, cause, ErrorLevel.ERROR);
    }
}
