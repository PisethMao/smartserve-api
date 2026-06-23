package co.istad.smartserve.exception;

/**
 * Custom exception for QR code generation failures.
 */
public class QrCodeGenerationException extends RuntimeException {
    public QrCodeGenerationException(String message) {
        super(message);
    }

    public QrCodeGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
