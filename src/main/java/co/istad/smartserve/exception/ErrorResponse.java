package co.istad.smartserve.exception;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path,
        Map<String, String> validationErrors
) {
}
