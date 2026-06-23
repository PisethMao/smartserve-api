package co.istad.smartserve.features.menuitems.variant.dto;

import co.istad.smartserve.features.menuitems.MenuItemAvailabilityStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record MenuItemVariantCreateRequest(
        @NotBlank(message = "Variant English name is required")
        String nameEn,
        String nameKh,
        @NotNull(message = "Variant price is required")
        @PositiveOrZero(message = "Variant price must be zero or positive")
        BigDecimal price,
        @NotNull(message = "Display order is required")
        @Positive(message = "Display order must be greater than 0")
        Integer displayOrder,
        MenuItemAvailabilityStatus availabilityStatus,
        Boolean defaultVariant
) {
}
