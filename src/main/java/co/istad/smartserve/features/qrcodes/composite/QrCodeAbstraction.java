package co.istad.smartserve.features.qrcodes.composite;

/**
 * Bridge Pattern: Abstraction interface.
 * Decouples the QR Code abstraction from its concrete rendering implementation.
 */
public interface QrCodeAbstraction {
    Object produce(byte[] qrBytes, String name);
}
