package ru.dlabs.sas.example.jsso.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
public class ServiceException extends RuntimeException {

    private String description;

    public ServiceException(String description, Throwable cause) {
        super(cause.getMessage(), cause);
        this.description = description;
    }

    public static InformationException.InformationExceptionBuilder builder(String description, Throwable throwable) {
        return new InformationException.InformationExceptionBuilder()
            .description(description)
            .throwable(throwable);
    }

    public static InformationException.InformationExceptionBuilder builder(String description) {
        return new InformationException.InformationExceptionBuilder()
            .description(description);
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class ServiceExceptionBuilder {

        private String description;
        private Throwable throwable;

        ServiceExceptionBuilder() {
        }

        public ServiceException build() {
            return new ServiceException(description, throwable);
        }
    }
}
