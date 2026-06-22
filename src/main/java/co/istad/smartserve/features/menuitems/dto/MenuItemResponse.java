package co.istad.smartserve.features.menuitems.dto;

import co.istad.smartserve.features.menuitems.MenuItemAvailabilityStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record MenuItemResponse(
        UUID id,
        UUID restaurantId,
        UUID categoryId,
        String categoryNameEn,
        String categoryNameKh,
        String nameEn,
        String nameKh,
        String descriptionEn,
        String descriptionKh,
        BigDecimal price,
        String imageUrl,
        Integer soldLimit,
        MenuItemAvailabilityStatus availabilityStatus,
        Boolean status,
        Instant createdAt,
        Instant updatedAt
) {
}
