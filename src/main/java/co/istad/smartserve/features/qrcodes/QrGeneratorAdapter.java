package co.istad.smartserve.features.qrcodes;

import java.io.IOException;

/**
 * Adapter Pattern: Interface to wrap 3rd-party QR libraries.
 */
public interface QrGeneratorAdapter {
    byte[] generateQrCodeImage(String text, int width, int height) throws IOException;
}
