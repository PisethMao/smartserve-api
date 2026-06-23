package co.istad.smartserve.features.menuitems.variant.dto;

import co.istad.smartserve.features.menuitems.MenuItemAvailabilityStatus;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record MenuItemVariantUpdateRequest(
        String nameEn,
        String nameKh,
        @PositiveOrZero(message = "Variant price must be zero or positive")
        BigDecimal price,
        Integer displayOrder,
        MenuItemAvailabilityStatus availabilityStatus,
        Boolean defaultVariant
) {
}
