package co.istad.smartserve.features.menuitems.variant.dto;

import co.istad.smartserve.features.menuitems.MenuItemAvailabilityStatus;
import jakarta.validation.constraints.NotNull;

public record MenuItemVariantStatusRequest(
        @NotNull(message = "Availability status is required")
        MenuItemAvailabilityStatus availabilityStatus
) {
}
