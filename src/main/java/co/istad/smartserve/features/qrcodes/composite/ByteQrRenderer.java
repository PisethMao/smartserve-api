package co.istad.smartserve.features.qrcodes.composite;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;

/**
 * QR Renderer implementation that converts QR code bytes to Base64 data URI.
 */
@Slf4j
@Component
public class ByteQrRenderer implements QrRenderer {
    
    private static final String DATA_URI_PREFIX = "data:image/png;base64,";
    private static final int MAX_BASE64_SIZE = 1_000_000; // ~750KB actual data

    @Override
    public String render(byte[] qrBytes, String fileName) {
        if (qrBytes == null || qrBytes.length == 0) {
            throw new IllegalArgumentException("QR bytes cannot be null or empty");
        }

        if (qrBytes.length > MAX_BASE64_SIZE) {
            log.warn("QR code image exceeds maximum size. Size: {} bytes", qrBytes.length);
            throw new IllegalArgumentException("QR code image is too large");
        }

        try {
            String base64Encoded = Base64.getEncoder().encodeToString(qrBytes);
            String dataUri = DATA_URI_PREFIX + base64Encoded;
            log.debug("QR code rendered successfully. Base64 size: {}", base64Encoded.length());
            return dataUri;
        } catch (OutOfMemoryError e) {
            log.error("Out of memory while encoding QR code to Base64", e);
            throw new RuntimeException("Failed to render QR code: out of memory", e);
        }
    }
}
