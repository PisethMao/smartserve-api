package co.istad.smartserve.features.qrcodes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record QrCodeResponse(
        @Schema(description = "Unique identifier of the QR code", example = "1")
        Long id,

        @Schema(description = "ID of the restaurant this QR code belongs to", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID restaurantId,

        @Schema(description = "ID of the table (null for PUBLIC_MENU)", example = "10")
        Integer tableId,

        @Schema(description = "Unique value encoded in the QR code", example = "REST1-TABLE10")
        String qrValue,

        @Schema(description = "Generated QR code image data URI (Base64)", example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...")
        String qrUrl,

        @Schema(description = "Type of the QR code", example = "TABLE_QR")
        String qrType,

        @Schema(description = "Current status of the QR code", example = "true")
        Boolean status,

        @Schema(description = "Timestamp when the QR code was created")
        Instant createdAt,

        @Schema(description = "Timestamp when the QR code was last updated")
        Instant updatedAt
) {
}
