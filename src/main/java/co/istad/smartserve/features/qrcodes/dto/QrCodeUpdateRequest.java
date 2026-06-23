package co.istad.smartserve.features.qrcodes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record QrCodeUpdateRequest(
        @Positive(message = "Table ID must be positive if provided")
        @Schema(description = "ID of the table", example = "10")
        Integer tableId,

        @Pattern(regexp = "^(PUBLIC_MENU|TABLE_QR)$", message = "QR Type must be either PUBLIC_MENU or TABLE_QR")
        @Schema(description = "Type of the QR code", example = "TABLE_QR", allowableValues = {"PUBLIC_MENU", "TABLE_QR"})
        String qrType,

        @Size(max = 2953, message = "QR Value exceeds maximum QR code capacity")
        @Schema(description = "Unique value encoded in the QR code", example = "REST1-TABLE10-V2")
        String qrValue,

        @Size(max = 2083, message = "QR URL is too long")
        @Schema(description = "Base URL for the QR code", example = "https://smartserve.io/r/1/v2")
        String qrUrl,

        @Schema(description = "Status of the QR code", example = "true")
        Boolean status
) {
}
