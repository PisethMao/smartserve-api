package co.istad.smartserve.exception;

public class QrCodeGenerationException extends RuntimeException {
    public QrCodeGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
