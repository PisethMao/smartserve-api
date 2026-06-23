package co.istad.smartserve.features.menuitems.variant.dto;

import co.istad.smartserve.features.menuitems.MenuItemAvailabilityStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record MenuItemVariantResponse(
        UUID id,
        UUID menuItemId,
        String nameEn,
        String nameKh,
        BigDecimal price,
        Integer displayOrder,
        MenuItemAvailabilityStatus availabilityStatus,
        Boolean defaultVariant,
        Instant createdAt,
        Instant updatedAt
) {
}
