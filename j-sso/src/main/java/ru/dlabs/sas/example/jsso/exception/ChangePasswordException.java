package ru.dlabs.sas.example.jsso.exception;


import ru.dlabs.sas.example.jsso.type.ErrorLevel;

public class ChangePasswordException extends InformationException {

    public ChangePasswordException(String description) {
        super(description, null, ErrorLevel.ERROR);
    }

    public ChangePasswordException(String message, Throwable cause) {
        super(message, cause, ErrorLevel.ERROR);
    }
}
