package co.istad.smartserve.features.qrcode.event;

import co.istad.smartserve.features.qrcode.QrCodeType;

import java.time.Instant;
import java.util.UUID;

public record QrCodeChangedEvent(
        UUID qrCodeId,
        UUID restaurantId,
        UUID tableId,
        String qrValue,
        QrCodeType qrCodeType,
        String action,
        Instant occurredAt
) {
}
