package co.istad.smartserve.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ConflictException extends RuntimeException {

    private final Map<String, String> validationErrors;

    public ConflictException(String message) {
        super(message);
        this.validationErrors = Map.of("error", message);
    }

    public ConflictException(String field, String message) {
        super(message);
        this.validationErrors = Map.of(field, message);
    }

    public ConflictException(String message, Map<String, String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors == null || validationErrors.isEmpty()
                ? Map.of("error", message)
                : validationErrors;
    }
}