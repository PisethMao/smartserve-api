package co.istad.smartserve.features.menuitems.dto;

import co.istad.smartserve.features.menuitems.MenuItemAvailabilityStatus;
import jakarta.validation.constraints.NotNull;

public record MenuItemAvailabilityRequest(
        @NotNull(message = "Availability status is required")
        MenuItemAvailabilityStatus availabilityStatus
) {
}
