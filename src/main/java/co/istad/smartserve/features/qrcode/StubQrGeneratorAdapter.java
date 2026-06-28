package co.istad.smartserve.features.qrcode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StubQrGeneratorAdapter implements QrGeneratorAdapter {
    @Value("${app.frontend.base-url:http://localhost:3000}")
    private String frontendBaseUrl;

    @Override
    public String generateQrValue() {
        return "qr_" + UUID.randomUUID()
                .toString()
                .replace("-", "");
    }

    private String normalizeBaseUrl() {
        if (frontendBaseUrl.endsWith("/")) {
            return frontendBaseUrl.substring(0, frontendBaseUrl.length() - 1);
        }
        return frontendBaseUrl;
    }

    @Override
    public String generatePublicMenuUrl(String qrValue) {
        return normalizeBaseUrl() + "/public-menu/" + qrValue;
    }

    @Override
    public String generateTableQrUrl(String qrValue) {
        return normalizeBaseUrl() + "/table-menu/" + qrValue;
    }
}
