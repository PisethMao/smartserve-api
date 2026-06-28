package co.istad.smartserve.features.qrcode;

public interface QrGeneratorAdapter {
    String generateQrValue();
    String generatePublicMenuUrl(String qrValue);
    String generateTableQrUrl(String qrValue);
}
