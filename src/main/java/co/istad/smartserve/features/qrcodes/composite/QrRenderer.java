package co.istad.smartserve.features.qrcodes.composite;

/**
 * Bridge Pattern: Implementation Interface for Rendering.
 */
public interface QrRenderer {
    Object render(byte[] qrBytes, String fileName);
}
