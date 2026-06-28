package co.istad.smartserve.features.qrcode.dto;

import co.istad.smartserve.features.qrcode.QrCodeType;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record QrCodeResponse(
        UUID id,
        UUID restaurantId,
        UUID tableId,
        String qrValue,
        String qrUrl,
        QrCodeType qrCodeType,
        Boolean status,
        Instant createdAt,
        Instant updatedAt
) {
}
