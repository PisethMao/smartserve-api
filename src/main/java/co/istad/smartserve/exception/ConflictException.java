package co.istad.smartserve.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ConflictException extends RuntimeException {
    private final Map<String, String> validationErrors;

    public ConflictException(String message) {
        super(message);
        this.validationErrors = null;
    }

    public ConflictException(String message, Map<String, String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

}