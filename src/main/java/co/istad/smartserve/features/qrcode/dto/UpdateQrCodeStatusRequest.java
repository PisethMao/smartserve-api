package co.istad.smartserve.features.qrcode.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateQrCodeStatusRequest(
        @NotNull(message = "Status is required")
        Boolean status
) {
}
