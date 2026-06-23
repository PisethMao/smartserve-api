package co.istad.smartserve.features.qrcodes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.UUID;

@Builder
public record QrCodeRequest(
        @NotNull(message = "Restaurant ID cannot be null")
        @Schema(description = "ID of the restaurant", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID restaurantId,

        @Positive(message = "Table ID must be positive if provided")
        @Schema(description = "ID of the table (required for TABLE_QR)", example = "10")
        Integer tableId,

        @NotBlank(message = "QR Type cannot be blank")
        @Pattern(regexp = "^(PUBLIC_MENU|TABLE_QR)$", message = "QR Type must be either PUBLIC_MENU or TABLE_QR")
        @Schema(description = "Type of the QR code", example = "TABLE_QR", allowableValues = {"PUBLIC_MENU", "TABLE_QR"})
        String qrType,

        @NotBlank(message = "QR Value cannot be blank")
        @Size(max = 2953, message = "QR Value exceeds maximum QR code capacity")
        @Schema(description = "Unique value encoded in the QR code", example = "REST1-TABLE10")
        String qrValue,

        @NotBlank(message = "QR URL cannot be blank")
        @Size(max = 2083, message = "QR URL is too long")
        @Schema(description = "Base URL for the QR code", example = "https://smartserve.io/r/1")
        String qrUrl,

        @NotNull(message = "Status cannot be null")
        @Schema(description = "Initial status of the QR code", example = "true")
        Boolean status
) {
}
