package co.istad.smartserve.features.qrcode.dto;

import co.istad.smartserve.features.qrcode.QrCodeType;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateQrCodeRequest(
        @NotNull(message = "QR code type is required")
        QrCodeType qrCodeType,
        UUID tableId
) {
}
