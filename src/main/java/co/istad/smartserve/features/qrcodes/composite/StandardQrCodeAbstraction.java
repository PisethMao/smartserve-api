package co.istad.smartserve.features.qrcodes.composite;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Bridge Pattern: Refined Abstraction.
 * Standard implementation of QrCodeAbstraction.
 * Uses the Bridge Pattern to delegate rendering to QrRenderer implementations.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StandardQrCodeAbstraction implements QrCodeAbstraction {
    
    private final QrRenderer renderer;

    @Override
    public Object produce(byte[] qrBytes, String name) {
        if (qrBytes == null || qrBytes.length == 0) {
            log.error("Cannot produce QR code with null or empty bytes");
            throw new IllegalArgumentException("QR bytes cannot be null or empty");
        }

        if (renderer == null) {
            log.error("QR renderer is not available");
            throw new IllegalStateException("QR renderer is not available");
        }

        try {
            Object result = renderer.render(qrBytes, name);
            log.debug("QR code produced successfully by renderer");
            return result;
        } catch (Exception e) {
            log.error("Error while rendering QR code", e);
            throw new RuntimeException("Failed to produce QR code: " + e.getMessage(), e);
        }
    }
}
