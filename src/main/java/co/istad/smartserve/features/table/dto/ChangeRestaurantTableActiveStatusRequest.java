package co.istad.smartserve.features.table.dto;

import jakarta.validation.constraints.NotNull;

public record ChangeRestaurantTableActiveStatusRequest(
        @NotNull(message = "Status is required")
        Boolean status
) {
}
